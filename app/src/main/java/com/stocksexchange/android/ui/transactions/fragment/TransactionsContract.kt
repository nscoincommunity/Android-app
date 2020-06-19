package com.stocksexchange.android.ui.transactions.fragment

import com.stocksexchange.api.model.rest.Transaction
import com.stocksexchange.android.ui.base.listdataloading.ListDataLoadingView

interface TransactionsContract {


    interface View : ListDataLoadingView<List<Transaction>> {

        fun showSecondaryProgressBar()

        fun hideSecondaryProgressBar()

        fun updateItem(position: Int, item: Transaction)

        fun launchBrowser(url: String)

        fun getItemPosition(item: Transaction): Int?

        fun postActionDelayed(delay: Long, action: () -> Unit)

        fun clearAdapter()

    }


    interface ActionListener {

        fun onTransactionAddressClicked(transaction: Transaction)

        fun onTransactionExplorerIdClicked(transaction: Transaction)

        fun onLeftActionButtonClicked(transaction: Transaction)

        fun onRightActionButtonClicked(transaction: Transaction)

    }


}