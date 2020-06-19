package com.stocksexchange.android.utils.diffcallbacks

import com.stocksexchange.api.model.rest.Transaction
import com.stocksexchange.android.ui.transactions.fragment.TransactionItem
import com.stocksexchange.android.utils.diffcallbacks.base.BaseDiffCallback

class TransactionsDiffCallback(
    oldList: List<TransactionItem>,
    newList: List<TransactionItem>
) : BaseDiffCallback<Transaction, TransactionItem>(oldList, newList) {


    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return (oldItem.id == newItem.id)
    }


}