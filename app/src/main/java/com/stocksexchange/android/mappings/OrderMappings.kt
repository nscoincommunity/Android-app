package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.Order
import com.stocksexchange.android.database.model.DatabaseOrder
import com.stocksexchange.api.model.rest.ApiOrderType
import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.api.model.rest.TradeType

fun Order.mapToDatabaseOrder(): DatabaseOrder {
    return DatabaseOrder(
        id = id,
        currencyPairId = currencyPairId,
        currencyPairName = currencyPairName,
        price = price,
        triggerPrice = triggerPrice,
        initialAmount = initialAmount,
        filledAmount = filledAmount,
        typeStr = typeStr,
        originalTypeStr = originalTypeStr,
        timestamp = timestamp,
        statusStr = statusStr,
        trades = trades,
        fees = fees
    )
}


fun Order.mapToTrade(): Trade {
    return Trade(
        id = id,
        price = price,
        amount = filledAmount,
        typeStr = when(type) {
            ApiOrderType.BUY,
            ApiOrderType.BUY_STOP_LIMIT,
            ApiOrderType.UNKNOWN -> TradeType.BUY.name

            ApiOrderType.SELL,
            ApiOrderType.SELL_STOP_LIMIT -> TradeType.SELL.name
        },
        timestamp = timestamp
    )
}


fun Order.mapToTradeHistory(): Trade {
    return Trade(
        id = id,
        price = price,
        amount = filledAmount,
        typeStr = when(type) {
            ApiOrderType.BUY,
            ApiOrderType.BUY_STOP_LIMIT,
            ApiOrderType.UNKNOWN -> TradeType.BUY.name

            ApiOrderType.SELL,
            ApiOrderType.SELL_STOP_LIMIT -> TradeType.SELL.name
        },
        timestamp = timestamp
    )
}


fun Order.mapToActiveOrders(): Trade {
    return Trade(
        id = id,
        price = price,
        amount = initialAmount - filledAmount,
        typeStr = when(type) {
            ApiOrderType.BUY,
            ApiOrderType.BUY_STOP_LIMIT,
            ApiOrderType.UNKNOWN -> TradeType.BUY.name

            ApiOrderType.SELL,
            ApiOrderType.SELL_STOP_LIMIT -> TradeType.SELL.name
        },
        timestamp = timestamp
    )
}


fun List<Order>.mapToDatabaseOrderList(): List<DatabaseOrder> {
    return map { it.mapToDatabaseOrder() }
}


fun List<Order>.mapToTradeHistoryList(): List<Trade> {
    return map { it.mapToTradeHistory() }
}

fun List<Order>.mapToActiveOrdersList(): List<Trade> {
    return map { it.mapToActiveOrders() }
}


fun DatabaseOrder.mapToOrder(): Order {
    return Order(
        id = id,
        currencyPairId = currencyPairId,
        currencyPairName = currencyPairName,
        price = price,
        triggerPrice = triggerPrice,
        initialAmount = initialAmount,
        filledAmount = filledAmount,
        typeStr = typeStr,
        originalTypeStr = originalTypeStr,
        timestamp = timestamp,
        statusStr = statusStr,
        _trades = trades,
        _fees = fees
    )
}


fun List<DatabaseOrder>.mapToOrderList(): List<Order> {
    return map { it.mapToOrder() }
}