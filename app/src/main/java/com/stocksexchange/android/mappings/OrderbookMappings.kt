package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.android.database.model.DatabaseOrderbook
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters

fun Orderbook.mapToDatabaseOrderbook(
    parameters: OrderbookParameters
): DatabaseOrderbook {
    return DatabaseOrderbook(
        currencyPairId = parameters.currencyPairId,
        buyOrders = buyOrders,
        sellOrders = sellOrders
    )
}


fun DatabaseOrderbook.mapToOrderbook(): Orderbook {
    return Orderbook(
        buyOrders = buyOrders,
        sellOrders = sellOrders
    )
}