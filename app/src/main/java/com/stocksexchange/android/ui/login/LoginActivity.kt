package com.stocksexchange.android.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.SignInConfirmationType
import com.stocksexchange.android.model.*
import com.stocksexchange.android.receivers.UserLogoutReceiver
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.auth.AuthenticationActivity
import com.stocksexchange.android.ui.auth.newInstance
import com.stocksexchange.android.ui.base.useradmission.BaseUserAdmissionActivity
import com.stocksexchange.android.ui.login.views.confirmationviews.base.BaseLoginConfirmationView
import com.stocksexchange.android.ui.passwordrecovery.PasswordRecoveryActivity
import com.stocksexchange.android.ui.passwordrecovery.newFirstPhaseInstance
import com.stocksexchange.android.ui.registration.RegistrationActivity
import com.stocksexchange.android.ui.registration.newInstance
import com.stocksexchange.android.ui.views.UserAdmissionButton
import com.stocksexchange.android.utils.handlers.EmailHandler
import com.stocksexchange.core.managers.KeyboardManager
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.login_activity_layout.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class LoginActivity : BaseUserAdmissionActivity<
    LoginPresenter,
    LoginProcessPhase,
    LoginInputView
>(), LoginContract.View {


    companion object {

        private const val KEYBOARD_HIDING_DELAY = 150L

    }


    override val mPresenter: LoginPresenter by inject { parametersOf(this) }

    private val mKeyboardManager: KeyboardManager by inject()

    private lateinit var mDestinationIntent: Intent

    private val mHandler = Handler()




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.wasAccountJustVerified = it.wasAccountJustVerified
            mPresenter.transitionAnimations = it.transitionAnimations

            mDestinationIntent = it.destinationIntent
        }
    }


    override fun initMainViews() {
        super.initMainViews()

        initCredentialsView()
        initAccountVerificationView()
        initConfirmationViews()
    }


    private fun initCredentialsView() {
        with(credentialsView) {
            setOnForgotPasswordClickListener {
                mPresenter.onCredentialsViewHelpButtonClicked()
            }
            setOnDoNotHaveAccountClickListener {
                mPresenter.onCredentialsViewRegisterButtonClicked()
            }

            ThemingUtil.Login.credentialsView(this, getAppTheme())
        }
    }


    private fun initAccountVerificationView() {
        with(accountVerificationView) {
            clearPadding()
            setCaption(getStr(R.string.user_admission_account_verification_view_caption))

            ThemingUtil.Login.accountVerificationView(this, getAppTheme())
        }
    }


    private fun initConfirmationViews() {
        val onCodeEnterListenerImpl = object : BaseLoginConfirmationView.OnCodeEnterListener {

            override fun onCodeEntered(editText: EditText, type: SignInConfirmationType, code: String) {
                mKeyboardManager.hideKeyboard(editText)

                mHandler.postDelayed(
                    { mPresenter.onSecurityCodeReceived(type, code) },
                    KEYBOARD_HIDING_DELAY
                )
            }

        }

        val confirmationViews = listOf(
            emailConfirmationView,
            smsConfirmationView,
            twoFactorAuthConfirmationView
        )

        for(confirmationView in confirmationViews) {
            with(confirmationView) {
                onCodeEnterListener = onCodeEnterListenerImpl

                setOnHelpButtonClickListener { type, _ ->
                    mPresenter.onConfirmationViewHelpButtonClicked(type)
                }
            }
        }

        with(ThemingUtil.Login) {
            val theme = getAppTheme()

            confirmationView(emailConfirmationView, theme)
            confirmationView(smsConfirmationView, theme)
            confirmationView(twoFactorAuthConfirmationView, theme)
        }
    }


    override fun sendEmail(emailAddress: String, subject: String, text: String) {
        get<EmailHandler>().sendEmail(this, emailAddress, subject, text, Constants.REQUEST_CODE_NEW_EMAIL)
    }


    override fun launchPasswordRecoveryActivity() {
        startActivity(PasswordRecoveryActivity.newFirstPhaseInstance(
            context = this,
            transitionAnimations = TransitionAnimations.HORIZONTAL_SLIDING_ANIMATIONS
        ))
    }


    override fun launchRegistrationActivity() {
        startActivity(RegistrationActivity.newInstance(
            context = this,
            transitionAnimations = TransitionAnimations.HORIZONTAL_SLIDING_ANIMATIONS
        ))
    }


    override fun launchDestinationActivity() {
        startActivity(AuthenticationActivity.newInstance(
            this,
            PinCodeMode.CREATION,
            TransitionAnimations.OVERSHOOT_SCALING_ANIMATIONS,
            getSettings().theme,
            mDestinationIntent
        ))
    }


    override fun recreateUserLogoutAlarm(triggerAtMillis: Long) {
        UserLogoutReceiver.recreateUserLogoutAlarm(this, triggerAtMillis)
    }


    override fun setInputViewsState(state: InputViewState, inputViews: List<LoginInputView>) {
        for(inputView in inputViews) {
            when(inputView) {
                LoginInputView.EMAIL -> credentialsView.setEmailInputViewState(state)
                LoginInputView.PASSWORD -> credentialsView.setPasswordInputViewState(state)
                LoginInputView.CONFIRMATION_CODE -> {
                    ((mSelectedMainView as? BaseLoginConfirmationView))?.setCodeInputViewState(state)
                }
            }
        }
    }


    override fun hasSecondaryButton(): Boolean = false


    override fun getContentLayoutResourceId(): Int = R.layout.login_activity_layout


    override fun getPrimaryButtonText(): String {
        return mStringProvider.getPrimaryButtonTextForLoginProcessPhase(mPresenter.processPhase)
    }


    override fun getEmail(): String = credentialsView.getEmail()


    override fun getPassword(): String = credentialsView.getPassword()


    override fun getCode(): String = when(mPresenter.confirmationType) {
        SignInConfirmationType.EMAIL -> emailConfirmationView.getCode()
        SignInConfirmationType.SMS -> smsConfirmationView.getCode()
        SignInConfirmationType.TWO_FACTOR_AUTHENTICATION -> twoFactorAuthConfirmationView.getCode()
        SignInConfirmationType.UNKNOWN -> ""
    }


    override fun getInitialProcessPhase(): LoginProcessPhase {
        return LoginProcessPhase.AWAITING_CREDENTIALS
    }


    override fun getTransitionAnimations(): TransitionAnimations {
        return mPresenter.transitionAnimations
    }


    override fun getCurrentlyVisibleMainView(phase: LoginProcessPhase): View? = when(phase) {
        LoginProcessPhase.AWAITING_CREDENTIALS -> credentialsView
        LoginProcessPhase.AWAITING_ACCOUNT_VERIFICATION -> accountVerificationView
        LoginProcessPhase.AWAITING_CONFIRMATION -> when(mPresenter.confirmationType) {
            SignInConfirmationType.EMAIL -> emailConfirmationView
            SignInConfirmationType.SMS -> smsConfirmationView
            SignInConfirmationType.TWO_FACTOR_AUTHENTICATION -> twoFactorAuthConfirmationView

            else -> mSelectedMainView
        }
    }


    override fun getAppTitleIv(): ImageView = icStexLogoIv


    override fun getAppMottoTv(): TextView = appMottoTv


    override fun getPrimaryButton(): UserAdmissionButton = primaryBtn


    override fun getContentView(): View = contentContainerRl


    override fun getMainViewsArray(): Array<View> = arrayOf(
        credentialsView,
        accountVerificationView,
        emailConfirmationView,
        smsConfirmationView,
        twoFactorAuthConfirmationView
    )


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if(intent == null) {
            return
        }

        intent.extras?.let(extrasExtractor)?.also {
            if(it.wasAccountJustVerified) {
                mPresenter.onAccountVerified()
            }
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


    override fun onRecycle() {
        super.onRecycle()

        mKeyboardManager.recycle()
    }


}