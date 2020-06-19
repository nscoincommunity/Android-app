package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.api.model.rest.parameters.TradeHistoryParameters

interface TradeHistoryData<
    TradesFetchingResult
> {

    suspend fun save(params: TradeHistoryParameters, trade: Trade)

    suspend fun save(params: TradeHistoryParameters, trades: List<Trade>)

    suspend fun delete(currencyPairId: Int)

    suspend fun get(params: TradeHistoryParameters): TradesFetchingResult

}