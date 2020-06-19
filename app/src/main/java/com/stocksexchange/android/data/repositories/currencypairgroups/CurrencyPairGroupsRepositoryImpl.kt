package com.stocksexchange.android.data.repositories.currencypairgroups

import com.stocksexchange.api.model.rest.CurrencyPairGroup
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.SimpleFreshDataHandler
import com.stocksexchange.android.data.stores.currencypairgroups.CurrencyPairGroupsDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.providers.ConnectionProvider

class CurrencyPairGroupsRepositoryImpl(
    private val serverDataStore: CurrencyPairGroupsDataStore,
    private val databaseDataStore: CurrencyPairGroupsDataStore,
    private val freshDataHandler: SimpleFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : CurrencyPairGroupsRepository {


    @Synchronized
    override suspend fun save(currencyPairGroups: List<CurrencyPairGroup>) {
        databaseDataStore.save(currencyPairGroups)
    }


    @Synchronized
    override suspend fun deleteAll() {
        databaseDataStore.deleteAll()
    }


    @Synchronized
    override suspend fun getAll(): RepositoryResult<List<CurrencyPairGroup>> {
        val result = RepositoryResult<List<CurrencyPairGroup>>()

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