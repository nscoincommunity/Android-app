package com.stocksexchange.android.ui.wallets.fragment

import android.content.Context
import android.view.View
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter

class WalletsRecyclerViewAdapter(
    context: Context,
    items: MutableList<WalletItem>
) : TrackableRecyclerViewAdapter<String, WalletItem, WalletItem.ViewHolder>(context, items) {


    private lateinit var mResources: WalletsResources

    var onCurrencyNameClickListener: ((View, WalletItem, Int) -> Unit)? = null
    var onDepositButtonClickListener: ((View, WalletItem, Int) -> Unit)? = null
    var onWithdrawButtonClickListener: ((View, WalletItem, Int) -> Unit)? = null




    override fun assignListeners(holder: WalletItem.ViewHolder, position: Int, item: WalletItem) {
        super.assignListeners(holder, position, item)

        with(item) {
            setOnCurrencyNameClickListener(holder, position, onCurrencyNameClickListener)
            setOnDepositButtonClickListener(holder, position, onDepositButtonClickListener)
            setOnWithdrawButtonClickListener(holder, position, onWithdrawButtonClickListener)
        }

    }


    /**
     * Retrieves a data set index of the wallet with the specified
     * currency ID or null if the wallet with such currency ID is absent.
     *
     * @param currencyId The currency ID of the wallet to fetch a
     * data set index of
     *
     * @return The data set index of the wallet or null if it is absent
     */
    fun getDataSetIndexForCurrencyId(currencyId: Int): Int? {
        return items.indices.firstOrNull {
            items[it].itemModel.currencyId == currencyId
        }
    }


    fun setResources(resources: WalletsResources) {
        mResources = resources
    }


    override fun getResources(): WalletsResources? {
        return mResources
    }


}