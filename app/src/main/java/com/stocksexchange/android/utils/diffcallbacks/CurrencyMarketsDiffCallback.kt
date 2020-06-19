package com.stocksexchange.android.utils.diffcallbacks

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketItem
import com.stocksexchange.android.utils.diffcallbacks.base.BaseDiffCallback

class CurrencyMarketsDiffCallback(
    oldList: List<CurrencyMarketItem>,
    newList: List<CurrencyMarketItem>
) : BaseDiffCallback<CurrencyMarket, CurrencyMarketItem>(oldList, newList) {


    override fun areItemsTheSame(oldItem: CurrencyMarket, newItem: CurrencyMarket): Boolean {
        return (oldItem.pairId == newItem.pairId)
    }


}