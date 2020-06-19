package com.stocksexchange.android.data.stores.candlesticks

import com.stocksexchange.api.model.rest.CandleStick
import com.stocksexchange.api.model.rest.parameters.PriceChartDataParameters
import com.stocksexchange.android.database.tables.CandleSticksTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class CandleSticksDatabaseDataStore(
    private val candleSticksTable: CandleSticksTable
) : CandleSticksDataStore {


    override suspend fun save(params: PriceChartDataParameters, candleStick: CandleStick) {
        executeBackgroundOperation {
            candleSticksTable.save(params, candleStick)
        }
    }


    override suspend fun save(params: PriceChartDataParameters, candleSticks: List<CandleStick>) {
        executeBackgroundOperation {
            candleSticksTable.save(params, candleSticks)
        }
    }


    override suspend fun delete(params: PriceChartDataParameters) {
        executeBackgroundOperation {
            candleSticksTable.delete(params)
        }
    }


    override suspend fun get(params: PriceChartDataParameters): Result<List<CandleStick>> {
        return performBackgroundOperation {
            candleSticksTable.get(params)
        }
    }


}