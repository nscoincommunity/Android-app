package com.stocksexchange.android.data.stores.orderbooks

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class OrderbooksServerDataStore(
    private val stexRestApi: StexRestApi
) : OrderbooksDataStore {


    override suspend fun save(params: OrderbookParameters, orderbook: Orderbook) {
        throw UnsupportedOperationException()
    }


    override suspend fun get(params: OrderbookParameters): Result<Orderbook> {
        return performBackgroundOperation {
            stexRestApi.getOrderbook(params)
        }
    }


}