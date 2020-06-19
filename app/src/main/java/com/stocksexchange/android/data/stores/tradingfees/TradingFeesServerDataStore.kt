package com.stocksexchange.android.data.stores.tradingfees

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.TradingFees
import com.stocksexchange.api.model.rest.parameters.TradingFeesParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class TradingFeesServerDataStore(
    private val stexRestApi: StexRestApi
) : TradingFeesDataStore {


    override suspend fun save(params: TradingFeesParameters, tradingFees: TradingFees) {
        throw UnsupportedOperationException()
    }


    override suspend fun get(params: TradingFeesParameters): Result<TradingFees> {
        return performBackgroundOperation {
            stexRestApi.getTradingFees(params)
        }
    }


}