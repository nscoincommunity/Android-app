package com.stocksexchange.android.ui.referral

import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.model.ReferralMode
import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface ReferralContract {


    interface View : BaseView {

        fun showProgressBar()

        fun hideProgressBar()

        fun showReferralCodeInput()

        fun showNotice()

        fun shareText(text: String, chooserTitle: String)

        fun updateReferralMode(referralMode: ReferralMode)

        fun setReferralCodeInputViewState(state: InputViewState)

        fun getReferralCodeInput(): String

    }


    interface ActionListener {

        fun onShareButtonClicked()

        fun onReferralLinkClicked()

        fun onLeftButtonClicked()

        fun onRightButtonClicked()

    }


}