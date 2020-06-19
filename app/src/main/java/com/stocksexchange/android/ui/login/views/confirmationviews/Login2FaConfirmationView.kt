package com.stocksexchange.android.ui.login.views.confirmationviews

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.SignInConfirmationType
import com.stocksexchange.android.ui.login.views.confirmationviews.base.BaseLoginConfirmationView

class Login2FaConfirmationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseLoginConfirmationView(context, attrs, defStyleAttr) {


    override fun hasHelpButtons(): Boolean = true


    override fun getCodeLength(): Int = Constants.TWO_FACTOR_AUTH_CODE_LENGTH


    override fun getInputViewType(): Int {
        return (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL)
    }


    override fun getHintText(): String {
        return mStringProvider.getString(R.string.login_confirmation_2fa_hint)
    }


    override fun getHelpButtonText(): String {
        return mStringProvider.getString(R.string.login_2fa_help_dialog_title)
    }


    override fun getInputLabelText(): String {
        return mStringProvider.getString(R.string.login_confirmation_disable_2fa_text)
    }


    override fun getConfirmationType(): SignInConfirmationType {
        return SignInConfirmationType.TWO_FACTOR_AUTHENTICATION
    }


}