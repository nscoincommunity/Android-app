package com.stocksexchange.android.ui.auth

import com.stocksexchange.android.R
import com.stocksexchange.android.model.MaterialDialogBuilder
import com.stocksexchange.android.model.PinCode
import com.stocksexchange.android.model.PinCodeMode
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.managers.AppLockManager
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.core.providers.FingerprintProvider

class AuthenticationPresenter(
    view: AuthenticationContract.View,
    model: AuthenticationModel,
    private val sessionManager: SessionManager,
    private val appLockManager: AppLockManager,
    private val fingerprintProvider: FingerprintProvider
) : BasePresenter<AuthenticationContract.View, AuthenticationModel>(view, model), AuthenticationContract.ActionListener,
    AuthenticationModel.ActionListener {


    companion object {

        private const val PIN_CODE_LENGTH = 4

        private val COMMON_PIN_CODES = arrayOf("1234", "1111", "0000", "1212", "7777")

    }


    var pinCode: String = ""
    var confirmedPinCode: String = ""

    var pinCodeMode: PinCodeMode = PinCodeMode.ENTER

    lateinit var transitionAnimations: TransitionAnimations
    lateinit var theme: Theme

    private var mAreAuthVariablesInitialized: Boolean = false
    private var mIsTimerRunning: Boolean = false
    private var mIsTimerCanceled: Boolean = false




    init {
        model.setActionListener(this)
    }


    private fun updatePinCode(newPinCode: String) {
        pinCode = newPinCode
    }


    private fun updateConfirmedPinCode(newPinCode: String) {
        confirmedPinCode = newPinCode
    }


    private fun updatePinCodeMode(newMode: PinCodeMode) {
        pinCodeMode = newMode
    }


    private fun clearPinCode() {
        updatePinCode("")
    }


    private fun clearConfirmedPinCode() {
        updateConfirmedPinCode("")
    }


    override fun start() {
        super.start()

        if(!mAreAuthVariablesInitialized) {
            mModel.initAuthVariables {
                restartTimerIfNecessary()
                runStartingChecks()
            }

            mAreAuthVariablesInitialized = true
        } else {
            runStartingChecks()
        }
    }


    private fun runStartingChecks() {
        updateFingerprintScannerButtonState()

        if(mIsTimerCanceled) {
            if(!restartTimerIfNecessary()) {
                mView.setMessageText(getHintMessage(), false)
                enableAuthentication()
            }

            mIsTimerCanceled = false
        }

        if(isFingerprintEnabled() &&
            !isFingerprintScannerExhausted() &&
            !isInProtectionMode()) {
            mView.showFingerprintAuthDialog()
        }
    }


    override fun stop() {
        super.stop()

        mView.hideFingerprintDialog()

        if(isInProtectionMode()) {
            mView.cancelTimer()
        }
    }


    private fun updateFingerprintScannerButtonState() {
        if(!isFingerprintEnabled()) {
            mView.hideKeypadFingerprintScannerButton()
            return
        }

        if(isFingerprintScannerExhausted()) {
            mView.disableKeypadFingerprintScannerButton()

            if(isInProtectionMode()) {
                mView.hideKeypadFingerprintOverlayView()
            } else {
                mView.showKeypadFingerprintOverlayView()
            }

            return
        }

        if(!isInProtectionMode()) {
            mView.showKeypadFingerprintScannerButton()
            mView.enableKeypadFingerprintScannerButton()
            mView.hideKeypadFingerprintOverlayView()
        }
    }


    private fun isFingerprintEnabled(): Boolean {
        return ((pinCodeMode == PinCodeMode.ENTER) &&
                sessionManager.getSettings().isFingerprintUnlockEnabled)
    }


    private fun isFingerprintScannerExhausted(): Boolean {
        return mModel.areFingerprintAttemptsUsedUp()
    }


    private fun isInProtectionMode(): Boolean {
        return mIsTimerRunning
    }


    override fun onFingerprintRegistrationDialogButtonClicked() {
        finishAuthentication()
    }


    override fun onFingerprintRegistrationSucceeded() {
        val newSettings = sessionManager.getSettings().copy(
            isFingerprintUnlockEnabled = true
        )

        mModel.updateSettings(newSettings) {
            sessionManager.setSettings(newSettings)

            finishAuthentication()
        }
    }


    override fun onFingerprintRegistrationFailed() {
        finishAuthentication()
    }


    override fun onFingerprintAuthDialogButtonClicked() {
        mView.hideFingerprintDialog()
    }


    override fun onFingerprintRecognized() {
        mModel.resetAuthData()

        onUserAuthenticated()
    }


    override fun onFingerprintAttemptsWasted() {
        mModel.setFingerprintAttemptsUsedUp(true)

        mView.disableKeypadFingerprintScannerButton()
        mView.showKeypadFingerprintOverlayView()
    }


    override fun onUserAuthenticated() {
        val lastAuthTimestamp = System.currentTimeMillis()

        mModel.saveLastAuthTimestamp(lastAuthTimestamp) {
            appLockManager.updateLastAuthenticationTimestamp(lastAuthTimestamp)

            registerFingerprintIfNeeded()
        }
    }


    private fun registerFingerprintIfNeeded() {
        if((pinCodeMode != PinCodeMode.CONFIRMATION) ||
            !fingerprintProvider.isHardwareAvailable() ||
            !fingerprintProvider.hasEnrolledFingerprints() ||
            sessionManager.getSettings().isFingerprintUnlockEnabled) {
            finishAuthentication()
            return
        }

        mView.showFingerprintRegistrationDialog()
    }


    private fun finishAuthentication() {
        mView.launchDestinationActivityIfNecessary()
        mView.setActivityResult(true)
        mView.finishActivity()
    }


    override fun onKeypadDigitButtonClicked(digit: String) {
        val newPinCode: String

        if(pinCodeMode != PinCodeMode.CONFIRMATION) {
            newPinCode = (pinCode + digit)
            updatePinCode(newPinCode)
        } else {
            newPinCode = (confirmedPinCode + digit)
            updateConfirmedPinCode(newPinCode)
        }

        mView.updatePinBoxContainer(newPinCode)

        if(newPinCode.length == PIN_CODE_LENGTH) {
            mView.startIntentionalDelay(pinCodeMode, newPinCode)
        }
    }


    override fun onKeypadFingerprintButtonClicked() {
        mView.showFingerprintAuthDialog()
    }


    override fun onKeypadFingerprintOverlayClicked() {
        mView.showToast(mStringProvider.getString(
            R.string.authentication_activity_fingerprint_exhausted_message
        ))
    }


    override fun onIntentionalDelayFinished(pinCodeMode: PinCodeMode, pinCode: String) {
        when(pinCodeMode) {
            PinCodeMode.CREATION -> onPinCodeEnteredOnCreation(pinCode)
            PinCodeMode.CONFIRMATION -> onPinCodeEnteredOnConfirmation(pinCode)
            PinCodeMode.ENTER -> onPinCodeEnteredOnEnter(pinCode)
            PinCodeMode.CHANGE -> onPinCodeEnteredOnChange(pinCode)
        }
    }


    private fun onPinCodeEnteredOnCreation(pinCode: String) {
        if(sessionManager.getSettings().pinCode.code == pinCode) {
            mView.showToast(mStringProvider.getString(R.string.error_invalid_new_pin))
            clearPinCode()
            mView.clearPinBoxContainer()

            return
        }

        if(COMMON_PIN_CODES.contains(pinCode)) {
            showCommonPinCodesDialog()
            return
        }

        proceedToConfirmationStep()
    }


    private fun showCommonPinCodesDialog() {
        val title = mStringProvider.getString(
            R.string.authentication_activity_common_pin_codes_dialog_title
        )
        val content = mStringProvider.getString(
            R.string.authentication_activity_common_pin_codes_dialog_message
        )
        val negativeBtnText = mStringProvider.getString(
            R.string.authentication_activity_common_pin_codes_dialog_negative_button_text
        )
        val positiveBtnText = mStringProvider.getString(
            R.string.authentication_activity_common_pin_codes_dialog_positive_button_text
        )

        mView.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            title = title,
            content = content,
            negativeBtnText = negativeBtnText,
            positiveBtnText = positiveBtnText,
            negativeBtnClick = {
                onCommonPinCodesDialogNegativeButtonClicked()
            },
            positiveBtnClick = {
                onCommonPinCodesDialogPositiveButtonClicked()
            }
        ))
    }


    private fun onPinCodeEnteredOnConfirmation(pinCode: String) {
        if(this.pinCode != pinCode) {
            mView.showToast(mStringProvider.getString(R.string.error_pins_mismatch))

            updatePinCodeMode(PinCodeMode.CREATION)

            mView.setMessageText(getHintMessage(), true)

            clearPinCode()
            clearConfirmedPinCode()

            mView.clearPinBoxContainer()

            return
        }

        val newSettings = sessionManager.getSettings().copy(pinCode = PinCode(pinCode))

        mModel.updateSettings(newSettings) {
            sessionManager.setSettings(newSettings)

            onUserAuthenticated()
        }
    }


    private fun onPinCodeEnteredOnEnter(pinCode: String) {
        if(sessionManager.getSettings().pinCode.code != pinCode) {
            clearPinCode()

            mView.clearPinBoxContainer()

            applyProtectionMeasureIfNecessary()

            return
        }

        mModel.resetAuthData()

        onUserAuthenticated()
    }


    private fun onPinCodeEnteredOnChange(pinCode: String) {
        if(sessionManager.getSettings().pinCode.code != pinCode) {
            clearPinCode()
            mView.clearPinBoxContainer()

            applyProtectionMeasureIfNecessary()

            return
        }

        mModel.resetAuthData()

        updatePinCodeMode(PinCodeMode.CREATION)
        mView.setMessageText(getHintMessage(), true)

        clearPinCode()
        mView.clearPinBoxContainer()
    }


    private fun proceedToConfirmationStep() {
        updatePinCodeMode(PinCodeMode.CONFIRMATION)

        mView.setMessageText(getHintMessage(), true)
        mView.clearPinBoxContainer()
    }


    private fun enableAuthentication() {
        mView.enableKeypadDigitButtons()
        mView.enableKeypadDeleteButton()

        if(isFingerprintEnabled()) {
            if(isFingerprintScannerExhausted()) {
                mView.showKeypadFingerprintOverlayView()
            } else {
                mView.enableKeypadFingerprintScannerButton()
            }
        }
    }


    private fun disableAuthentication() {
        mView.disableKeypadDigitButtons()
        mView.disableKeypadDeleteButton()

        if(isFingerprintEnabled()) {
            if(isFingerprintScannerExhausted()) {
                mView.hideKeypadFingerprintOverlayView()
            } else {
                mView.disableKeypadFingerprintScannerButton()
            }
        }
    }


    private fun startTimer(millisInFuture: Long, animateMessage: Boolean) {
        mView.setMessageText(getTimerMessage(millisInFuture), animateMessage) {
            mView.startTimer(
                millisInFuture,
                AuthenticationModel.TIMER_INTERVAL,
                AuthenticationModel.TIMER_MIN_FINISH_TIME
            )
        }

        mIsTimerRunning = true
    }


    private fun restartTimerIfNecessary(): Boolean {
        val timeLeftToWait = (mModel.getAllowAuthTimestamp() - System.currentTimeMillis())

        return if(timeLeftToWait > 0L) {
            disableAuthentication()
            startTimer(timeLeftToWait, false)

            true
        } else {
            false
        }
    }


    private fun applyProtectionMeasureIfNecessary() {
        mModel.incrementInvalidPinCodeAttemptsNumber()

        if(mModel.getInvalidPinCodeAttemptsNumber() >= AuthenticationModel.MAX_INVALID_PIN_CODE_ATTEMPTS_NUMBER) {
            mModel.updateAllowAuthTimestamp()

            disableAuthentication()
            startTimer(AuthenticationModel.TIMER_DURATION, true)

            showInvalidPinCodeAttemptsDialog(mModel.getInvalidPinCodeAttemptsNumber())
        } else {
            mView.showToast(mStringProvider.getString(R.string.error_invalid_pin))
        }
    }


    private fun showInvalidPinCodeAttemptsDialog(invalidPinCodeAttemptsNumber: Int) {
        val content = mStringProvider.getString(
            R.string.error_pin_code_too_many_attempts,
            invalidPinCodeAttemptsNumber
        )

        mView.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            content = content,
            positiveBtnText = mStringProvider.getString(R.string.ok)
        ))
    }


    private fun onCommonPinCodesDialogNegativeButtonClicked() {
        clearPinCode()
        mView.clearPinBoxContainer()
    }


    private fun onCommonPinCodesDialogPositiveButtonClicked() {
        proceedToConfirmationStep()
    }


    override fun onKeypadDeleteButtonClicked() {
        val pinCode = if(pinCodeMode == PinCodeMode.CONFIRMATION) {
            confirmedPinCode
        } else {
            pinCode
        }

        pinCode.takeIf { it.isNotEmpty() }?.apply {
            val newPinCode = dropLast(1)

            if(pinCodeMode == PinCodeMode.CONFIRMATION) {
                updateConfirmedPinCode(newPinCode)
            } else {
                updatePinCode(newPinCode)
            }

            mView.updatePinBoxContainer(newPinCode)
        }
    }


    override fun onHelpButtonClicked() {
        mView.launchPinRecoveryActivity()
    }


    override fun onTimerTick(millisecondsTillFinished: Long) {
        updateTimerMessage(millisecondsTillFinished)
    }


    override fun onTimerCancelled() {
        mIsTimerRunning = false
        mIsTimerCanceled = true
    }


    override fun onTimerFinished() {
        mIsTimerRunning = false

        mView.setMessageText(getHintMessage(), true) {
            mModel.deleteAllowAuthTimestamp()

            enableAuthentication()
        }
    }


    private fun updateTimerMessage(millisecondsTillFinished: Long) {
        mView.setMessageText(getTimerMessage(millisecondsTillFinished))
    }


    private fun getTimerMessage(millisecondsTillFinished: Long): String {
        val secondsLeft = (millisecondsTillFinished / AuthenticationModel.TIMER_INTERVAL).toInt()

        return mStringProvider.getQuantityString(
            R.plurals.authentication_activity_count_down_timer_template,
            secondsLeft,
            secondsLeft
        )
    }


    private fun getHintMessage(): String {
        return mStringProvider.getMessageForPinCodeMode(pinCodeMode)
    }


    override fun onBackPressed(): Boolean {
        if(pinCodeMode == PinCodeMode.CHANGE) {
            mView.setActivityResult(false)
        }

        return super.onBackPressed()
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            mAreAuthVariablesInitialized = it.areAuthVariablesInitialized
            mIsTimerCanceled = it.isTimerCancelled
            pinCode = it.pinCode
            confirmedPinCode = it.confirmedPinCode
            pinCodeMode = it.pinCodeMode
            transitionAnimations = it.transitionAnimations
            theme = it.theme
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            areAuthVariablesInitialized = mAreAuthVariablesInitialized,
            isTimerCancelled = mIsTimerCanceled,
            pinCode = pinCode,
            confirmedPinCode = confirmedPinCode,
            pinCodeMode = pinCodeMode,
            transitionAnimations = transitionAnimations,
            theme = theme
        ))
    }


}