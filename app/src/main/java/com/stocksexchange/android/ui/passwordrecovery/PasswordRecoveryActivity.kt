package com.stocksexchange.android.ui.passwordrecovery

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.stocksexchange.android.R
import com.stocksexchange.android.model.*
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.useradmission.BaseUserAdmissionActivity
import com.stocksexchange.android.ui.login.LoginActivity
import com.stocksexchange.android.ui.login.newInstance
import com.stocksexchange.android.ui.views.UserAdmissionButton
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.password_recovery_activity_layout.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class PasswordRecoveryActivity : BaseUserAdmissionActivity<
    PasswordRecoveryPresenter,
    PasswordRecoveryProcessPhase,
    PasswordRecoveryInputView
>(), PasswordRecoveryContract.View {


    companion object {}


    override val mPresenter: PasswordRecoveryPresenter by inject { parametersOf(this) }




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.passwordResetToken = it.passwordResetToken
            mPresenter.processPhase = it.processPhase
            mPresenter.transitionAnimations = it.transitionAnimations
        }
    }


    override fun initMainViews() {
        super.initMainViews()

        initEmailCredentialView()
        initCredentialsView()
        initRecoveryPasswordInfo()
        initSuccessInfoView()
    }


    private fun initEmailCredentialView() {
        ThemingUtil.PasswordRecovery.emailCredentialView(emailCredentialView, getAppTheme())
    }


    private fun initCredentialsView() {
        ThemingUtil.PasswordRecovery.credentialsView(credentialsView, getAppTheme())
    }


    private fun initRecoveryPasswordInfo() {
        recoveryText.text =  getStr(R.string.password_recovery_reset_link_sent_dialog_content)

        ThemingUtil.PasswordRecovery.textView(recoveryText, getAppTheme())
    }


    private fun initSuccessInfoView() {
        with(successInfoView) {
            clearPadding()
            setCaption(mStringProvider.getString(R.string.password_recovery_reset_dialog_content))

            ThemingUtil.PasswordRecovery.successInfoView(this, getAppTheme())
        }
    }


    override fun launchLoginActivity() {
        startActivity(LoginActivity.newInstance(this))
    }


    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun setInputViewsState(state: InputViewState, inputViews: List<PasswordRecoveryInputView>) {
        for(inputView in inputViews) {
            when(inputView) {
                PasswordRecoveryInputView.EMAIL -> when(mPresenter.processPhase) {
                    PasswordRecoveryProcessPhase.AWAITING_EMAIL_ADDRESS -> emailCredentialView.setEmailInputViewState(state)
                    PasswordRecoveryProcessPhase.AWAITING_NEW_PASSWORD -> credentialsView.setEmailInputViewState(state)
                }
                PasswordRecoveryInputView.NEW_PASSWORD -> credentialsView.setPasswordInputViewState(state)
                PasswordRecoveryInputView.PASSWORD_CONFIRMATION -> credentialsView.setPasswordConfirmationInputViewState(state)
            }
        }
    }


    override fun hasSecondaryButton(): Boolean = true


    override fun getContentLayoutResourceId(): Int = R.layout.password_recovery_activity_layout


    override fun getSecondaryButtonVisibility(): Int = when(mPresenter.processPhase) {
        PasswordRecoveryProcessPhase.PASSWORD_RESET_EMAIL_SENT -> View.VISIBLE

        else -> View.GONE
    }


    override fun getPrimaryButtonText(): String {
        return mStringProvider.getPrimaryButtonTextForPasswordRecoveryProcessPhase(
            mPresenter.processPhase
        )
    }


    override fun getSecondaryButtonText(): String {
        return getStr(R.string.action_resend_password_reset_email)
    }


    override fun getCredentialViewEmail(): String = emailCredentialView.getEmail()


    override fun getCredentialsViewEmail(): String = credentialsView.getEmail()


    override fun getCredentialsViewPassword(): String = credentialsView.getPassword()


    override fun getCredentialsViewPasswordConfirmation(): String {
        return credentialsView.getPasswordConfirmation()
    }


    override fun getInitialProcessPhase(): PasswordRecoveryProcessPhase {
        return PasswordRecoveryProcessPhase.AWAITING_EMAIL_ADDRESS
    }


    override fun getTransitionAnimations(): TransitionAnimations {
        return mPresenter.transitionAnimations
    }


    override fun getCurrentlyVisibleMainView(phase: PasswordRecoveryProcessPhase): View? = when(phase) {
        PasswordRecoveryProcessPhase.AWAITING_EMAIL_ADDRESS -> emailCredentialView
        PasswordRecoveryProcessPhase.AWAITING_NEW_PASSWORD -> credentialsView
        PasswordRecoveryProcessPhase.PASSWORD_RESET_EMAIL_SENT -> recoveryText
        PasswordRecoveryProcessPhase.PASSWORD_RESET -> successInfoView
    }


    override fun getAppTitleIv(): ImageView = icStexLogoIv


    override fun getAppMottoTv(): TextView = appMottoTv


    override fun getPrimaryButton(): UserAdmissionButton = primaryBtn


    override fun getSecondaryButton(): UserAdmissionButton? = secondaryBtn


    override fun getContentView(): View = contentContainerCl


    override fun getMainViewsArray(): Array<View> = arrayOf(
        emailCredentialView,
        credentialsView,
        successInfoView,
        recoveryText
    )


}