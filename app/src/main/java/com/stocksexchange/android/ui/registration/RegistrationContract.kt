package com.stocksexchange.android.ui.registration

import com.stocksexchange.android.model.RegistrationInputView
import com.stocksexchange.android.model.RegistrationProcessPhase
import com.stocksexchange.android.ui.base.useradmission.UserAdmissionView

interface RegistrationContract {


    interface View : UserAdmissionView<
        RegistrationProcessPhase,
        RegistrationInputView
    > {

        fun launchLoginActivity()

        fun launchBrowser(url: String)

        fun isTermsOfUseCheckBoxChecked(): Boolean

        fun isAgeCheckBoxChecked(): Boolean

        fun getEmail(): String

        fun getPassword(): String

        fun getReferralCode(): String

    }


    interface ActionListener {

        fun onReferralCodeIconClicked()

        fun onTermsOfUseLinkClicked()

    }


}