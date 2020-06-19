package com.stocksexchange.android.model

enum class CurrencyMarketPreviewDataSource(
    val isChartView: Boolean = false,
    val isTradingView: Boolean = false
) {

    PRICE_CHART(isChartView = true),
    DEPTH_CHART(isChartView = true),
    ORDERBOOK(isTradingView = true),
    TRADE_HISTORY(isTradingView = true),
    USER_ACTIVE_ORDERS(isTradingView = true),
    USER_HISTORY_ORDERS(isTradingView = true)

}