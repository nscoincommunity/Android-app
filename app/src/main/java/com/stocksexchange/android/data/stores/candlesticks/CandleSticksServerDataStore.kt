package com.stocksexchange.android.data.stores.candlesticks

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.CandleStick
import com.stocksexchange.api.model.rest.parameters.PriceChartDataParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation
import kotlin.UnsupportedOperationException

class CandleSticksServerDataStore(
    private val stexRestApi: StexRestApi
) : CandleSticksDataStore {


    override suspend fun save(params: PriceChartDataParameters, candleStick: CandleStick) {
        throw UnsupportedOperationException()
    }


    override suspend fun save(params: PriceChartDataParameters, candleSticks: List<CandleStick>) {
        throw UnsupportedOperationException()
    }


    override suspend fun delete(params: PriceChartDataParameters) {
        throw UnsupportedOperationException()
    }


    override suspend fun get(params: PriceChartDataParameters): Result<List<CandleStick>> {
        return performBackgroundOperation {
            stexRestApi.getCandleSticks(params)
        }
    }


}