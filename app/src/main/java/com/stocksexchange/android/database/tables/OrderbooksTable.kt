package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.android.database.daos.OrderbookDao
import com.stocksexchange.android.database.model.DatabaseOrderbook
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.mappings.mapToDatabaseOrderbook
import com.stocksexchange.android.mappings.mapToOrderbook
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters
import com.stocksexchange.core.model.Result

class OrderbooksTable(
    private val orderbookDao: OrderbookDao
) : BaseTable() {


    @Synchronized
    fun save(params: OrderbookParameters,
             orderbook: Orderbook) {
        orderbookDao.insert(orderbook.mapToDatabaseOrderbook(params))
    }


    @Synchronized
    fun get(params: OrderbookParameters): Result<Orderbook> {
        return orderbookDao.get(currencyPairId = params.currencyPairId).toResult(
            precondition = { !it.isEmpty },
            convert = DatabaseOrderbook::mapToOrderbook
        )
    }


}