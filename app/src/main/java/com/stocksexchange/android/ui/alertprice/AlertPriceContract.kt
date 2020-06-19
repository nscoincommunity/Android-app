package com.stocksexchange.android.ui.alertprice

import com.stocksexchange.api.model.rest.AlertPrice
import com.stocksexchange.android.ui.base.listdataloading.ListDataLoadingView
import com.stocksexchange.api.model.rest.CurrencyMarket

interface AlertPriceContract {


    interface View : ListDataLoadingView<List<AlertPrice>> {

        fun deleteItem(item: AlertPrice)

        fun getItemPosition(item: AlertPrice): Int?

        fun hideToolbarProgressBar()

        fun showToolbarProgressBar()

        fun navigateToCurrencyMarketPreviewScreen(currencyMarket: CurrencyMarket)

    }


    interface ActionListener {

        fun onItemMoreDeleted(item: AlertPairItem)

        fun onItemLessDeleted(item: AlertPairItem)

        fun onItemClick(pairId: Int)

    }


}