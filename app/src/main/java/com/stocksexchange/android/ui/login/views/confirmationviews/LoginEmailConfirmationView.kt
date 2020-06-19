package com.stocksexchange.android.ui.login.views.confirmationviews

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.SignInConfirmationType
import com.stocksexchange.android.ui.login.views.confirmationviews.base.BaseLoginConfirmationView

class LoginEmailConfirmationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseLoginConfirmationView(context, attrs, defStyleAttr) {


    override fun hasHelpButtons(): Boolean = false


    override fun getCodeLength(): Int = Constants.EMAIL_CONFIRMATION_CODE_LENGTH


    override fun getInputViewType(): Int {
        return (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL)
    }


    override fun getHintText(): String {
        return mStringProvider.getString(R.string.login_confirmation_email_hint)
    }


    override fun getInputLabelText(): String {
        return mStringProvider.getString(R.string.email)
    }


    override fun getConfirmationType(): SignInConfirmationType {
        return SignInConfirmationType.EMAIL
    }


}