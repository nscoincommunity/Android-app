package com.stocksexchange.android.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.style.UnderlineSpan
import android.widget.ImageView
import androidx.core.text.toSpannable
import com.stocksexchange.android.R
import com.stocksexchange.android.model.PinCodeMode
import com.stocksexchange.android.model.SystemWindowType
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.ui.pinrecovery.PinRecoveryActivity
import com.stocksexchange.android.ui.pinrecovery.newInstance
import com.stocksexchange.android.ui.views.PinEntryKeypad
import com.stocksexchange.android.ui.views.dialogs.FingerprintDialog
import com.stocksexchange.core.utils.Timer
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.extensions.makeGone
import kotlinx.android.synthetic.main.authentication_activity_layout.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class AuthenticationActivity : BaseActivity<AuthenticationPresenter>(), AuthenticationContract.View {


    companion object {

        private const val INTENTIONAL_DELAY_DURATION = 300L

    }


    override val mPresenter: AuthenticationPresenter by inject { parametersOf(this) }

    private var mTimer: Timer? = null

    private lateinit var mHandler: Handler

    private var mDestinationIntent: Intent? = null

    private var mFingerprintDialog: FingerprintDialog? = null




    override fun preInit() {
        super.preInit()

        mHandler = Handler(Looper.getMainLooper())
    }


    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.pinCodeMode = it.pinCodeMode
            mPresenter.transitionAnimations = it.transitionAnimations
            mPresenter.theme = it.theme

            mDestinationIntent = it.destinationIntent
        }
    }


    override fun init() {
        super.init()

        initContentContainer()
        initAppLogo()
        initMessage()
        initPinBoxContainer()
        initPinEntryKeypad()
        initHelpButton()
    }


    private fun initContentContainer() {
        ThemingUtil.Authentication.contentContainer(contentContainerRl, mPresenter.theme)
    }


    private fun initAppLogo() {
        ThemingUtil.Authentication.appLogo(appLogoIv, mPresenter.theme)
    }


    private fun initMessage() {
        setMessageText(mStringProvider.getMessageForPinCodeMode(mPresenter.pinCodeMode))

        ThemingUtil.Authentication.message(messageTv, mPresenter.theme)
    }


    private fun initPinBoxContainer() {
        updatePinBoxContainer(if(mPresenter.pinCodeMode != PinCodeMode.CONFIRMATION) {
            mPresenter.pinCode
        } else {
            mPresenter.confirmedPinCode
        })
    }


    private fun initPinEntryKeypad() {
        with(pinEntryKeypad) {
            onButtonClickListener = mOnButtonClickListener

            if(mPresenter.pinCodeMode != PinCodeMode.ENTER) {
                setBottomMargin(dimenInPx(
                    R.dimen.authentication_activity_pin_entry_keypad_margin_bottom
                ))
            }

            ThemingUtil.Authentication.pinEntryKeypad(this, mPresenter.theme)
        }
    }


    private fun initHelpButton() {
        with(helpBtnTv) {
            val originalText = getStr(R.string.authentication_activity_help_button_text)

            text = originalText.toSpannable().apply {
                this[0, length] = UnderlineSpan()
            }

            if(mPresenter.pinCodeMode == PinCodeMode.ENTER) {
                makeVisible()
            } else {
                makeGone()
            }

            setOnClickListener {
                mPresenter.onHelpButtonClicked()
            }

            ThemingUtil.Authentication.helpButton(helpBtnTv, mPresenter.theme)
        }
    }


    override fun showKeypadFingerprintScannerButton() {
        pinEntryKeypad.showFingerprintButton()
    }


    override fun hideKeypadFingerprintScannerButton() {
        pinEntryKeypad.hideFingerprintButton()
    }


    override fun showKeypadFingerprintOverlayView() {
        pinEntryKeypad.showFingerprintOverlayView()
    }


    override fun hideKeypadFingerprintOverlayView() {
        pinEntryKeypad.hideFingerprintOverlayView()
    }


    override fun enableKeypadDigitButtons() {
        pinEntryKeypad.enableDigitButtons()
    }


    override fun disableKeypadDigitButtons() {
        pinEntryKeypad.disableDigitButtons()
    }


    override fun enableKeypadFingerprintScannerButton() {
        pinEntryKeypad.enableFingerprintButton()
    }


    override fun disableKeypadFingerprintScannerButton() {
        pinEntryKeypad.disableFingerprintButton()
    }


    override fun enableKeypadDeleteButton() {
        pinEntryKeypad.enableDeleteButton()
    }


    override fun disableKeypadDeleteButton() {
        pinEntryKeypad.disableDeleteButton()
    }


    override fun showFingerprintRegistrationDialog() {
        val listener = object : FingerprintDialog.Listener {

            override fun onSuccess() {
                mPresenter.onFingerprintRegistrationSucceeded()
            }

            override fun onError(error: FingerprintDialog.Error) {
                mPresenter.onFingerprintRegistrationFailed()
            }

            override fun onDismiss() {
                mPresenter.onFingerprintRegistrationFailed()
            }

        }

        val dialog = FingerprintDialog(
            this,
            FingerprintDialog.Mode.SETUP,
            listener
        ).apply {
            ThemingUtil.Authentication.fingerprintDialog(this, getAppTheme())

            setSubtitleText(getStr(R.string.fingerprint_dialog_introduction_message))
            setButtonText(getStr(R.string.action_cancel))
            setButtonListener {
                mPresenter.onFingerprintRegistrationDialogButtonClicked()
            }
            showSubtitle()
            startAuthenticationProcess()
        }

        mFingerprintDialog = dialog
        mFingerprintDialog?.show()
    }


    override fun showFingerprintAuthDialog() {
        val listener = object : FingerprintDialog.Listener {

            override fun onSuccess() {
                mPresenter.onFingerprintRecognized()
            }

            override fun onError(error: FingerprintDialog.Error) {
                when(error) {
                    FingerprintDialog.Error.TOO_MANY_ATTEMPTS -> {
                        mPresenter.onFingerprintAttemptsWasted()
                    }
                }
            }

        }

        val dialog = FingerprintDialog(
            context = this,
            mode = FingerprintDialog.Mode.SCAN,
            listener = listener
        ).apply {
            ThemingUtil.Authentication.fingerprintDialog(this, mPresenter.theme)

            setButtonText(getStr(R.string.action_use_pin))
            setButtonListener {
                mPresenter.onFingerprintAuthDialogButtonClicked()
            }
            startAuthenticationProcess()
        }

        mFingerprintDialog = dialog
        mFingerprintDialog?.show()
    }


    override fun hideFingerprintDialog() {
        mFingerprintDialog?.dismiss()
        mFingerprintDialog = null
    }


    override fun startTimer(millisInFuture: Long, tickInterval: Long,
                            minFinishTime: Long) {
        mTimer = object : Timer(millisInFuture, tickInterval) {

            override fun onTick(millisUntilFinished: Long) {
                mPresenter.onTimerTick(millisUntilFinished)
            }

            override fun onCancelled() {
                mPresenter.onTimerCancelled()
            }

            override fun onFinished() {
                mPresenter.onTimerFinished()
            }

        }
        mTimer?.setMinimumFinishTime(minFinishTime)
        mTimer?.start()
    }


    override fun cancelTimer() {
        mTimer?.cancel()
    }


    override fun updatePinBoxContainer(pinCode: String) {
        val pinCodeLength = pinCode.length

        updatePinBox(firstPinBox, pinCodeLength, 0)
        updatePinBox(secondPinBox, pinCodeLength, 1)
        updatePinBox(thirdPinBox, pinCodeLength, 2)
        updatePinBox(fourthPinBox, pinCodeLength, 3)
    }


    private fun updatePinBox(pinBoxIv: ImageView, pinCodeLength: Int, threshold: Int) {
        if(pinCodeLength > threshold) {
            ThemingUtil.Authentication.filledPinBox(pinBoxIv, mPresenter.theme)
        } else {
            ThemingUtil.Authentication.emptyPinBox(pinBoxIv, mPresenter.theme)
        }
    }


    override fun clearPinBoxContainer() {
        updatePinBoxContainer("")
    }


    override fun launchDestinationActivityIfNecessary() {
        if(mDestinationIntent != null) {
            startActivity(mDestinationIntent)
        }
    }


    override fun launchPinRecoveryActivity() {
        startActivity(PinRecoveryActivity.newInstance(this))
    }


    override fun startIntentionalDelay(pinCodeMode: PinCodeMode, pinCode: String) {
        val runnable = Runnable {
            mPresenter.onIntentionalDelayFinished(pinCodeMode, pinCode)
        }

        mHandler.postDelayed(runnable, INTENTIONAL_DELAY_DURATION)
    }


    override fun setActivityResult(result: Boolean) {
        setResult(
            if(result) Activity.RESULT_OK else Activity.RESULT_CANCELED,
            Intent()
        )
    }


    override fun setMessageText(message: String, animate: Boolean,
                                onFinish: (() -> Unit)?) {
        if(animate) {
            messageTv.crossFadeText(text = message, onFinish = onFinish)
        } else {
            messageTv.text = message
            onFinish?.invoke()
        }
    }


    override fun shouldShowIntercomInAppMessagePopups(): Boolean = false


    override fun getContentLayoutResourceId(): Int = R.layout.authentication_activity_layout


    override fun getColorForSystemWindow(type: SystemWindowType): Int {
        return when(type) {
            SystemWindowType.STATUS_BAR,
            SystemWindowType.NAVIGATION_BAR -> mPresenter.theme.generalTheme.primaryDarkColor
            SystemWindowType.RECENT_APPS_TOOLBAR -> mPresenter.theme.generalTheme.primaryColor
        }
    }


    fun getPinCodeMode(): PinCodeMode = mPresenter.pinCodeMode


    override fun getTransitionAnimations(): TransitionAnimations {
        return mPresenter.transitionAnimations
    }


    private val mOnButtonClickListener: PinEntryKeypad.OnButtonClickListener =
        object : PinEntryKeypad.OnButtonClickListener {

        override fun onDigitButtonClicked(digit: String) {
            mPresenter.onKeypadDigitButtonClicked(digit)
        }

        override fun onFingerprintButtonClicked() {
            mPresenter.onKeypadFingerprintButtonClicked()
        }

        override fun onFingerprintOverlayClicked() {
            mPresenter.onKeypadFingerprintOverlayClicked()
        }

        override fun onDeleteButtonClicked() {
            mPresenter.onKeypadDeleteButtonClicked()
        }

    }


    override fun onRestoreState(savedState: Bundle) {
        super.onRestoreState(savedState)

        savedState.extract(activityStateExtractor).also {
            mDestinationIntent = it.destinationIntent
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.saveState(ActivityState(
            destinationIntent = mDestinationIntent
        ))
    }


}