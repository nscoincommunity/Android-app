package com.stocksexchange.android.data.repositories.tradehistory

import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.api.model.rest.parameters.TradeHistoryParameters
import com.stocksexchange.android.data.stores.tradehistory.TradeHistoryDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.TradeHistoryFreshDataHandler
import com.stocksexchange.core.providers.ConnectionProvider

class TradeHistoryRepositoryImpl(
    private val serverDataStore: TradeHistoryDataStore,
    private val databaseDataStore: TradeHistoryDataStore,
    private val freshDataHandler: TradeHistoryFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : TradeHistoryRepository {


    @Synchronized
    override suspend fun refresh(params: TradeHistoryParameters) {
        freshDataHandler.refresh(params)
    }


    @Synchronized
    override suspend fun save(params: TradeHistoryParameters, trade: Trade) {
        databaseDataStore.save(params, trade)
    }


    @Synchronized
    override suspend fun save(params: TradeHistoryParameters, trades: List<Trade>) {
        databaseDataStore.save(params, trades)
    }


    @Synchronized
    override suspend fun delete(currencyPairId: Int) {
        databaseDataStore.delete(currencyPairId)
    }


    @Synchronized
    override suspend fun get(params: TradeHistoryParameters): RepositoryResult<List<Trade>> {
        val result = RepositoryResult<List<Trade>>()

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