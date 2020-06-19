package com.stocksexchange.android.socket.model.enums

enum class SocketEvent(val eventName: String) {

    TICKER_ITEM_UPDATED("App\\Events\\Ticker"),
    PRICE_CHART_DATA_UPDATED("App\\Events\\CandleChanged"),
    ORDERBOOK_ORDER_UPDATED("App\\Events\\GlassRowChanged"),
    TRADE_HISTORY_ITEM_CREATED("App\\Events\\OrderFillCreated"),
    BEST_PRICE_UPDATED("App\\Events\\BestPriceChanged"),
    WALLET_BALANCE_UPDATED("App\\Events\\BalanceChanged"),
    USER_ACTIVE_ORDER_FILLS_UPDATED("App\\Events\\UserOrder"),
    USER_ACTIVE_ORDER_STATUS_UPDATED("App\\Events\\UserOrderDeleted"),
    INBOX_COUNT_UPDATE("App\\Events\\NotifyCount"),
    INBOX_NEW_MESSAGE("App\\Events\\Notify")
}