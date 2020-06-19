package com.stocksexchange.android.ui.registration.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.withStyledAttributes
import androidx.core.widget.CompoundButtonCompat
import com.stocksexchange.android.R
import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.ui.views.InputView
import com.stocksexchange.android.ui.views.base.useradmission.BaseUserAdmissionCredentialsView
import com.stocksexchange.core.utils.extensions.getCompatDrawable
import com.stocksexchange.core.utils.extensions.setColor
import com.stocksexchange.core.utils.extensions.toColorStateList
import kotlinx.android.synthetic.main.registration_credentials_view.view.*

/**
 * A registration input view that enables a user to enter his
 * credentials for the purpose of registering.
 */
class RegistrationCredentialsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseUserAdmissionCredentialsView(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_CHECK_BOX_COLOR = "check_box_color"
        private const val ATTRIBUTE_KEY_CHECK_BOX_TEXT_COLOR = "check_box_text_color"
        private const val ATTRIBUTE_KEY_CHECK_BOX_TEXT_HIGHLIGHT_COLOR = "check_box_text_highlight_color"

        private const val ATTRIBUTE_KEY_INPUT_VIEW_REFERRAL_CODE_ICON = "input_view_referral_code_icon"

        private const val DEFAULT_CHECK_BOX_COLOR = Color.GREEN
        private const val DEFAULT_CHECK_BOX_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_CHECK_BOX_TEXT_HIGHLIGHT_COLOR = Color.TRANSPARENT

        private const val DEFAULT_INPUT_VIEW_REFERRAL_CODE_HELP_ICON = R.drawable.ic_question

    }


    private var mReferralCodeDrawable: Drawable? = null

    private lateinit var mTextViews: Array<TextView>

    private lateinit var mCheckBoxes: Array<AppCompatCheckBox>




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        super.saveAttributes(attrs, defStyleAttr)

        context.withStyledAttributes(attrs, R.styleable.RegistrationCredentialsView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_INPUT_VIEW_TEXT_COLOR, getColor(R.styleable.RegistrationCredentialsView_inputViewTextColor, DEFAULT_INPUT_VIEW_TEXT_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_HINT_TEXT_COLOR, getColor(R.styleable.RegistrationCredentialsView_inputViewHintTextColor, DEFAULT_INPUT_VIEW_HINT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_CURSOR_COLOR, getColor(R.styleable.RegistrationCredentialsView_inputViewCursorColor, DEFAULT_INPUT_VIEW_CURSOR_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_ICON_COLOR, getColor(R.styleable.RegistrationCredentialsView_inputViewIconColor, DEFAULT_INPUT_VIEW_ICON_COLOR))
                save(ATTRIBUTE_KEY_PASSWORD_INPUT_VIEW_VISIBLE_ICON, getDrawable(R.styleable.RegistrationCredentialsView_inputViewPasswordVisibleIcon) ?: getCompatDrawable(DEFAULT_PASSWORD_INPUT_VIEW_VISIBLE_ICON))
                save(ATTRIBUTE_KEY_PASSWORD_INPUT_VIEW_HIDDEN_ICON, getDrawable(R.styleable.RegistrationCredentialsView_inputViewPasswordHiddenIcon) ?: getCompatDrawable(DEFAULT_PASSWORD_INPUT_VIEW_HIDDEN_ICON))
                save(ATTRIBUTE_KEY_CHECK_BOX_COLOR, getColor(R.styleable.RegistrationCredentialsView_checkBoxColor, DEFAULT_CHECK_BOX_COLOR))
                save(ATTRIBUTE_KEY_CHECK_BOX_TEXT_COLOR, getColor(R.styleable.RegistrationCredentialsView_checkBoxTextColor, DEFAULT_CHECK_BOX_TEXT_COLOR))
                save(ATTRIBUTE_KEY_CHECK_BOX_TEXT_HIGHLIGHT_COLOR, getColor(R.styleable.RegistrationCredentialsView_checkBoxTextHighlightColor, DEFAULT_CHECK_BOX_TEXT_HIGHLIGHT_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_REFERRAL_CODE_ICON, (getDrawable(R.styleable.RegistrationCredentialsView_inputViewReferralCodeIcon) ?: getCompatDrawable(DEFAULT_INPUT_VIEW_REFERRAL_CODE_HELP_ICON)))
            }
        }
    }


    override fun init() {
        super.init()

        initReferralCodeInput()
        initInputLabels()

        mTextViews = arrayOf(termsOfUseTv, ageTv)
        mCheckBoxes = arrayOf(termsOfUseCb, ageCb)
    }


    private fun initReferralCodeInput() {
        with(referralCodeIv) {
            setIconVisible(true)
            setEtHintText(mStringProvider.getString(R.string.enter_your_code))
        }
    }


    private fun initInputLabels() {
        emailLabelTv.text = mStringProvider.getString(R.string.email)
        passwordLabelTv.text = mStringProvider.getString(R.string.password)
        referralLabelTv.text = mStringProvider.getString(R.string.referral_code)
    }


    override fun applyAttributes() {
        super.applyAttributes()

        with(mAttributes) {
            setCheckBoxColor(get(ATTRIBUTE_KEY_CHECK_BOX_COLOR, DEFAULT_CHECK_BOX_COLOR))
            setCheckBoxTextColor(get(ATTRIBUTE_KEY_CHECK_BOX_TEXT_COLOR, DEFAULT_CHECK_BOX_TEXT_COLOR))
            setCheckBoxTextHighlightColor(get(ATTRIBUTE_KEY_CHECK_BOX_TEXT_HIGHLIGHT_COLOR, DEFAULT_CHECK_BOX_TEXT_HIGHLIGHT_COLOR))
            setReferralCodeDrawable(get(ATTRIBUTE_KEY_INPUT_VIEW_REFERRAL_CODE_ICON, getCompatDrawable(DEFAULT_INPUT_VIEW_REFERRAL_CODE_HELP_ICON)))
        }
    }


    override fun setInputViewIconColor(color: Int) {
        super.setInputViewIconColor(color)

        setReferralCodeDrawable(mReferralCodeDrawable?.apply {
            setColor(color)
        })
    }


    fun setCheckBoxColor(@ColorInt color: Int) {
        mCheckBoxes.forEach {
            CompoundButtonCompat.setButtonTintList(it, color.toColorStateList())
        }
    }


    fun setCheckBoxTextColor(@ColorInt color: Int) {
        mTextViews.forEach {
            it.setTextColor(color)
        }
    }


    fun setCheckBoxTextHighlightColor(@ColorInt color: Int) {
        mTextViews.forEach {
            it.highlightColor = color
        }
    }


    fun setReferralCodeInputViewState(state: InputViewState) {
        referralCodeIv.setState(state)
    }


    fun setTermsOfUseCheckBoxText(text: CharSequence) {
        termsOfUseTv.text = text
    }


    fun setAgeCheckBoxText(text: CharSequence) {
        ageTv.text = text
    }


    fun setTermsOfUseCheckBoxMovementMethod(method: MovementMethod) {
        termsOfUseTv.movementMethod = method
    }


    fun setAgeCheckBoxMovementMethod(method: MovementMethod) {
        ageTv.movementMethod = method
    }


    fun setReferralCodeDrawable(drawable: Drawable?) {
        mReferralCodeDrawable = drawable

        referralCodeIv.setIconDrawable(drawable)
    }


    fun setReferralCodeDrawableClickListener(listener: ((View) -> Unit)) {
        referralCodeIv.setOnIconClickListener(listener)
    }


    fun setInputLabelColor(@ColorInt color: Int) {
        emailLabelTv.setTextColor(color)
        passwordLabelTv.setTextColor(color)
        referralLabelTv.setTextColor(color)

        termsOfUseTv.setTextColor(color)
        ageTv.setTextColor(color)
    }


    override fun hasHint(): Boolean = false


    override fun hasHelpButtons(): Boolean = false


    fun isTermsOfUseCheckBoxChecked(): Boolean = termsOfUseCb.isChecked


    fun isAgeCheckBoxChecked(): Boolean = ageCb.isChecked


    override fun getLayoutResourceId(): Int = R.layout.registration_credentials_view


    fun getReferralCode(): String = referralCodeIv.getContent()


    override fun getEmailIv(): InputView = emailIv


    override fun getPasswordIv(): InputView = passwordIv


    override fun getInputViewsArray(): Array<InputView> {
        return arrayOf(emailIv, passwordIv, referralCodeIv)
    }


}