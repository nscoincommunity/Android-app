package com.stocksexchange.android.data.repositories.tradingfees

import com.stocksexchange.api.model.rest.TradingFees
import com.stocksexchange.api.model.rest.parameters.TradingFeesParameters
import com.stocksexchange.android.data.base.TradingFeesData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface TradingFeesRepository : TradingFeesData<
    RepositoryResult<TradingFees>
>, Repository {

    suspend fun refresh(params: TradingFeesParameters)

}