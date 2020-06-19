package com.stocksexchange.android.data.repositories.currencypairs

import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.android.data.stores.currencypairs.CurrencyPairsDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.SimpleFreshDataHandler
import com.stocksexchange.core.providers.ConnectionProvider

class CurrencyPairsRepositoryImpl(
    private val serverDataStore: CurrencyPairsDataStore,
    private val databaseDataStore: CurrencyPairsDataStore,
    private val freshDataHandler: SimpleFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : CurrencyPairsRepository {


    @Synchronized
    override suspend fun refresh() {
        freshDataHandler.refresh()
    }


    @Synchronized
    override suspend fun save(currencyPair: CurrencyPair) {
        databaseDataStore.save(currencyPair)
    }


    @Synchronized
    override suspend fun save(currencyPairs: List<CurrencyPair>) {
        databaseDataStore.save(currencyPairs)
    }


    @Synchronized
    override suspend fun deleteAll() {
        databaseDataStore.deleteAll()
    }


    @Synchronized
    override suspend fun search(query: String): RepositoryResult<List<CurrencyPair>> {
        // Fetching the data to make sure it is present since the search is
        // performed solely on database records
        val result = getAll()

        return if(result.isSuccessful()) {
            RepositoryResult(databaseResult = databaseDataStore.search(query))
        } else {
            result
        }
    }


    @Synchronized
    override suspend fun get(currencyPairId: Int): RepositoryResult<CurrencyPair> {
        return RepositoryResult<CurrencyPair>().apply {
            databaseResult = databaseDataStore.get(currencyPairId)

            if(isDatabaseResultErroneous() && connectionProvider.isNetworkAvailable()) {
                serverResult = serverDataStore.get(currencyPairId)
            }
        }
    }


    @Synchronized
    override suspend fun getAll(): RepositoryResult<List<CurrencyPair>> {
        val result = RepositoryResult<List<CurrencyPair>>()

        if(freshDataHandler.shouldLoadFreshData(connectionProvider)) {
            result.serverResult = serverDataStore.getAll()

            if(result.isServerResultSuccessful()) {
                deleteAll()
                save(result.getSuccessfulResultValue())
            }
        }

        if(result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.getAll()
        }

        return result
    }


}