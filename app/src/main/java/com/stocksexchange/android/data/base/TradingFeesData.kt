package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.TradingFees
import com.stocksexchange.api.model.rest.parameters.TradingFeesParameters

interface TradingFeesData<
    TradingFeesFetchingResult
> {

    suspend fun save(params: TradingFeesParameters, tradingFees: TradingFees)

    suspend fun get(params: TradingFeesParameters): TradingFeesFetchingResult

}