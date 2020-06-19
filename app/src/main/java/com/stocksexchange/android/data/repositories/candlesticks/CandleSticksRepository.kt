package com.stocksexchange.android.data.repositories.candlesticks

import com.stocksexchange.api.model.rest.CandleStick
import com.stocksexchange.api.model.rest.parameters.PriceChartDataParameters
import com.stocksexchange.android.data.base.CandleSticksData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface CandleSticksRepository : CandleSticksData<
    RepositoryResult<List<CandleStick>>
>, Repository {

    suspend fun refresh(params: PriceChartDataParameters)

}