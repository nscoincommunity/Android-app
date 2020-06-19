package com.stocksexchange.android.socket.model

/**
 * A model class containing socket channels related
 * functionality.
 */
object SocketChannels {


    private const val PRIVATE_CHANNEL_PREFIX = "private"

    private const val CHANNEL_TICKER_UPDATES = "rate"

    private const val CHANNEL_PREFIX_PRICE_CHART_DATA_UPDATES = "stats_data_"
    private const val CHANNEL_PREFIX_ORDERBOOK_BID_ORDERS_UPDATES = "buy_data"
    private const val CHANNEL_PREFIX_ORDERBOOK_ASK_ORDERS_UPDATES = "sell_data"
    private const val CHANNEL_PREFIX_TRADE_HISTORY_ITEMS_CREATION = "trade_c"
    private const val CHANNEL_PREFIX_BEST_BID_PRICE_UPDATES = "best_bid_price_"
    private const val CHANNEL_PREFIX_BEST_ASK_PRICE_UPDATES = "best_ask_price_"
    private const val CHANNEL_PREFIX_WALLET_BALANCE_UPDATES = "private-balance_changed_w_"

    private const val CHANNEL_USER_BUY_ACTIVE_ORDERS_FILLS_UPDATES_TEMPLATE = "private-buy_user_data_u%sc%s"
    private const val CHANNEL_USER_SELL_ACTIVE_ORDERS_FILLS_UPDATES_TEMPLATE = "private-sell_user_data_u%sc%s"

    private const val CHANNEL_USER_ACTIVE_ORDERS_STATUSES_UPDATES_TEMPLATE = "private-del_order_u%sc%s"

    private const val CHANNEL_INBOX_COUNT = "private-notify_user_%s_about_count"
    private const val CHANNEL_INBOX_NEW_MESSAGE = "private-notify_user_%s"



    /**
     * Parses the price chart data updates channel and retrieves an
     * interval name from it.
     *
     * @param channel The channel to parse
     *
     * @return The interval name
     */
    fun parsePriceChartIntervalName(channel: String): String {
        return channel.removePrefix(CHANNEL_PREFIX_PRICE_CHART_DATA_UPDATES).substringBefore("_")
    }


    fun isPriceChartDataUpdatesChannel(channel: String, currencyPairId: String): Boolean {
        return (channel.startsWith(CHANNEL_PREFIX_PRICE_CHART_DATA_UPDATES) && channel.endsWith(currencyPairId))
    }


    fun isOrderbookBidOrdersUpdatesChannel(channel: String): Boolean {
        return channel.startsWith(CHANNEL_PREFIX_ORDERBOOK_BID_ORDERS_UPDATES)
    }


    fun isOrderbookAskOrdersUpdatesChannel(channel: String): Boolean {
        return channel.startsWith(CHANNEL_PREFIX_ORDERBOOK_ASK_ORDERS_UPDATES)
    }


    fun isPrivateChannel(channel: String): Boolean {
        return channel.startsWith(PRIVATE_CHANNEL_PREFIX)
    }


    fun getTickerUpdatesChannel(): String {
        return CHANNEL_TICKER_UPDATES
    }


    fun getPriceChartDataUpdatesChannel(intervalName: String, currencyPairId: String): String {
        return (CHANNEL_PREFIX_PRICE_CHART_DATA_UPDATES + intervalName + "_" + currencyPairId)
    }


    fun getOrderbookBidOrdersUpdatesChannel(currencyPairId: String): String {
        return (CHANNEL_PREFIX_ORDERBOOK_BID_ORDERS_UPDATES + currencyPairId)
    }


    fun getOrderbookAskOrdersUpdatesChannel(currencyPairId: String): String {
        return (CHANNEL_PREFIX_ORDERBOOK_ASK_ORDERS_UPDATES + currencyPairId)
    }


    fun getTradeHistoryItemsCreationChannel(currencyPairId: String): String {
        return (CHANNEL_PREFIX_TRADE_HISTORY_ITEMS_CREATION + currencyPairId)
    }


    fun geBestBidPriceUpdatesChannel(currencyPairId: String): String {
        return (CHANNEL_PREFIX_BEST_BID_PRICE_UPDATES + currencyPairId)
    }


    fun getBestAskPriceUpdatesChannel(currencyPairId: String): String {
        return (CHANNEL_PREFIX_BEST_ASK_PRICE_UPDATES + currencyPairId)
    }


    fun getWalletBalanceUpdatesChannel(walletId: String): String {
        return (CHANNEL_PREFIX_WALLET_BALANCE_UPDATES + walletId)
    }


    fun getUserBuyActiveOrdersFillsUpdatesChannel(userId: String, currencyPairId: String): String {
        return String.format(CHANNEL_USER_BUY_ACTIVE_ORDERS_FILLS_UPDATES_TEMPLATE, userId, currencyPairId)
    }


    fun getUserSellActiveOrdersFillsUpdatesChannel(userId: String, currencyPairId: String): String {
        return String.format(CHANNEL_USER_SELL_ACTIVE_ORDERS_FILLS_UPDATES_TEMPLATE, userId, currencyPairId)
    }


    fun getUserActiveOrdersStatusesUpdatesChannel(userId: String, currencyPairId: String): String {
        return String.format(CHANNEL_USER_ACTIVE_ORDERS_STATUSES_UPDATES_TEMPLATE, userId, currencyPairId)
    }


    fun getUserInboxCountChannel(userId: String): String {
        return String.format(CHANNEL_INBOX_COUNT, userId)
    }


    fun getUserInboxNewMessage(userId: String): String {
        return String.format(CHANNEL_INBOX_NEW_MESSAGE, userId)
    }


}