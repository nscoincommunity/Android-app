package com.stocksexchange.android.ui.orders.fragment

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.android.model.OrderData
import com.stocksexchange.android.ui.base.listdataloading.ListDataLoadingView
import com.stocksexchange.api.model.rest.SortOrder

interface OrdersContract {


    interface View : ListDataLoadingView<List<OrderData>> {

        fun showSecondaryProgressBar()

        fun hideSecondaryProgressBar()

        fun addOrderChronologically(orderData: OrderData, sortOrder: SortOrder)

        fun updateOrder(position: Int, orderData: OrderData)

        fun containsOrder(orderData: OrderData): Boolean

        fun deleteOrder(orderData: OrderData)

        fun launchBrowser(url: String)

        fun navigateToCurrencyMarketPreviewScreen(currencyMarket: CurrencyMarket)

        fun getOrderPosition(orderData: OrderData): Int?

        fun postActionDelayed(delay: Long, action: () -> Unit)

        fun clearAdapter()

    }


    interface ActionListener {

        fun onMarketNameClicked(currencyMarket: CurrencyMarket)

        fun onCancelButtonClicked(orderData: OrderData)

    }


}