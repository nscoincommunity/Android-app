package com.stocksexchange.android.data.stores.tradehistory

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.api.model.rest.parameters.TradeHistoryParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class TradeHistoryServerDataStore(
    private val stexRestApi: StexRestApi
) : TradeHistoryDataStore {


    override suspend fun save(params: TradeHistoryParameters, trade: Trade) {
        throw UnsupportedOperationException()
    }


    override suspend fun save(params: TradeHistoryParameters, trades: List<Trade>) {
        throw UnsupportedOperationException()
    }


    override suspend fun delete(currencyPairId: Int) {
        throw UnsupportedOperationException()
    }


    override suspend fun get(params: TradeHistoryParameters): Result<List<Trade>> {
        return performBackgroundOperation {
            stexRestApi.getHistoryTrades(params)
        }
    }


}