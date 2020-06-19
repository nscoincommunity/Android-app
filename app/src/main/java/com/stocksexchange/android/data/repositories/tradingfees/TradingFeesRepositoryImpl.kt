package com.stocksexchange.android.data.repositories.tradingfees

import com.stocksexchange.api.model.rest.TradingFees
import com.stocksexchange.api.model.rest.parameters.TradingFeesParameters
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.TradingFeesFreshDataHandler
import com.stocksexchange.android.data.stores.tradingfees.TradingFeesDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.providers.ConnectionProvider

class TradingFeesRepositoryImpl(
    private val serverDataStore: TradingFeesDataStore,
    private val databaseDataStore: TradingFeesDataStore,
    private val freshDataHandler: TradingFeesFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : TradingFeesRepository {


    @Synchronized
    override suspend fun refresh(params: TradingFeesParameters) {
        freshDataHandler.refresh(params)
    }


    @Synchronized
    override suspend fun save(params: TradingFeesParameters, tradingFees: TradingFees) {
        databaseDataStore.save(params, tradingFees)
    }


    @Synchronized
    override suspend fun get(params: TradingFeesParameters): RepositoryResult<TradingFees> {
        val result = RepositoryResult<TradingFees>()

        if(freshDataHandler.shouldLoadFreshData(connectionProvider, params)) {
            result.serverResult = serverDataStore.get(params)

            if(result.isServerResultSuccessful()) {
                save(params, result.getSuccessfulResultValue())
            }
        }

        if(result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.get(params)
        }

        return result
    }


}