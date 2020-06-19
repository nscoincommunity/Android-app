package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.Transaction
import com.stocksexchange.android.ui.transactions.fragment.TransactionItem

fun Transaction.mapToTransactionItem(): TransactionItem {
    return TransactionItem(this)
}


fun List<Transaction>.mapToTransactionItemList(): List<TransactionItem> {
    return map { it.mapToTransactionItem() }
}