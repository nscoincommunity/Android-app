package com.stocksexchange.android.ui.transactioncreation.base

import com.stocksexchange.android.model.TransactionData
import com.stocksexchange.android.ui.base.dataloading.DataLoadingView

/**
 * A view of the MVP architecture that contains functionality
 * for creating transactions (deposits and withdrawals).
 */
interface TransactionCreationView : DataLoadingView<TransactionData> {


    fun showToolbarProgressBar()


    fun hideToolbarProgressBar()


}