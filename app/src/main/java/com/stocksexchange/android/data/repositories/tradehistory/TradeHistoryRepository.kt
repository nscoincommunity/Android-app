package com.stocksexchange.android.data.repositories.tradehistory

import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.api.model.rest.parameters.TradeHistoryParameters
import com.stocksexchange.android.data.base.TradeHistoryData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface TradeHistoryRepository : TradeHistoryData<
    RepositoryResult<List<Trade>>
>, Repository {

    suspend fun refresh(params: TradeHistoryParameters)

}