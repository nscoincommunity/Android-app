package com.stocksexchange.android.ui.currencymarkets.fragment

import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.TradeType
import com.stocksexchange.android.ui.base.listdataloading.ListDataLoadingView

interface CurrencyMarketsContract {


    interface View : ListDataLoadingView<List<CurrencyMarket>> {

        fun sortDataSet(comparator: CurrencyMarketComparator)

        fun setItems(items: List<CurrencyMarket>, notifyAboutChange: Boolean)

        fun setItemsAndSort(items: List<CurrencyMarket>, comparator: CurrencyMarketComparator?)

        fun updateItemWith(item: CurrencyMarket, position: Int)

        fun navigateToCurrencyMarketPreviewScreen(currencyMarket: CurrencyMarket)

        fun navigateToTradeScreen(tradeType: TradeType, currencyMarket: CurrencyMarket,
                                  selectedPrice: Double = 0.0)

        fun addCurrencyMarketChronologically(currencyMarket: CurrencyMarket, comparator: CurrencyMarketComparator?)

        fun deleteCurrencyMarket(currencyMarket: CurrencyMarket)

        fun containsCurrencyMarket(currencyMarket: CurrencyMarket): Boolean

        fun getDataSetPositionForPairId(id: Int): Int?

        fun getItem(position: Int): CurrencyMarket?

    }


    interface ActionListener {

        fun onCurrencyMarketItemClicked(currencyMarket: CurrencyMarket)

        fun onCurrencyMarketItemLongClicked(currencyMarket: CurrencyMarket)

        fun onSortPayloadChanged(payload: Any)

    }


}