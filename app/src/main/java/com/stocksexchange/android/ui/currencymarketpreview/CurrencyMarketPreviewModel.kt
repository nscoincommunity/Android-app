package com.stocksexchange.android.ui.currencymarketpreview

import com.stocksexchange.android.data.repositories.candlesticks.CandleSticksRepository
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.data.repositories.notification.NotificationRepository
import com.stocksexchange.android.data.repositories.orderbooks.OrderbooksRepository
import com.stocksexchange.android.data.repositories.orders.OrdersRepository
import com.stocksexchange.android.data.repositories.settings.SettingsRepository
import com.stocksexchange.android.data.repositories.tradehistory.TradeHistoryRepository
import com.stocksexchange.android.mappings.mapToActiveOrdersList
import com.stocksexchange.android.mappings.mapToTradeHistoryList
import com.stocksexchange.android.model.CurrencyMarketPreviewDataSource
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.model.TradeData
import com.stocksexchange.android.ui.base.multipledataloading.BaseMultipleDataLoadingModel
import com.stocksexchange.android.ui.currencymarketpreview.CurrencyMarketPreviewModel.ActionListener
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters
import com.stocksexchange.api.model.rest.parameters.PriceChartDataParameters
import com.stocksexchange.api.model.rest.parameters.TradeHistoryParameters

