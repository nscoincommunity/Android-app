package com.stocksexchange.android.ui.verification.prompt

import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface VerificationPromptContract {


    interface View : BaseView {

        fun launchBrowser(url: String)

        fun finishActivity()

    }


    interface ActionListener {

        fun onEuParliamentDirectiveClicked()

        fun onVerifyNowButtonClicked()

        fun onVerifyLaterButtonClicked()

        fun onSwipedToBottom()

    }


}