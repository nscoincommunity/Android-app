package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.android.database.model.DatabaseTrade
import com.stocksexchange.android.model.DataActionItem
import com.stocksexchange.api.model.rest.parameters.TradeHistoryParameters

fun Trade.mapToDatabaseTrade(
    parameters: TradeHistoryParameters
): DatabaseTrade {
    return DatabaseTrade(
        id = id,
        currencyPairId = parameters.currencyPairId,
        price = price,
        amount = amount,
        typeStr = typeStr,
        timestamp = timestamp
    )
}


fun List<Trade>.mapToDatabaseTradeList(
    parameters: TradeHistoryParameters
): List<DatabaseTrade> {
    return map { it.mapToDatabaseTrade(parameters) }
}


fun DatabaseTrade.mapToTrade(): Trade {
    return Trade(
        id = id,
        price = price,
        amount = amount,
        typeStr = typeStr,
        timestamp = timestamp
    )
}


fun List<DatabaseTrade>.mapToTradeList(): List<Trade> {
    return map { it.mapToTrade() }
}


fun List<DataActionItem<Trade>>.mapToIdTradeActionItemsMap(): Map<Long, DataActionItem<Trade>> {
    return mutableMapOf<Long, DataActionItem<Trade>>().apply {
        for(item in this@mapToIdTradeActionItemsMap) {
            put(item.dataItem.id, item)
        }
    }
}