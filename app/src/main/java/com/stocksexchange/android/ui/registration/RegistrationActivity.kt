package com.stocksexchange.android.ui.registration

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.toSpannable
import com.stocksexchange.android.R
import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.model.RegistrationInputView
import com.stocksexchange.android.model.RegistrationProcessPhase
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.useradmission.BaseUserAdmissionActivity
import com.stocksexchange.android.ui.login.LoginActivity
import com.stocksexchange.android.ui.login.newInstance
import com.stocksexchange.android.ui.views.UserAdmissionButton
import com.stocksexchange.android.utils.handlers.BrowserHandler
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.text.CustomLinkMovementMethod
import com.stocksexchange.core.utils.text.SelectorSpan
import kotlinx.android.synthetic.main.registration_activity_layout.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class RegistrationActivity : BaseUserAdmissionActivity<
    RegistrationPresenter,
    RegistrationProcessPhase,
    RegistrationInputView
>(), RegistrationContract.View {


    companion object {

        private const val TERMS_OF_USE_LINK_ALPHA_VALUE = 0.2f

    }


    override val mPresenter: RegistrationPresenter by inject { parametersOf(this) }

    private lateinit var mDestinationIntent: Intent




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.transitionAnimations = it.transitionAnimations

            mDestinationIntent = it.destinationIntent
        }
    }


    override fun initMainViews() {
        super.initMainViews()

        initCredentialsView()
        initAccountVerificationView()
    }


    private fun initCredentialsView() {
        with(credentialsView) {
            val generalTheme = getAppTheme().generalTheme
            val termsOfUseParameter = getStr(R.string.registration_terms_of_use_check_box_text_parameter)
            val termsOfUseCheckBoxText = mStringProvider.getString(
                R.string.registration_terms_of_use_check_box_text_template,
                termsOfUseParameter
            )
            val spannableString = termsOfUseCheckBoxText.toSpannable().apply {
                val selectorSpan = object : SelectorSpan(
                    generalTheme.accentColor,
                    generalTheme.accentColor.adjustAlpha(TERMS_OF_USE_LINK_ALPHA_VALUE)
                ) {

                    override fun onClick(widget: View) {
                        mPresenter.onTermsOfUseLinkClicked()
                    }

                }
                val spanStartIndex = indexOf(termsOfUseParameter)
                val spanEndIndex = (spanStartIndex + termsOfUseParameter.length)

                this[spanStartIndex, spanEndIndex] = StyleSpan(Typeface.BOLD)
                this[spanStartIndex, spanEndIndex] = selectorSpan
            }

            setTermsOfUseCheckBoxMovementMethod(CustomLinkMovementMethod())
            setTermsOfUseCheckBoxText(spannableString)
            setAgeCheckBoxText(getStr(R.string.registration_age_check_box_text))
            setReferralCodeDrawableClickListener {
                mPresenter.onReferralCodeIconClicked()
            }

            ThemingUtil.Registration.credentialsView(this, getAppTheme())
        }
    }


    private fun initAccountVerificationView() {
        registrationText.text = getStr(R.string.registration_account_verification_dialog_message)

        ThemingUtil.Registration.textView(registrationText, getAppTheme())
    }


    override fun setInputViewsState(state: InputViewState, inputViews: List<RegistrationInputView>) {
        for(inputView in inputViews) {
            when(inputView) {
                RegistrationInputView.EMAIL -> credentialsView.setEmailInputViewState(state)
                RegistrationInputView.PASSWORD -> credentialsView.setPasswordInputViewState(state)
                RegistrationInputView.REFERRAL_CODE -> credentialsView.setReferralCodeInputViewState(state)
            }
        }
    }


    override fun launchLoginActivity() {
        startActivity(LoginActivity.newInstance(
            context = this,
            destinationIntent = mDestinationIntent
        ))
    }


    override fun launchBrowser(url: String) {
        get<BrowserHandler>().launchBrowser(this, url, getAppTheme())
    }


    override fun hasSecondaryButton(): Boolean = true


    override fun isTermsOfUseCheckBoxChecked(): Boolean = credentialsView.isTermsOfUseCheckBoxChecked()


    override fun isAgeCheckBoxChecked(): Boolean = credentialsView.isAgeCheckBoxChecked()


    override fun getContentLayoutResourceId(): Int = R.layout.registration_activity_layout


    override fun getSecondaryButtonVisibility(): Int = when(mPresenter.processPhase) {
        RegistrationProcessPhase.AWAITING_CREDENTIALS -> View.GONE
        RegistrationProcessPhase.AWAITING_ACCOUNT_VERIFICATION -> View.VISIBLE
    }


    override fun getPrimaryButtonText(): String {
        return mStringProvider.getPrimaryButtonTextForRegistrationProcessPhase(mPresenter.processPhase)
    }


    override fun getSecondaryButtonText(): String {
        return getStr(R.string.action_resend_verification_email)
    }


    override fun getEmail(): String = credentialsView.getEmail()


    override fun getPassword(): String = credentialsView.getPassword()


    override fun getReferralCode(): String = credentialsView.getReferralCode()


    override fun getInitialProcessPhase(): RegistrationProcessPhase {
        return RegistrationProcessPhase.AWAITING_CREDENTIALS
    }


    override fun getTransitionAnimations(): TransitionAnimations {
        return mPresenter.transitionAnimations
    }


    override fun getCurrentlyVisibleMainView(phase: RegistrationProcessPhase): View? = when(phase) {
        RegistrationProcessPhase.AWAITING_CREDENTIALS -> credentialsView
        RegistrationProcessPhase.AWAITING_ACCOUNT_VERIFICATION -> registrationText
    }


    override fun getAppTitleIv(): ImageView = icStexLogoIv


    override fun getAppMottoTv(): TextView = appMottoTv


    override fun getPrimaryButton(): UserAdmissionButton = primaryBtn


    override fun getSecondaryButton(): UserAdmissionButton? = secondaryBtn


    override fun getContentView(): View = contentContainerRl


    override fun getMainViewsArray(): Array<View> = arrayOf(
        credentialsView,
        registrationText
    )


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