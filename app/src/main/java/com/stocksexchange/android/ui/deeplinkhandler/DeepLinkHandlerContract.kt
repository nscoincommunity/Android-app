package com.stocksexchange.android.ui.deeplinkhandler

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface DeepLinkHandlerContract {


    interface View : BaseView {

        fun launchLoginActivity()

        fun launchPasswordRecoveryActivity(passwordResetToken: String)

        fun navigateToCurrencyMarketPreviewScreen(currencyMarket: CurrencyMarket)

        fun navigateToWithdrawalsScreen(wasWithdrawalJustConfirmed: Boolean = false,
                                        wasWithdrawalJustCancelled: Boolean = false)

        fun finishActivity()

        fun finishActivityWithError(error: String)

    }


    interface ActionListener {

        fun onUriRetrieved(uri: String)

    }


}