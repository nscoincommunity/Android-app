package com.stocksexchange.android.ui.login.views

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.InputView
import com.stocksexchange.android.ui.views.base.useradmission.BaseUserAdmissionCredentialsView
import com.stocksexchange.core.utils.extensions.getCompatDrawable
import kotlinx.android.synthetic.main.login_credentials_view.view.*

/**
 * A login input view that enables a user to enter his
 * credentials (user and password).
 */
class LoginCredentialsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseUserAdmissionCredentialsView(context, attrs, defStyleAttr) {


    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        super.saveAttributes(attrs, defStyleAttr)

        context.withStyledAttributes(attrs, R.styleable.LoginCredentialsView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_INPUT_VIEW_TEXT_COLOR, getColor(R.styleable.LoginCredentialsView_inputViewTextColor, DEFAULT_INPUT_VIEW_TEXT_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_HINT_TEXT_COLOR, getColor(R.styleable.LoginCredentialsView_inputViewHintTextColor, DEFAULT_INPUT_VIEW_HINT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_CURSOR_COLOR, getColor(R.styleable.LoginCredentialsView_inputViewCursorColor, DEFAULT_INPUT_VIEW_CURSOR_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_ICON_COLOR, getColor(R.styleable.LoginCredentialsView_inputViewIconColor, DEFAULT_INPUT_VIEW_ICON_COLOR))
                save(ATTRIBUTE_KEY_HELP_BUTTON_TEXT_COLOR, getColor(R.styleable.LoginCredentialsView_helpButtonTextColor, DEFAULT_HELP_BUTTON_TEXT_COLOR))
                save(ATTRIBUTE_KEY_PASSWORD_INPUT_VIEW_VISIBLE_ICON, getDrawable(R.styleable.LoginCredentialsView_inputViewPasswordVisibleIcon) ?: getCompatDrawable(DEFAULT_PASSWORD_INPUT_VIEW_VISIBLE_ICON))
                save(ATTRIBUTE_KEY_PASSWORD_INPUT_VIEW_HIDDEN_ICON, getDrawable(R.styleable.LoginCredentialsView_inputViewPasswordHiddenIcon) ?: getCompatDrawable(DEFAULT_PASSWORD_INPUT_VIEW_HIDDEN_ICON))
            }
        }
    }


    override fun init() {
        super.init()

        initHelpButtons()
        initDoNotHaveAccountButton()
    }


    private fun initHelpButtons() {
        forgotPasswordTv.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
            text = mStringProvider.getString(R.string.login_help_button_text)
        }
    }


    private fun initDoNotHaveAccountButton() {
        doNotHaveAnAccountBtn.text = mStringProvider.getString(R.string.login_register_button_text)
    }


    fun setOnForgotPasswordClickListener(listener: ((View) -> Unit)) {
        forgotPasswordTv.setOnClickListener(listener)
    }


    fun setForgotPasswordTextColor(@ColorInt color: Int) {
        forgotPasswordTv.setTextColor(color)
    }


    fun setInputLabelsColor(@ColorInt color: Int) {
        emailLabelTv.setTextColor(color)
        passwordLabelTv.setTextColor(color)
    }

    
    fun setDoNotHaveAccountTextColor(@ColorInt color: Int) {
        doNotHaveAnAccountBtn.setTextColor(color)
    }


    fun setOnDoNotHaveAccountClickListener(listener: ((View) -> Unit)) {
        doNotHaveAnAccountBtn.setOnClickListener(listener)
    }


    override fun hasHint(): Boolean = false


    override fun hasHelpButtons(): Boolean = true


    override fun getLayoutResourceId(): Int = R.layout.login_credentials_view


    override fun getEmailIv(): InputView = emailIv


    override fun getPasswordIv(): InputView = passwordIv


    override fun getHelpButtonsArray(): Array<TextView> = arrayOf(forgotPasswordTv)


    override fun getInputViewsArray(): Array<InputView> = arrayOf(emailIv, passwordIv)


}