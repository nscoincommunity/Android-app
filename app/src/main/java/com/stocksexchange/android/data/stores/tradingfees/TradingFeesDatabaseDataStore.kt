package com.stocksexchange.android.data.stores.tradingfees

import com.stocksexchange.api.model.rest.TradingFees
import com.stocksexchange.api.model.rest.parameters.TradingFeesParameters
import com.stocksexchange.android.database.tables.TradingFeesTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class TradingFeesDatabaseDataStore(
    private val tradingFeesTable: TradingFeesTable
) : TradingFeesDataStore {


    override suspend fun save(params: TradingFeesParameters, tradingFees: TradingFees) {
        executeBackgroundOperation {
            tradingFeesTable.save(params, tradingFees)
        }
    }


    override suspend fun get(params: TradingFeesParameters): Result<TradingFees> {
        return performBackgroundOperation {
            tradingFeesTable.get(params)
        }
    }


}