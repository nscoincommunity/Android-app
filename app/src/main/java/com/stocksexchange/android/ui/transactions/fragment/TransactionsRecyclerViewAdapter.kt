package com.stocksexchange.android.ui.transactions.fragment

import android.content.Context
import android.view.View
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter
import com.stocksexchange.api.model.rest.Transaction

class TransactionsRecyclerViewAdapter(
    context: Context,
    items: MutableList<TransactionItem>
) : TrackableRecyclerViewAdapter<Long, TransactionItem, TransactionItem.ViewHolder>(context, items) {


    private lateinit var mResources: TransactionResources

    var onTransactionAddressClickListener: ((View, TransactionItem, Int) -> Unit)? = null
    var onTransactionExplorerIdClickListener: ((View, TransactionItem, Int) -> Unit)? = null
    var onLeftActionButtonClickListener: ((View, TransactionItem, Int) -> Unit)? = null
    var onRightActionButtonClickListener: ((View, TransactionItem, Int) -> Unit)? = null




    override fun assignListeners(holder: TransactionItem.ViewHolder, position: Int, item: TransactionItem) {
        super.assignListeners(holder, position, item)

        with(item) {
            setOnTransactionAddressClickListener(holder, mResources, position, onTransactionAddressClickListener)
            setOnTransactionExplorerIdClickListener(holder, mResources, position, onTransactionExplorerIdClickListener)
            setOnLeftActionButtonClickListener(holder, position, onLeftActionButtonClickListener)
            setOnRightActionButtonClickListener(holder, position, onRightActionButtonClickListener)
        }
    }


    fun getItemPosition(item: Transaction): Int? {
        return items.indices.firstOrNull {
            items[it].itemModel.id == item.id
        }
    }


    fun setResources(resources: TransactionResources) {
        mResources = resources
    }


    override fun getResources(): TransactionResources? {
        return mResources
    }


}