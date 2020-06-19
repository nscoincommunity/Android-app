package com.stocksexchange.android.model.comparators

import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketItem

class CurrencyMarketItemComparator(
    private val currencyMarketComparator: CurrencyMarketComparator
) : Comparator<CurrencyMarketItem> {


    override fun compare(o1: CurrencyMarketItem, o2: CurrencyMarketItem): Int {
        return currencyMarketComparator.compare(o1.itemModel, o2.itemModel)
    }


}