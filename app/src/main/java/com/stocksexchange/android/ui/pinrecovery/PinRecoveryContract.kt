package com.stocksexchange.android.ui.pinrecovery

import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface PinRecoveryContract {


    interface View : BaseView {

        fun launchDashboardActivity()

        fun finishActivity()

    }


    interface ActionListener {

        fun onCancellationButtonClicked()

        fun onConfirmationButtonClicked()

    }


}