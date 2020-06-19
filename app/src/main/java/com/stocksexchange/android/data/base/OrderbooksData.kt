package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters

interface OrderbooksData<
    OrderbookFetchingResult
> {

    suspend fun save(params: OrderbookParameters, orderbook: Orderbook)

    suspend fun get(params: OrderbookParameters): OrderbookFetchingResult

}