package com.stocksexchange.android.data.repositories.candlesticks

import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.CandleSticksFreshDataHandler
import com.stocksexchange.api.model.rest.CandleStick
import com.stocksexchange.api.model.rest.parameters.PriceChartDataParameters
import com.stocksexchange.android.data.stores.candlesticks.CandleSticksDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.providers.ConnectionProvider

class CandleSticksRepositoryImpl(
    private val serverDataStore: CandleSticksDataStore,
    private val databaseDataStore: CandleSticksDataStore,
    private val freshDataHandler: CandleSticksFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : CandleSticksRepository {


    @Synchronized
    override suspend fun refresh(params: PriceChartDataParameters) {
        freshDataHandler.refresh(params)
    }


    @Synchronized
    override suspend fun save(params: PriceChartDataParameters, candleStick: CandleStick) {
        databaseDataStore.save(params, candleStick)
    }


    @Synchronized
    override suspend fun save(params: PriceChartDataParameters, candleSticks: List<CandleStick>) {
        databaseDataStore.save(params, candleSticks)
    }


    @Synchronized
    override suspend fun delete(params: PriceChartDataParameters) {
        databaseDataStore.delete(params)
    }


    @Synchronized
    override suspend fun get(params: PriceChartDataParameters): RepositoryResult<List<CandleStick>> {
        val result = RepositoryResult<List<CandleStick>>()

        if(freshDataHandler.shouldLoadFreshData(connectionProvider, params)) {
            result.serverResult = serverDataStore.get(params)

            if(result.isServerResultSuccessful()) {
                delete(params)
                save(params, result.getSuccessfulResultValue())
            }
        }

        if(result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.get(params)
        }

        return result
    }


}