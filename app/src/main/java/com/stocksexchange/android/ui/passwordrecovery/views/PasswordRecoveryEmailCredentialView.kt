package com.stocksexchange.android.ui.passwordrecovery.views

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.InputType
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.stocksexchange.android.R
import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.ui.views.InputView
import com.stocksexchange.android.ui.views.base.useradmission.BaseUserAdmissionInputView
import kotlinx.android.synthetic.main.password_recovery_email_credential_view.view.*

/**
 * A password recovery input view that enables a user to enter
 * his email address in order to recover the password.
 */
class PasswordRecoveryEmailCredentialView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleArr: Int = 0
) : BaseUserAdmissionInputView(context, attrs, defStyleArr) {


    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.PasswordRecoveryEmailCredentialView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_HINT_TEXT_COLOR, getColor(R.styleable.PasswordRecoveryEmailCredentialView_hintTextColor, DEFAULT_HINT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_TEXT_COLOR, getColor(R.styleable.PasswordRecoveryEmailCredentialView_inputViewTextColor, DEFAULT_INPUT_VIEW_TEXT_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_HINT_TEXT_COLOR, getColor(R.styleable.PasswordRecoveryEmailCredentialView_inputViewHintTextColor, DEFAULT_INPUT_VIEW_HINT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_CURSOR_COLOR, getColor(R.styleable.PasswordRecoveryEmailCredentialView_inputViewCursorColor, DEFAULT_INPUT_VIEW_CURSOR_COLOR))
            }
        }
    }


    override fun init() {
        super.init()

        initHint()
        initInputLabel()
        initEmailInput()
    }


    private fun initHint() {
        hintTv.text = mStringProvider.getString(R.string.password_recovery_email_credential_view_hint_text)
    }


    private fun initInputLabel() {
        emailLabelTv.text = mStringProvider.getString(R.string.email)
    }


    private fun initEmailInput() {
        with(emailIv) {
            setEtHintText(mStringProvider.getString(R.string.enter_your_email))
            setEtInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        }
    }


    fun setEmailInputViewState(state: InputViewState) {
        emailIv.setState(state)
    }


    fun setEmailLabelTextColor(@ColorInt color: Int) {
        emailLabelTv.setTextColor(color)
    }


    override fun hasHint(): Boolean = true


    override fun hasHelpButtons(): Boolean = false


    override fun getLayoutResourceId(): Int = R.layout.password_recovery_email_credential_view


    fun getEmail(): String = emailIv.getContent()


    override fun getHintTv(): TextView? = hintTv


    override fun getInputViewsArray(): Array<InputView> {
        return arrayOf(emailIv)
    }


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).also {
            onSaveInstanceState(it)
        }
    }


    private class SavedState : BaseUserAdmissionInputViewState {

        companion object {

            @JvmField
            var CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(parcel: Parcel): SavedState {
                    return SavedState(parcel)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }

            }

        }


        constructor(parcel: Parcel): super(parcel)

        constructor(superState: Parcelable?): super(superState)

    }


}