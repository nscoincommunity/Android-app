package com.stocksexchange.android.ui.passwordrecovery

import com.stocksexchange.android.R
import com.stocksexchange.android.model.*
import com.stocksexchange.api.model.rest.parameters.PasswordRecoveryParameters
import com.stocksexchange.api.model.rest.PasswordResetEmailSendingResponse
import com.stocksexchange.api.model.rest.PasswordResetResponse
import com.stocksexchange.api.model.rest.UserAdmissionError
import com.stocksexchange.android.ui.base.useradmission.BaseUserAdmissionPresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.api.exceptions.rest.PasswordRecoveryException

class PasswordRecoveryPresenter(
    view: PasswordRecoveryContract.View,
    model: PasswordRecoveryModel
) : BaseUserAdmissionPresenter<
    PasswordRecoveryContract.View,
    PasswordRecoveryModel,
    PasswordRecoveryParameters,
    PasswordRecoveryProcessPhase,
    PasswordRecoveryInputView
>(view, model), PasswordRecoveryContract.ActionListener {


    override val initialProcessPhase: PasswordRecoveryProcessPhase
        get() = PasswordRecoveryProcessPhase.AWAITING_EMAIL_ADDRESS

    var passwordResetToken: String = ""

    lateinit var transitionAnimations: TransitionAnimations




    override fun getPrimaryButtonText(processPhase: PasswordRecoveryProcessPhase): String {
        return mStringProvider.getPrimaryButtonTextForPasswordRecoveryProcessPhase(processPhase)
    }


    override fun getDefaultParameters(): PasswordRecoveryParameters = PasswordRecoveryParameters.getDefaultParameters()


    override fun getUserAdmissionError(errorMessage: String): UserAdmissionError {
        return when(processPhase) {
            PasswordRecoveryProcessPhase.AWAITING_NEW_PASSWORD -> {
                UserAdmissionError.newPasswordRecoveryError(errorMessage)
            }

            else -> super.getUserAdmissionError(errorMessage)
        }
    }


    private fun getButtonTypeForPasswordResetEmailRequestType(): UserAdmissionButtonType {
        return when(processPhase) {
            PasswordRecoveryProcessPhase.AWAITING_EMAIL_ADDRESS -> UserAdmissionButtonType.PRIMARY
            PasswordRecoveryProcessPhase.PASSWORD_RESET_EMAIL_SENT -> UserAdmissionButtonType.SECONDARY

            else -> UserAdmissionButtonType.PRIMARY
        }
    }


    override fun onErrorReceived(exception: Throwable) {
        when(exception) {
            is PasswordRecoveryException -> when(exception.error) {
                PasswordRecoveryException.Error.MULTIPLE -> {
                    onMultipleErrorsReceived(exception.errorMessages)
                }

                PasswordRecoveryException.Error.USER_NOT_FOUND -> {
                    onUserNotFoundErrorReceived()
                }

                PasswordRecoveryException.Error.UNKNOWN -> {
                    onUnknownErrorReceived(exception.message)
                }
            }

            else -> super.onErrorReceived(exception)
        }
    }


    override fun onSecondaryButtonClicked() {
        if(processPhase != PasswordRecoveryProcessPhase.PASSWORD_RESET_EMAIL_SENT) {
            return
        }

        mModel.performPasswordResetEmailSendingRequest(mParameters.email)
    }


    override fun onPrimaryButtonClicked() {
        when(processPhase) {
            PasswordRecoveryProcessPhase.AWAITING_EMAIL_ADDRESS -> onAwaitingEmailAddressPrimaryBtnClicked()
            PasswordRecoveryProcessPhase.PASSWORD_RESET_EMAIL_SENT -> onPasswordResetEmailSentBtnClicked()
            PasswordRecoveryProcessPhase.AWAITING_NEW_PASSWORD -> onAwaitingNewPasswordPrimaryBtnClicked()
            PasswordRecoveryProcessPhase.PASSWORD_RESET -> onPasswordResetPrimaryBtnClicked()
        }
    }


    private fun onAwaitingEmailAddressPrimaryBtnClicked() {
        val errorsList = mutableListOf<String>()
        val inputViews = mutableListOf<PasswordRecoveryInputView>()
        val errorsHandler: ((List<String>) -> Unit) = { errorsList.addAll(it) }

        val email = mView.getCredentialViewEmail()

        validateEmailAddress(email) {
            errorsHandler(it)
            inputViews.add(PasswordRecoveryInputView.EMAIL)
        }

        if(!validateForm(errorsList, inputViews)) {
            return
        }

        mParameters = mParameters.copy(
            email = email
        )

        mModel.performPasswordResetEmailSendingRequest(mParameters.email)
    }


    private fun onPasswordResetEmailSentBtnClicked() {
        launchLoginAndFinish()
    }


    private fun onAwaitingNewPasswordPrimaryBtnClicked() {
        val errorsList = mutableListOf<String>()
        val inputViews = mutableListOf<PasswordRecoveryInputView>()
        val errorsHandler: ((List<String>) -> Unit) = { errorsList.addAll(it) }

        val email = mView.getCredentialsViewEmail()
        val password = mView.getCredentialsViewPassword()
        val passwordConfirmation = mView.getCredentialsViewPasswordConfirmation()

        validateEmailAddress(email) {
            errorsHandler(it)
            inputViews.add(PasswordRecoveryInputView.EMAIL)
        }
        validatePassword(password) {
            errorsHandler(it)

            with(inputViews) {
                add(PasswordRecoveryInputView.NEW_PASSWORD)
                add(PasswordRecoveryInputView.PASSWORD_CONFIRMATION)
            }
        }

        // Checking if blank
        if(passwordConfirmation.isBlank()) {
            errorsList.add(mStringProvider.getString(R.string.error_empty_password_confirmation))
            inputViews.add(PasswordRecoveryInputView.PASSWORD_CONFIRMATION)
        }

        // Checking for inequality
        if(password != passwordConfirmation) {
            errorsList.add(mStringProvider.getString(R.string.error_passwords_do_not_match))
            inputViews.add(PasswordRecoveryInputView.PASSWORD_CONFIRMATION)
        }

        if(!validateForm(errorsList, inputViews)) {
            return
        }

        mParameters = mParameters.copy(
            passwordResetToken = passwordResetToken,
            email = email,
            newPassword = password
        )

        mModel.performPasswordResetRequest(mParameters)
    }


    private fun onPasswordResetPrimaryBtnClicked() {
        launchLoginAndFinish()
    }


    override fun onBeforeShowingNewMainView() {
        if(processPhase != PasswordRecoveryProcessPhase.PASSWORD_RESET_EMAIL_SENT) {
            return
        }

        mView.showSecondaryButton()
    }


    override fun onNewMainViewShowingFinished() {

    }


    override fun onRequestSent(requestType: Int) {
        when(requestType) {

            PasswordRecoveryModel.REQUEST_TYPE_SEND_PASSWORD_RESET_EMAIL -> {
                mView.showButtonProgressBar(getButtonTypeForPasswordResetEmailRequestType())
            }

            PasswordRecoveryModel.REQUEST_TYPE_RESET_PASSWORD -> {
                mView.showButtonProgressBar(UserAdmissionButtonType.PRIMARY)
            }

        }
    }


    override fun onResponseReceived(requestType: Int) {
        when(requestType) {

            PasswordRecoveryModel.REQUEST_TYPE_SEND_PASSWORD_RESET_EMAIL -> {
                mView.hideButtonProgressBar(getButtonTypeForPasswordResetEmailRequestType())
            }

            PasswordRecoveryModel.REQUEST_TYPE_RESET_PASSWORD -> {
                mView.hideButtonProgressBar(UserAdmissionButtonType.PRIMARY)
            }

        }
    }


    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        when(requestType) {

            PasswordRecoveryModel.REQUEST_TYPE_SEND_PASSWORD_RESET_EMAIL -> {
                onPasswordResetEmailSendingRequestSucceeded(response as PasswordResetEmailSendingResponse)
            }

            PasswordRecoveryModel.REQUEST_TYPE_RESET_PASSWORD -> {
                onPasswordResetRequestSucceeded(response as PasswordResetResponse)
            }

        }
    }


    private fun onPasswordResetEmailSendingRequestSucceeded(response: PasswordResetEmailSendingResponse) {
        val oldProcessPhase = processPhase
        val newProcessPhase = PasswordRecoveryProcessPhase.PASSWORD_RESET_EMAIL_SENT

        if(oldProcessPhase != newProcessPhase) {
            onProcessPhaseChanged(newProcessPhase)
        } else {
            showAccountVerificationEmailResentDialog()
        }
    }


    private fun showAccountVerificationEmailResentDialog() {
        mView.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            title = mStringProvider.getString(R.string.password_recovery_reset_link_dialog_title),
            content = mStringProvider.getString(R.string.password_recovery_reset_link_resent_dialog_content),
            positiveBtnText = mStringProvider.getString(R.string.ok),
            positiveBtnClick = {
                onPasswordResetDialogPositiveBtnClicked()
            }
        ))
    }


    private fun onPasswordResetDialogPositiveBtnClicked() {
        launchLoginAndFinish()
    }


    private fun launchLoginAndFinish() {
        mView.launchLoginActivity()
        mView.finishActivity()
    }


    private fun onPasswordResetRequestSucceeded(response: PasswordResetResponse) {
        onProcessPhaseChanged(PasswordRecoveryProcessPhase.PASSWORD_RESET)
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            passwordResetToken = it.passwordResetToken
            transitionAnimations = it.transitionAnimations
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            passwordResetToken = passwordResetToken,
            transitionAnimations = transitionAnimations
        ))
    }


}