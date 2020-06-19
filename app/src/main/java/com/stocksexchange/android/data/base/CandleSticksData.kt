package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.CandleStick
import com.stocksexchange.api.model.rest.parameters.PriceChartDataParameters

interface CandleSticksData<
    CandleSticksFetchingResult
> {

    suspend fun save(params: PriceChartDataParameters, candleStick: CandleStick)

    suspend fun save(params: PriceChartDataParameters, candleSticks: List<CandleStick>)

    suspend fun delete(params: PriceChartDataParameters)

    suspend fun get(params: PriceChartDataParameters): CandleSticksFetchingResult

}