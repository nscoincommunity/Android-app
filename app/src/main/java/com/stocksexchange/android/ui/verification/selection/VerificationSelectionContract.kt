package com.stocksexchange.android.ui.verification.selection

import com.stocksexchange.api.model.rest.VerificationType
import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface VerificationSelectionContract {


    interface View : BaseView {

        fun launchBrowser(url: String)

    }


    interface ActionListener {

        fun onVerificationClicked(item: VerificationType)

    }


}