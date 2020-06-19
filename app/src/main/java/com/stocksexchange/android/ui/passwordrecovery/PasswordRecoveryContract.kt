package com.stocksexchange.android.ui.passwordrecovery

import com.stocksexchange.android.model.PasswordRecoveryInputView
import com.stocksexchange.android.model.PasswordRecoveryProcessPhase
import com.stocksexchange.android.ui.base.useradmission.UserAdmissionView

interface PasswordRecoveryContract {


    interface View : UserAdmissionView<
        PasswordRecoveryProcessPhase,
        PasswordRecoveryInputView
    > {

        fun launchLoginActivity()

        fun getCredentialViewEmail(): String

        fun getCredentialsViewEmail(): String

        fun getCredentialsViewPassword(): String

        fun getCredentialsViewPasswordConfirmation(): String

    }


    interface ActionListener


}