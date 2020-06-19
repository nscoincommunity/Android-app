package com.stocksexchange.android.di

import com.stocksexchange.android.di.utils.get
import com.stocksexchange.android.di.utils.single
import com.stocksexchange.android.socket.SocketConnection
import com.stocksexchange.android.socket.handlers.*
import com.stocksexchange.android.socket.handlers.base.Handler
import com.stocksexchange.android.socket.model.enums.SocketEvent
import org.koin.android.ext.koin.androidApplication
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

const val BEST_PRICE_UPDATE_HANDLER = "best_price_update_handler"
const val INBOX_COUNT_ITEM_UPDATE_HANDLER = "inbox_count_item_update_handler"
const val INBOX_NEW_MESSAGE_UPDATE_HANDLER = "inbox_new_message_update_handler"
const val ORDERBOOK_ORDER_UPDATE_HANDLER = "orderbook_order_update_handler"
const val PRICE_CHART_DATA_UPDATE_HANDLER = "price_chart_data_update_handler"
const val TICKER_ITEM_UPDATE_HANDLER = "ticker_item_update_handler"
const val TRADE_HISTORY_ITEM_CREATION_HANDLER = "trade_history_item_creation_handler"
const val USER_ACTIVE_ORDER_FILLS_UPDATE_HANDLER = "user_active_order_fills_update_handler"
const val USER_ACTIVE_ORDER_STATUS_UPDATE_HANDLER = "user_active_order_status_update_handler"
const val WALLET_BALANCE_UPDATE_HANDLER = "wallet_balance_update_handler"

val socketModule = module {

    single<Handler>(BEST_PRICE_UPDATE_HANDLER) { BestPriceUpdateHandler(get(), get()) }
    single<Handler>(INBOX_COUNT_ITEM_UPDATE_HANDLER) { InboxCountItemUpdateHandler(get(), get()) }
    single<Handler>(INBOX_NEW_MESSAGE_UPDATE_HANDLER) { InboxNewMessageUpdateHandler(get()) }
    single<Handler>(ORDERBOOK_ORDER_UPDATE_HANDLER) { OrderbookOrderUpdateHandler(get(), get()) }
    single<Handler>(PRICE_CHART_DATA_UPDATE_HANDLER) { PriceChartDataUpdateHandler(get(), get()) }
    single<Handler>(TICKER_ITEM_UPDATE_HANDLER) { TickerItemUpdateHandler(get(), get()) }
    single<Handler>(TRADE_HISTORY_ITEM_CREATION_HANDLER) { TradeHistoryItemCreationHandler(get(), get()) }
    single<Handler>(USER_ACTIVE_ORDER_FILLS_UPDATE_HANDLER) { UserActiveOrderFillsUpdateHandler(get(), get(), get()) }
    single<Handler>(USER_ACTIVE_ORDER_STATUS_UPDATE_HANDLER) { UserActiveOrderStatusUpdateHandler(get(), get(), get()) }
    single<Handler>(WALLET_BALANCE_UPDATE_HANDLER) { WalletBalanceUpdateHandler(get(), get()) }

    factory<Handler> { (socketEvent: SocketEvent) ->
        when(socketEvent) {
            SocketEvent.TICKER_ITEM_UPDATED -> get(TICKER_ITEM_UPDATE_HANDLER)
            SocketEvent.PRICE_CHART_DATA_UPDATED -> get(PRICE_CHART_DATA_UPDATE_HANDLER)
            SocketEvent.ORDERBOOK_ORDER_UPDATED -> get(ORDERBOOK_ORDER_UPDATE_HANDLER)
            SocketEvent.TRADE_HISTORY_ITEM_CREATED -> get(TRADE_HISTORY_ITEM_CREATION_HANDLER)
            SocketEvent.BEST_PRICE_UPDATED -> get(BEST_PRICE_UPDATE_HANDLER)
            SocketEvent.WALLET_BALANCE_UPDATED -> get(WALLET_BALANCE_UPDATE_HANDLER)
            SocketEvent.USER_ACTIVE_ORDER_FILLS_UPDATED -> get(USER_ACTIVE_ORDER_FILLS_UPDATE_HANDLER)
            SocketEvent.USER_ACTIVE_ORDER_STATUS_UPDATED -> get(USER_ACTIVE_ORDER_STATUS_UPDATE_HANDLER)
            SocketEvent.INBOX_COUNT_UPDATE -> get(INBOX_COUNT_ITEM_UPDATE_HANDLER)
            SocketEvent.INBOX_NEW_MESSAGE -> get(INBOX_NEW_MESSAGE_UPDATE_HANDLER)
        }
    }

    single<Map<SocketEvent, Handler>> {
        mutableMapOf<SocketEvent, Handler>().apply {
            for(socketEvent in SocketEvent.values()) {
                put(socketEvent, this@single.get { parametersOf(socketEvent) })
            }
        }
    }

    single {
        SocketConnection(
            application = androidApplication(),
            credentialsHandler = get(),
            handlersMap = get()
        )
    }

}