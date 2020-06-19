package com.stocksexchange.android.data.stores.tradehistory

import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.api.model.rest.parameters.TradeHistoryParameters
import com.stocksexchange.android.database.tables.TradeHistoryTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class TradeHistoryDatabaseDataStore(
    private val tradesTable: TradeHistoryTable
) : TradeHistoryDataStore {


    override suspend fun save(params: TradeHistoryParameters, trade: Trade) {
        executeBackgroundOperation {
            tradesTable.save(params, trade)
        }
    }


    override suspend fun save(params: TradeHistoryParameters, trades: List<Trade>) {
        executeBackgroundOperation {
            tradesTable.save(params, trades)
        }
    }


    override suspend fun delete(currencyPairId: Int) {
        executeBackgroundOperation {
            tradesTable.delete(currencyPairId)
        }
    }


    override suspend fun get(params: TradeHistoryParameters): Result<List<Trade>> {
        return performBackgroundOperation {
            tradesTable.get(params)
        }
    }


}