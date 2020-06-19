package com.stocksexchange.android.ui.protocolselection

import com.stocksexchange.api.model.rest.Protocol
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface ProtocolSelectionContract {


    interface View : BaseView {

        fun navigateToDepositCreationScreen(wallet: Wallet, protocol: Protocol)

        fun navigateToWithdrawalCreationScreen(wallet: Wallet, protocol: Protocol)

    }


    interface ActionListener {

        fun onProtocolClicked(protocol: Protocol)

    }


}