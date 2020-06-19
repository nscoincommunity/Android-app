package com.stocksexchange.android.ui.registration

import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.android.model.*
import com.stocksexchange.api.model.rest.AccountVerificationEmailSendingResponse
import com.stocksexchange.api.model.rest.parameters.SignUpParameters
import com.stocksexchange.api.model.rest.SignUpResponse
import com.stocksexchange.api.model.rest.UserAdmissionError
import com.stocksexchange.android.ui.base.useradmission.BaseUserAdmissionPresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.api.exceptions.rest.AccountVerificationException
import com.stocksexchange.api.exceptions.rest.RegistrationException

class RegistrationPresenter(
    view: RegistrationContract.View,
    model: RegistrationModel
) : BaseUserAdmissionPresenter<
    RegistrationContract.View,
    RegistrationModel,
    SignUpParameters,
    RegistrationProcessPhase,
    RegistrationInputView
>(view, model), RegistrationContract.ActionListener {


    override val initialProcessPhase: RegistrationProcessPhase
        get() = RegistrationProcessPhase.AWAITING_CREDENTIALS

    lateinit var transitionAnimations: TransitionAnimations




    override fun getPrimaryButtonText(processPhase: RegistrationProcessPhase): String {
        return mStringProvider.getPrimaryButtonTextForRegistrationProcessPhase(processPhase)
    }


    override fun getDefaultParameters(): SignUpParameters = SignUpParameters.getDefaultParameters()


    override fun getUserAdmissionError(errorMessage: String): UserAdmissionError {
        return UserAdmissionError.newRegistrationError(errorMessage)
    }


    override fun onReferralCodeIconClicked() {
        showReferralCodeHelpDialog()
    }


    private fun showReferralCodeHelpDialog() {
        val title = mStringProvider.getString(
            R.string.registration_referral_code_help_dialog_title
        )
        val content = mStringProvider.getString(
            R.string.registration_referral_code_help_dialog_message
        )

        showInfoDialog(
            title = title,
            content = content
        )
    }


    override fun onTermsOfUseLinkClicked() {
        mView.launchBrowser(Constants.STEX_TERMS_OF_USE_URL)
    }


    override fun onSecondaryButtonClicked() {
        mModel.performAccountVerificationEmailSendingRequest(mParameters.email)
    }


    override fun onPrimaryButtonClicked() {
        when(processPhase) {
            RegistrationProcessPhase.AWAITING_CREDENTIALS -> onAwaitingCredentialsPrimaryBtnClicked()
            RegistrationProcessPhase.AWAITING_ACCOUNT_VERIFICATION -> onAwaitingAccVerificationPrimaryBtnClicked()
        }
    }


    private fun onAwaitingCredentialsPrimaryBtnClicked() {
        val errorsList = mutableListOf<String>()
        val inputViews = mutableListOf<RegistrationInputView>()
        val errorsHandler: ((List<String>) -> Unit) = { errorsList.addAll(it) }

        val email = mView.getEmail()
        val password = mView.getPassword()
        val referralCode = mView.getReferralCode()
        val isTermsOfUseCheckBoxChecked = mView.isTermsOfUseCheckBoxChecked()
        val isAgeCheckBoxChecked = mView.isAgeCheckBoxChecked()

        validateEmailAddress(email) {
            errorsHandler(it)
            inputViews.add(RegistrationInputView.EMAIL)
        }
        validatePassword(password) {
            errorsHandler(it)
            inputViews.add(RegistrationInputView.PASSWORD)
        }

        // If the referral code has not been provided, then we consider it as
        // valid situation since it is an optional field
        if(referralCode.isNotBlank()) {
            val isLengthWrong = (referralCode.length != Constants.REFERRAL_CODE_LENGTH)
            val containsInvalidCharacters = (referralCode.toLongOrNull() == null)

            if(isLengthWrong || containsInvalidCharacters) {
                errorsList.add(mStringProvider.getString(R.string.error_invalid_referral_code))
                inputViews.add(RegistrationInputView.REFERRAL_CODE)
            }
        }

        if(!isTermsOfUseCheckBoxChecked) {
            errorsList.add(mStringProvider.getString(R.string.error_terms_of_use_not_accepted))
        }

        if(!isAgeCheckBoxChecked) {
            errorsList.add(mStringProvider.getString(R.string.error_age_not_confirmed))
        }

        if(!validateForm(errorsList, inputViews)) {
            return
        }

        mParameters = mParameters.copy(
            email = email,
            password = password,
            referralCode = referralCode
        )

        mModel.performSignUpRequest(mParameters)
    }


    private fun onAwaitingAccVerificationPrimaryBtnClicked() {
        mView.launchLoginActivity()
        mView.finishActivity()
    }


    override fun onBeforeShowingNewMainView() {
        if(processPhase != RegistrationProcessPhase.AWAITING_ACCOUNT_VERIFICATION) {
            return
        }

        mView.showSecondaryButton()
    }


    override fun onNewMainViewShowingFinished() {

    }


    override fun onRequestSent(requestType: Int) {
        when(requestType) {

            RegistrationModel.REQUEST_TYPE_SIGN_UP -> {
                mView.showButtonProgressBar(UserAdmissionButtonType.PRIMARY)
            }

            RegistrationModel.REQUEST_TYPE_ACCOUNT_VERIFICATION_EMAIL_SENDING -> {
                mView.showButtonProgressBar(UserAdmissionButtonType.SECONDARY)
            }

        }
    }


    override fun onResponseReceived(requestType: Int) {
        when(requestType) {

            RegistrationModel.REQUEST_TYPE_SIGN_UP -> {
                mView.hideButtonProgressBar(UserAdmissionButtonType.PRIMARY)
            }

            RegistrationModel.REQUEST_TYPE_ACCOUNT_VERIFICATION_EMAIL_SENDING -> {
                mView.hideButtonProgressBar(UserAdmissionButtonType.SECONDARY)
            }

        }
    }


    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        when(requestType) {

            RegistrationModel.REQUEST_TYPE_SIGN_UP -> {
                onSignUpRequestSucceeded(response as SignUpResponse)
            }

            RegistrationModel.REQUEST_TYPE_ACCOUNT_VERIFICATION_EMAIL_SENDING -> {
                onResendVerificationEmailRequestSucceeded(response as AccountVerificationEmailSendingResponse)
            }

        }
    }


    private fun onSignUpRequestSucceeded(response: SignUpResponse) {
        onProcessPhaseChanged(RegistrationProcessPhase.AWAITING_ACCOUNT_VERIFICATION)
    }


    private fun onResendVerificationEmailRequestSucceeded(response: AccountVerificationEmailSendingResponse) {
        showVerificationEmailResentDialog()
    }


    private fun showVerificationEmailResentDialog() {
        mView.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            title = mStringProvider.getString(R.string.user_admission_account_verification_email_resent_dialog_title),
            content = mStringProvider.getString(R.string.user_admission_account_verification_email_resent_dialog_message),
            positiveBtnText = mStringProvider.getString(R.string.ok),
            positiveBtnClick = {
                onVerificationEmailResentDialogPositiveBtnClicked()
            }
        ))
    }


    private fun onVerificationEmailResentDialogPositiveBtnClicked() {
        launchLoginAndFinish()
    }


    private fun launchLoginAndFinish() {
        mView.launchLoginActivity()
        mView.finishActivity()
    }


    override fun onErrorReceived(exception: Throwable) {
        when(exception) {
            is RegistrationException -> when(exception.error) {
                RegistrationException.Error.MULTIPLE -> {
                    onMultipleErrorsReceived(exception.errorMessages)
                }

                RegistrationException.Error.UNKNOWN -> {
                    onUnknownErrorReceived(exception.message)
                }
            }

            is AccountVerificationException -> when(exception.error) {
                AccountVerificationException.Error.USER_NOT_FOUND -> {
                    onUserNotFoundErrorReceived()
                }

                AccountVerificationException.Error.UNKNOWN -> {
                    onUnknownErrorReceived(exception.message)
                }
            }

            else -> super.onErrorReceived(exception)
        }
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            transitionAnimations = it.transitionAnimations
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            transitionAnimations = transitionAnimations
        ))
    }


}