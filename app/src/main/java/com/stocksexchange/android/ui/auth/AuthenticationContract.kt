package com.stocksexchange.android.ui.auth

import com.stocksexchange.android.model.PinCodeMode
import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface AuthenticationContract {


    interface View : BaseView {

        fun showKeypadFingerprintScannerButton()

        fun hideKeypadFingerprintScannerButton()

        fun showKeypadFingerprintOverlayView()

        fun hideKeypadFingerprintOverlayView()

        fun enableKeypadDigitButtons()

        fun disableKeypadDigitButtons()

        fun enableKeypadFingerprintScannerButton()

        fun disableKeypadFingerprintScannerButton()

        fun enableKeypadDeleteButton()

        fun disableKeypadDeleteButton()

        fun showFingerprintRegistrationDialog()

        fun showFingerprintAuthDialog()

        fun hideFingerprintDialog()

        fun startTimer(millisInFuture: Long, tickInterval: Long, minFinishTime: Long)

        fun cancelTimer()

        fun updatePinBoxContainer(pinCode: String)

        fun clearPinBoxContainer()

        fun launchDestinationActivityIfNecessary()

        fun launchPinRecoveryActivity()

        fun finishActivity()

        fun startIntentionalDelay(pinCodeMode: PinCodeMode, pinCode: String)

        fun setActivityResult(result: Boolean)

        fun setMessageText(message: String, animate: Boolean = false,
                           onFinish: (() -> Unit)? = null)

    }


    interface ActionListener {

        fun onFingerprintRegistrationDialogButtonClicked()

        fun onFingerprintRegistrationSucceeded()

        fun onFingerprintRegistrationFailed()

        fun onFingerprintAuthDialogButtonClicked()

        fun onFingerprintRecognized()

        fun onFingerprintAttemptsWasted()

        fun onUserAuthenticated()

        fun onKeypadDigitButtonClicked(digit: String)

        fun onKeypadFingerprintButtonClicked()

        fun onKeypadFingerprintOverlayClicked()

        fun onKeypadDeleteButtonClicked()

        fun onHelpButtonClicked()

        fun onTimerTick(millisecondsTillFinished: Long)

        fun onTimerCancelled()

        fun onTimerFinished()

        fun onIntentionalDelayFinished(pinCodeMode: PinCodeMode, pinCode: String)

    }


}