class CurrencyMarketPreviewModel(
    private val settingsRepository: SettingsRepository,
    private val currencyMarketsRepository: CurrencyMarketsRepository,
    private val candleSticksRepository: CandleSticksRepository,
    private val orderbooksRepository: OrderbooksRepository,
    private val tradeHistoryRepository: TradeHistoryRepository,
    private val ordersRepository: OrdersRepository,
    private val notificationRepository: NotificationRepository
) : BaseMultipleDataLoadingModel<
    CurrencyMarketPreviewDataSource,
    ActionListener
    >() {


    companion object {

        const val REQUEST_TYPE_ORDER_RETRIEVAL = 0
        const val REQUEST_TYPE_ORDER_CANCELLATION = 1
        const val REQUEST_TYPE_NOTIFICATION_STATUS = 2

    }




    override fun getDataSourcesArray(): Array<CurrencyMarketPreviewDataSource> {
        return CurrencyMarketPreviewDataSource.values()
    }


    override suspend fun getRepositoryResult(parameters: Any, dataSource: CurrencyMarketPreviewDataSource): Any {
        return when(dataSource) {
            CurrencyMarketPreviewDataSource.PRICE_CHART -> {
                getCandleSticks(parameters as PriceChartDataParameters)
            }

            CurrencyMarketPreviewDataSource.DEPTH_CHART,
            CurrencyMarketPreviewDataSource.ORDERBOOK -> {
                getOrderbook(parameters as OrderbookParameters)
            }

            CurrencyMarketPreviewDataSource.TRADE_HISTORY -> {
                getTrades(parameters as TradeHistoryParameters)
            }

            CurrencyMarketPreviewDataSource.USER_ACTIVE_ORDERS -> {
                getUserActiveOrders(parameters as OrderParameters)
            }

            CurrencyMarketPreviewDataSource.USER_HISTORY_ORDERS -> {
                getUserHistoryOrders(parameters as OrderParameters)
            }
        }
    }


    private suspend fun getCandleSticks(params: PriceChartDataParameters): Any {
        return candleSticksRepository.get(params).apply {
            log("candleSticksRepository.get(params: $params)")
        }
    }


    private suspend fun getOrderbook(params: OrderbookParameters): Any {
        return orderbooksRepository.get(params).apply {
            log("orderbooksRepository.get(params: $params)")
        }
    }


    private suspend fun getTrades(params: TradeHistoryParameters): Any {
        return tradeHistoryRepository.get(params).apply {
            log("tradeHistoryRepository.get(params: $params)")
        }
    }


    private suspend fun getUserActiveOrders(params: OrderParameters): Any {
        return ordersRepository.getActiveOrders(params).apply {
            log("ordersRepository.getActiveOrders(params: $params)")
        }.mapToActiveOrdersRepositoryResult()
    }


    private suspend fun getUserHistoryOrders(params: OrderParameters): Any {
        return ordersRepository.getHistoryOrders(params).apply {
            log("ordersRepository.getHistoryOrders(params: $params)")
        }.mapToTradeHistoryRepositoryResult()
    }


    private fun RepositoryResult<List<Order>>.mapToTradeHistoryRepositoryResult(): RepositoryResult<List<Trade>> {
        if(isErroneous()) {
            return RepositoryResult.newErroneousInstance(this)
        }

        val orders = getSuccessfulResultValue()
        val trades = orders.mapToTradeHistoryList()

        return RepositoryResult.newSuccessfulInstance(
            successfulResult = this,
            successfulValue = trades
        )
    }


    private fun RepositoryResult<List<Order>>.mapToActiveOrdersRepositoryResult(): RepositoryResult<List<Trade>> {
        if(isErroneous()) {
            return RepositoryResult.newErroneousInstance(this)
        }

        val orders = getSuccessfulResultValue()
        val trades = orders.mapToActiveOrdersList()

        return RepositoryResult.newSuccessfulInstance(
            successfulResult = this,
            successfulValue = trades
        )
    }


    override suspend fun refreshData(parameters: Any, dataSource: CurrencyMarketPreviewDataSource) {
        when(dataSource) {

            CurrencyMarketPreviewDataSource.PRICE_CHART -> {
                candleSticksRepository.refresh(parameters as PriceChartDataParameters)
            }

            CurrencyMarketPreviewDataSource.DEPTH_CHART,
            CurrencyMarketPreviewDataSource.ORDERBOOK -> {
                orderbooksRepository.refresh(parameters as OrderbookParameters)
            }

            CurrencyMarketPreviewDataSource.TRADE_HISTORY -> {
                tradeHistoryRepository.refresh(parameters as TradeHistoryParameters)
            }

            CurrencyMarketPreviewDataSource.USER_ACTIVE_ORDERS,
            CurrencyMarketPreviewDataSource.USER_HISTORY_ORDERS -> {
                ordersRepository.refresh(parameters as OrderParameters)
            }

        }
    }


    fun performOrderRetrievalRequest(orderId: Long, metadata: TradeData) {
        performRequest(
            requestType = REQUEST_TYPE_ORDER_RETRIEVAL,
            params = orderId,
            metadata = metadata
        )
    }


    fun performOrderCancellationRequest(order: Order, metadata: Pair<Order, TradeData>) {
        performRequest(
            requestType = REQUEST_TYPE_ORDER_CANCELLATION,
            params = order,
            metadata = metadata
        )
    }


    fun updateNotificationStatus(status: NotificationStatus) {
        performRequest(
            requestType = REQUEST_TYPE_NOTIFICATION_STATUS,
            params = status
        )
    }


    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when(requestType) {

            REQUEST_TYPE_ORDER_RETRIEVAL -> {
                ordersRepository.getOrder(params as Long).apply {
                    log("ordersRepository.getOrder(params: $params)")
                }
            }

            REQUEST_TYPE_ORDER_CANCELLATION -> {
                ordersRepository.cancel(params as Order).apply {
                    log("ordersRepository.cancel(order: $params)")
                }
            }

            REQUEST_TYPE_NOTIFICATION_STATUS -> {
                notificationRepository.updateNotificationStatus(params as NotificationStatus).apply {
                    log("updateNotificationStatus(params: $params)")
                }
            }

            else -> throw IllegalStateException()

        }
    }


    fun toggleChartsVisibilitySettingOption(settings: Settings) {
        createUiLaunchCoroutine {
            if(settings.areMarketPreviewChartsVisible) {
                disableChartsVisibilitySettingOption(settings)
            } else {
                enableChartsVisibilitySettingOption(settings)
            }
        }
    }


    private suspend fun disableChartsVisibilitySettingOption(settings: Settings) {
        val updatedSettings = settings.copy(areMarketPreviewChartsVisible = false)

        settingsRepository.save(updatedSettings)
        mActionListener?.onChartsVisibilitySettingDisabled(updatedSettings)
    }


    private suspend fun enableChartsVisibilitySettingOption(settings: Settings) {
        val updatedSettings = settings.copy(areMarketPreviewChartsVisible = true)

        settingsRepository.save(updatedSettings)
        mActionListener?.onChartsVisibilitySettingEnabled(updatedSettings)
    }


    fun toggleFavoriteState(currencyMarket: CurrencyMarket) {
        createUiLaunchCoroutine {
            if(currencyMarketsRepository.isCurrencyMarketFavorite(currencyMarket)) {
                unfavoriteCurrencyMarket(currencyMarket)
            } else {
                favoriteCurrencyMarket(currencyMarket)
            }
        }
    }


    private suspend fun unfavoriteCurrencyMarket(currencyMarket: CurrencyMarket) {
        currencyMarketsRepository.unfavorite(currencyMarket)

        mActionListener?.onCurrencyMarketUnfavorited(currencyMarket)
    }


    private suspend fun favoriteCurrencyMarket(currencyMarket: CurrencyMarket) {
        currencyMarketsRepository.favorite(currencyMarket)

        mActionListener?.onCurrencyMarketFavorited(currencyMarket)
    }


    fun fetchOrder(id: Long, onFinish: ((RepositoryResult<Order>) -> Unit)) {
        createUiLaunchCoroutine {
            onFinish(ordersRepository.getOrder(id))
        }
    }


    fun updateSettings(settings: Settings, onFinish: (() -> Unit)) {
        createUiLaunchCoroutine {
            settingsRepository.save(settings)

            onFinish()
        }
    }


    interface ActionListener : BaseMultipleDataLoadingActionListener<CurrencyMarketPreviewDataSource> {

        fun onChartsVisibilitySettingEnabled(settings: Settings)

        fun onChartsVisibilitySettingDisabled(settings: Settings)

        fun onCurrencyMarketFavorited(currencyMarket: CurrencyMarket)

        fun onCurrencyMarketUnfavorited(currencyMarket: CurrencyMarket)

    }


}