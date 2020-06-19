package com.stocksexchange.android.ui.currencymarketpreview

import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.base.multipledataloading.MultipleDataLoadingView
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.PriceChartDataInterval
import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.api.model.rest.TradeType

interface CurrencyMarketPreviewContract {


    interface View : MultipleDataLoadingView<CurrencyMarketPreviewDataSource> {

        fun showChartsContainer()

        fun hideChartsContainer()

        fun showView(dataSource: CurrencyMarketPreviewDataSource)

        fun hideView(dataSource: CurrencyMarketPreviewDataSource)

        fun showUserActiveOrdersViewItemProgressBar(item: TradeData)

        fun hideUserActiveOrdersViewItemProgressBar(item: TradeData)

        fun showOrderCancellationTip()

        fun enableScrollViewScrolling()

        fun disableScrollViewScrolling()

        fun updateInboxButtonItemCount()

        fun updateChartsVisibilityButtonState(isVisible: Boolean)

        fun updateFavoriteButtonState(isFavorite: Boolean)

        fun addUserOrderItem(dataSource: CurrencyMarketPreviewDataSource, item: Trade)

        fun deleteUserOrderItem(dataSource: CurrencyMarketPreviewDataSource, item: Trade)

        fun navigateToInboxScreen()

        fun navigateToPriceAlertCreationScreen(currencyMarket: CurrencyMarket)

        fun navigateToOrderbookScreen(currencyMarket: CurrencyMarket)

        fun navigateToNextScreen(tradeType: TradeType, currencyMarket: CurrencyMarket,
                                 selectedPrice: Double, selectedAmount: Double)

        fun setPriceInfoViewData(currencyMarket: CurrencyMarket, animate: Boolean)

        fun isScrollViewAtTheBottom(): Boolean

        fun containsUserOrder(dataSource: CurrencyMarketPreviewDataSource, trade: Trade): Boolean

        fun getSelectedAmount(orderbookOrderData: OrderbookOrderData): Double

        fun hideRefreshProgressBar()

    }


    interface ActionListener {

        fun onPreRightButtonClicked()

        fun onRightButtonClicked()

        fun onInboxButtonClicked()

        fun onTopTabSelected(position: Int)

        fun onChartPressed()

        fun onChartReleased()

        fun onPriceChartDataIntervalChanged(oldInterval: PriceChartDataInterval,
                                            newInterval: PriceChartDataInterval)

        fun onDepthChartTabSelected(tab: DepthChartTab)

        fun onBottomTabSelected(position: Int)

        fun onOrderbookHeaderMoreButtonClicked()

        fun onOrderbookOrderItemClicked(orderbookOrderData: OrderbookOrderData)

        fun onActiveOrderCancelButtonClicked(tradeData: TradeData)

        fun onTradeButtonClicked(tradeType: TradeType)

        fun onAlertPriceButtonClicked()

    }


}