package com.stocksexchange.android.data.stores.orderbooks

import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters
import com.stocksexchange.android.database.tables.OrderbooksTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class OrderbooksDatabaseDataStore(
    private val orderbooksTable: OrderbooksTable
) : OrderbooksDataStore {


    override suspend fun save(params: OrderbookParameters, orderbook: Orderbook) {
        executeBackgroundOperation {
            orderbooksTable.save(params, orderbook)
        }
    }


    override suspend fun get(params: OrderbookParameters): Result<Orderbook> {
        return performBackgroundOperation {
            orderbooksTable.get(params)
        }
    }


}