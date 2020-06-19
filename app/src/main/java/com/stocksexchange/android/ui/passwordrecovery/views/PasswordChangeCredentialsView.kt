package com.stocksexchange.android.ui.passwordrecovery.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.stocksexchange.android.R
import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.ui.views.InputView
import com.stocksexchange.android.ui.views.base.useradmission.BaseUserAdmissionCredentialsView
import com.stocksexchange.core.utils.extensions.getCompatDrawable
import kotlinx.android.synthetic.main.password_change_credentials_view.view.*

/**
 * A password change input view that enables a user to
 * change his password.
 */
class PasswordChangeCredentialsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArr: Int = 0
) : BaseUserAdmissionCredentialsView(context, attrs, defStyleArr) {


    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        super.saveAttributes(attrs, defStyleAttr)

        context.withStyledAttributes(attrs, R.styleable.PasswordChangeCredentialsView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_INPUT_VIEW_TEXT_COLOR, getColor(R.styleable.PasswordChangeCredentialsView_inputViewTextColor, DEFAULT_INPUT_VIEW_TEXT_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_HINT_TEXT_COLOR, getColor(R.styleable.PasswordChangeCredentialsView_inputViewHintTextColor, DEFAULT_INPUT_VIEW_HINT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_CURSOR_COLOR, getColor(R.styleable.PasswordChangeCredentialsView_inputViewCursorColor, DEFAULT_INPUT_VIEW_CURSOR_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_ICON_COLOR, getColor(R.styleable.PasswordChangeCredentialsView_inputViewIconColor, DEFAULT_INPUT_VIEW_ICON_COLOR))
                save(ATTRIBUTE_KEY_HELP_BUTTON_TEXT_COLOR, getColor(R.styleable.PasswordChangeCredentialsView_helpButtonTextColor, DEFAULT_HELP_BUTTON_TEXT_COLOR))
                save(ATTRIBUTE_KEY_PASSWORD_INPUT_VIEW_VISIBLE_ICON, getDrawable(R.styleable.PasswordChangeCredentialsView_inputViewPasswordVisibleIcon) ?: getCompatDrawable(DEFAULT_PASSWORD_INPUT_VIEW_VISIBLE_ICON))
                save(ATTRIBUTE_KEY_PASSWORD_INPUT_VIEW_HIDDEN_ICON, getDrawable(R.styleable.PasswordChangeCredentialsView_inputViewPasswordHiddenIcon) ?: getCompatDrawable(DEFAULT_PASSWORD_INPUT_VIEW_HIDDEN_ICON))
            }
        }
    }


    override fun init() {
        super.init()

        initPasswordConfirmationInput()
        initInputLabels()
    }


    private fun initPasswordConfirmationInput() {
        with(passwordConfirmationIv) {
            setEtInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            setEtHintText(mStringProvider.getString(R.string.action_confirm_new_password))
        }
    }


    private fun initInputLabels() {
        emailLabelTv.text = mStringProvider.getString(R.string.email)
        passwordLabelTv.text = mStringProvider.getString(R.string.password)
        confirmPasswordLabelTv.text = mStringProvider.getString(R.string.action_confirm_new_password)
    }


    fun setPasswordConfirmationInputViewState(state: InputViewState) {
        passwordConfirmationIv.setState(state)
    }


    fun setInputLabelTextColor(@ColorInt color: Int) {
        emailLabelTv.setTextColor(color)
        passwordLabelTv.setTextColor(color)
        confirmPasswordLabelTv.setTextColor(color)
    }


    override fun hasHint(): Boolean = false


    override fun hasHelpButtons(): Boolean = false


    override fun getLayoutResourceId(): Int = R.layout.password_change_credentials_view


    override fun getPasswordInputHintText(): String {
        return mStringProvider.getString(R.string.new_password)
    }


    fun getPasswordConfirmation(): String = passwordConfirmationIv.getContent()


    override fun getEmailIv(): InputView = emailIv


    override fun getPasswordIv(): InputView = passwordIv


    override fun getInputViewsArray(): Array<InputView> {
        return arrayOf(emailIv, passwordIv, passwordConfirmationIv)
    }


    override fun onPasswordVisibilityChanged(isPasswordVisible: Boolean,
                                             newDrawable: Drawable?,
                                             newType: Int) {
        with(passwordConfirmationIv) {
            setEtInputType(newType, false)
            setEtSelection(getInputViewEt().length())
        }
    }


}