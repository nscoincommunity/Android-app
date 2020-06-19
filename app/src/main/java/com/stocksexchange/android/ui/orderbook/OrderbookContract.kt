package com.stocksexchange.android.ui.orderbook

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.model.DataActionItem
import com.stocksexchange.android.model.OrderbookInfo
import com.stocksexchange.android.model.OrderbookOrderData
import com.stocksexchange.android.ui.base.mvp.views.BaseView
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters

interface OrderbookContract {


    interface View : BaseView {

        fun showAppBar(animate: Boolean)

        fun enableAppBarScrolling()

        fun disableAppBarScrolling()

        fun showProgressBar()

        fun hideProgressBar()

        fun showMainView()

        fun hideMainView()

        fun showEmptyView(orderbookDetailsCaption: String, orderbookViewCaption: String)

        fun showErrorView(orderbookDetailsCaption: String, orderbookViewCaption: String)

        fun hideInfoView()

        fun setData(info: OrderbookInfo, orderbook: Orderbook, shouldBindData: Boolean)

        fun updateData(info: OrderbookInfo, orderbook: Orderbook,
                       dataActionItems: List<DataActionItem<OrderbookOrder>>)

        fun bindData()

        fun scrollOrderbookViewToTop()

        fun navigateToNextScreen(tradeType: TradeType, currencyMarket: CurrencyMarket,
                                 selectedPrice: Double, selectedAmount: Double)

        fun isOrderbookEmpty(): Boolean

        fun getOrderbookParameters(): OrderbookParameters

        fun getSelectedAmount(orderbookOrderData: OrderbookOrderData): Double

    }


    interface ActionListener {

        fun onOrderbookOrderItemClicked(orderbookOrderData: OrderbookOrderData)

    }


}