package com.stocksexchange.android.ui.login

import com.stocksexchange.api.model.rest.SignInConfirmationType
import com.stocksexchange.android.model.LoginInputView
import com.stocksexchange.android.model.LoginProcessPhase
import com.stocksexchange.android.ui.base.useradmission.UserAdmissionView

interface LoginContract {


    interface View : UserAdmissionView<
        LoginProcessPhase,
        LoginInputView
    > {

        fun sendEmail(emailAddress: String, subject: String, text: String)

        fun launchPasswordRecoveryActivity()

        fun launchRegistrationActivity()

        fun launchDestinationActivity()

        fun recreateUserLogoutAlarm(triggerAtMillis: Long)

        fun hasWindowFocus(): Boolean

        fun isFinishing(): Boolean

        fun getEmail(): String

        fun getPassword(): String

        fun getCode(): String

    }


    interface ActionListener {

        fun onCredentialsViewHelpButtonClicked()

        fun onCredentialsViewRegisterButtonClicked()

        fun onConfirmationViewHelpButtonClicked(type: SignInConfirmationType)

        fun onSecurityCodeReceived(type: SignInConfirmationType, code: String)

        fun onAccountVerified()

    }


}