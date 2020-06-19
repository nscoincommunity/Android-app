package com.stocksexchange.android.ui.login

import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.model.LoginInputView
import com.stocksexchange.android.model.LoginProcessPhase
import com.stocksexchange.android.model.MaterialDialogBuilder
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.notification.FirebasePushClient
import com.stocksexchange.android.ui.base.useradmission.BaseUserAdmissionPresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.api.exceptions.rest.AccountVerificationException
import com.stocksexchange.api.exceptions.rest.LoginException
import com.stocksexchange.api.model.rest.parameters.SignInParameters
import com.stocksexchange.api.utils.CredentialsHandler
import com.stocksexchange.core.handlers.PreferenceHandler
import com.stocksexchange.core.utils.helpers.generateRandomString

class LoginPresenter(
    view: LoginContract.View,
    model: LoginModel,
    private val preferenceHandler: PreferenceHandler,
    private val credentialsHandler: CredentialsHandler,
    private val sessionManager: SessionManager,
    private val firebasePushClient: FirebasePushClient
) : BaseUserAdmissionPresenter<
    LoginContract.View,
    LoginModel,
    SignInParameters,
    LoginProcessPhase,
    LoginInputView
>(view, model), LoginContract.ActionListener {


    override val initialProcessPhase: LoginProcessPhase
        get() = LoginProcessPhase.AWAITING_CREDENTIALS

    var wasAccountJustVerified: Boolean = false

    var confirmationType = SignInConfirmationType.UNKNOWN

    lateinit var transitionAnimations: TransitionAnimations

    private var mIsAccountVerifiedDialogAlreadyShown: Boolean = false
    private var mIsLoginSessionExpired: Boolean = false




    override fun start() {
        super.start()

        showAccountVerifiedDialogIfNeeded()
    }


    private fun showAccountVerifiedDialogIfNeeded() {
        if(!wasAccountJustVerified || mIsAccountVerifiedDialogAlreadyShown) {
            return
        }

        showAccountVerifiedDialog()
    }


    private fun showAccountVerifiedDialog() {
        mIsAccountVerifiedDialogAlreadyShown = true

        showInfoDialog(
            title = mStringProvider.getString(R.string.login_account_verified_dialog_title),
            content = mStringProvider.getString(R.string.login_account_verified_dialog_context)
        )
    }


    private fun showLoginSessionExpiredDialog() {
        mIsLoginSessionExpired = false

        // Additional checks to prevent a weird bug
        if(mView.hasWindowFocus() && !mView.isFinishing()) {
            showInfoDialog(
                title = mStringProvider.getString(R.string.login_session_expired_dialog_title),
                content = mStringProvider.getString(R.string.login_session_expired_dialog_content)
            )
        }
    }


    private fun showErrorDialog() {
        val message: String = mStringProvider.getString(when(processPhase) {
            LoginProcessPhase.AWAITING_CREDENTIALS -> R.string.error_invalid_credentials
            LoginProcessPhase.AWAITING_CONFIRMATION -> R.string.error_invalid_code

            else -> throw IllegalStateException()
        })

        super.showErrorDialog(message)
    }


    override fun getPrimaryButtonText(processPhase: LoginProcessPhase): String {
        return mStringProvider.getPrimaryButtonTextForLoginProcessPhase(processPhase)
    }


    override fun getDefaultParameters(): SignInParameters = SignInParameters.getDefaultParameters()


    override fun getUserAdmissionError(errorMessage: String): UserAdmissionError {
        return UserAdmissionError.newLoginError(errorMessage)
    }


    private fun onAccountEmailVerificationReceived(verification: AccountEmailVerification) {
        onProcessPhaseChanged(LoginProcessPhase.AWAITING_ACCOUNT_VERIFICATION)
    }


    private fun onLoginConfirmationReceived(confirmation: SignInConfirmation) {
        confirmationType = confirmation.confirmationType

        onProcessPhaseChanged(LoginProcessPhase.AWAITING_CONFIRMATION)
    }


    private fun onOAuthCredentialsReceived(oauthCredentials: OAuthCredentials) {
        with(credentialsHandler) {
            saveEmail(mParameters.email)
            saveOAuthCredentials(oauthCredentials)
        }

        mView.recreateUserLogoutAlarm(oauthCredentials.refreshTokenExpirationTimeInMillis)
        mModel.performProfileInfoFetchingRequest(mParameters.email)
    }


    private fun onLoginSessionExpired() {
        onProcessPhaseChanged(LoginProcessPhase.AWAITING_CREDENTIALS)

        mIsLoginSessionExpired = true
    }


    override fun onErrorReceived(exception: Throwable) {
        when(exception) {
            is LoginException -> when(exception.error) {
                LoginException.Error.MULTIPLE -> {
                    onMultipleErrorsReceived(exception.errorMessages)
                }

                LoginException.Error.INVALID_PARAMETERS -> {
                    showErrorDialog()
                }

                LoginException.Error.SESSION_EXPIRED -> {
                    onLoginSessionExpired()
                }

                LoginException.Error.CONFIRMATION_OBJECT_BAD_JSON,
                LoginException.Error.VERIFICATION_OBJECT_BAD_JSON,
                LoginException.Error.UNKNOWN -> {
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


    private fun onSecurityCodeReceived(code: String) {
        mParameters = mParameters.copy(code = code)

        mModel.performSignInConfirmationRequest(mParameters)
    }


    override fun onCredentialsViewHelpButtonClicked() {
        mView.launchPasswordRecoveryActivity()
    }


    override fun onCredentialsViewRegisterButtonClicked() {
        mView.launchRegistrationActivity()
    }


    override fun onConfirmationViewHelpButtonClicked(type: SignInConfirmationType) {
        when(type) {
            SignInConfirmationType.TWO_FACTOR_AUTHENTICATION -> showTwoFactorAuthHelpDialog()
            SignInConfirmationType.SMS -> showSmsAuthHelpDialog()
        }
    }


    private fun showTwoFactorAuthHelpDialog() {
        mView.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            title = mStringProvider.getString(R.string.login_2fa_help_dialog_title),
            content = mStringProvider.getString(R.string.login_2fa_help_dialog_message),
            negativeBtnText = mStringProvider.getString(R.string.action_cancel),
            positiveBtnText = mStringProvider.getString(R.string.login_2fa_help_dialog_positive_button_text),
            positiveBtnClick = {
                onContactSupportButtonClicked()
            }
        ))
    }


    private fun onContactSupportButtonClicked() {
        mView.sendEmail(
            emailAddress = Constants.SUPPORT_EMAIL_ADDRESS,
            subject = mStringProvider.getString(R.string.login_disable_2fa_email_subject),
            text = mStringProvider.getString(
                R.string.login_disable_2fa_email_text,
                mParameters.email
            )
        )
    }


    private fun showSmsAuthHelpDialog() {
        showInfoDialog(
            title = mStringProvider.getString(R.string.login_sms_help_dialog_title),
            content = mStringProvider.getString(R.string.login_sms_help_dialog_message)
        )
    }


    override fun onSecurityCodeReceived(type: SignInConfirmationType, code: String) {
        if(type != confirmationType) {
            mView.showToast(mStringProvider.getSomethingWentWrongMessage())
            return
        }

        onSecurityCodeReceived(code)
    }


    override fun onAccountVerified() {
        if(processPhase != LoginProcessPhase.AWAITING_CREDENTIALS) {
            onProcessPhaseChanged(LoginProcessPhase.AWAITING_CREDENTIALS, false)
        }

        showAccountVerifiedDialog()
    }


    override fun onPrimaryButtonClicked() {
        when(processPhase) {
            LoginProcessPhase.AWAITING_CREDENTIALS -> onAwaitingCredentialsPrimaryBtnClicked()
            LoginProcessPhase.AWAITING_ACCOUNT_VERIFICATION -> onAwaitingAccVerificationPrimaryBtnClicked()
            LoginProcessPhase.AWAITING_CONFIRMATION -> onAwaitingConfirmationPrimaryBtnClicked()
        }
    }


    private fun onAwaitingCredentialsPrimaryBtnClicked() {
        val errorsList = mutableListOf<String>()
        val inputViews = mutableListOf<LoginInputView>()
        val errorsHandler: ((List<String>) -> Unit) = { errorsList.addAll(it) }

        val email = mView.getEmail()
        val password = mView.getPassword()

        validateEmailAddress(email) {
            errorsHandler(it)
            inputViews.add(LoginInputView.EMAIL)
        }
        validatePassword(password) {
            errorsHandler(it)
            inputViews.add(LoginInputView.PASSWORD)
        }

        if(!validateForm(errorsList, inputViews)) {
            return
        }

        mParameters = mParameters.copy(
            email = email,
            password = password,
            key = generateRandomString(SignInParameters.KEY_STRING_LENGTH)
        )

        mModel.performSignInRequest(mParameters)
    }


    private fun onAwaitingAccVerificationPrimaryBtnClicked() {
        if(processPhase != LoginProcessPhase.AWAITING_ACCOUNT_VERIFICATION) {
            return
        }

        mModel.performAccountVerificationEmailSendingRequest(mParameters.email)
    }


    private fun onAwaitingConfirmationPrimaryBtnClicked() {
        val errorsList = mutableListOf<String>()
        val inputViews = mutableListOf<LoginInputView>()

        val code = mView.getCode()

        // Checking if blank
        if(code.isBlank()) {
            errorsList.add(mStringProvider.getString(R.string.error_empty_code))
            inputViews.add(LoginInputView.CONFIRMATION_CODE)
        }

        if(!validateForm(errorsList, inputViews)) {
            return
        }

        onSecurityCodeReceived(code)
    }


    override fun onNewMainViewShowingFinished() {
        if((processPhase != LoginProcessPhase.AWAITING_CREDENTIALS) ||
            !mIsLoginSessionExpired) {
            return
        }

        showLoginSessionExpiredDialog()
    }


    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        when(requestType) {

            LoginModel.REQUEST_TYPE_SIGN_IN -> {
                onSignInRequestSucceeded(response as SignInResponse)
            }

            LoginModel.REQUEST_TYPE_ACCOUNT_VERIFICATION_EMAIL_SENDING -> {
                onResendVerificationEmailRequestSucceeded(response as AccountVerificationEmailSendingResponse)
            }

            LoginModel.REQUEST_TYPE_SIGN_IN_CONFIRMATION -> {
                onSignInConfirmationRequestSucceeded(response as SignInConfirmationResponse)
            }

            LoginModel.REQUEST_TYPE_PROFILE_INFO -> {
                onProfileInfoRequestSucceeded(response as ProfileInfo)
            }

        }
    }


    private fun onSignInRequestSucceeded(response: SignInResponse) {
        if(!response.hasData) {
            mView.showToast(mStringProvider.getSomethingWentWrongMessage())
            return
        }

        if(response.hasAccountEmailVerification) {
            onAccountEmailVerificationReceived(response.accountEmailVerification!!)
        } else {
            onLoginConfirmationReceived(response.loginConfirmation!!)
        }
    }


    private fun onResendVerificationEmailRequestSucceeded(response: AccountVerificationEmailSendingResponse) {
        showAccountVerificationEmailResentDialog()
    }


    private fun onSignInConfirmationRequestSucceeded(response: SignInConfirmationResponse) {
        if(!response.hasData) {
            return
        }

        if(response.hasOauthCredentials) {
            onOAuthCredentialsReceived(response.oauthCredentials!!)
        } else {
            onLoginConfirmationReceived(response.confirmation!!)
        }
    }


    private fun onProfileInfoRequestSucceeded(profileInfo: ProfileInfo) {
        if(preferenceHandler.isIntercomUnidentifiableUserRegistered()) {
            preferenceHandler.saveIntercomUnidentifiableUserRegistered(false)
        }

        sessionManager.setProfileInfo(profileInfo)
        setNotificationStatus(profileInfo)
        updateNotificationToken()
        getInboxUnreadCount()

        mView.launchDestinationActivity()
        mView.finishActivity()
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            wasAccountJustVerified = it.wasAccountJustVerified
            mIsAccountVerifiedDialogAlreadyShown = it.isAccountVerifiedDialogAlreadyShown
            confirmationType = it.confirmationType
            transitionAnimations = it.transitionAnimations
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            wasAccountJustVerified = wasAccountJustVerified,
            isAccountVerifiedDialogAlreadyShown = mIsAccountVerifiedDialogAlreadyShown,
            confirmationType = confirmationType,
            transitionAnimations = transitionAnimations
        ))
    }


    private fun updateNotificationToken() {
        firebasePushClient.updateNotificationToken()
    }


    private fun setNotificationStatus(profileInfo: ProfileInfo?) {
        if (profileInfo == null) {
            return
        }

        val newSettings = sessionManager.getSettings().copy(
            isNotificationEnabled = profileInfo.getNotificationStatus()
        )
        mModel.updateSettings(newSettings) {
            sessionManager.setSettings(newSettings)
        }
    }


    private fun getInboxUnreadCount() {
        firebasePushClient.getInboxUnreadCount()
    }


    private fun showAccountVerificationEmailResentDialog() {
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
        onProcessPhaseChanged(LoginProcessPhase.AWAITING_CREDENTIALS)
    }


}