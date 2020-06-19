package com.stocksexchange.android.ui.wallets.fragment

import com.stocksexchange.api.model.rest.Protocol
import com.stocksexchange.api.model.rest.TransactionType
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.android.ui.base.listdataloading.ListDataLoadingView
import com.stocksexchange.api.model.rest.WalletBalanceType

interface WalletsContract {


    interface View : ListDataLoadingView<List<Wallet>> {

        fun updateItemWith(item: Wallet, position: Int)

        fun navigateToCurrencyMarketsSearchScreen(searchQuery: String)

        fun launchVerificationPromptActivity()

        fun navigateToProtocolSelectionScreen(transactionType: TransactionType, wallet: Wallet)

        fun navigateToDepositCreationScreen(wallet: Wallet, protocol: Protocol)

        fun navigateToWithdrawalCreationScreen(wallet: Wallet, protocol: Protocol)

        fun launchBrowser(url: String)

        fun getDataSetIndexForCurrencyId(currencyId: Int): Int?

        fun clearAdapter()

    }


    interface ActionListener {

        fun onCurrencyNameClicked(wallet: Wallet)

        fun onDepositButtonClicked(wallet: Wallet)

        fun onWithdrawButtonClicked(wallet: Wallet)

        fun onShowEmptyWalletsFlagChanged(showEmptyWallets: Boolean)

        fun onSortColumnChanged(sortColumn: WalletBalanceType)

    }


}