package com.stocksexchange.android.data.repositories.deposits

import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.SimpleFreshDataHandler
import com.stocksexchange.api.model.rest.Deposit
import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import com.stocksexchange.android.data.stores.deposits.DepositsDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.providers.ConnectionProvider

class DepositsRepositoryImpl(
    private val serverDataStore: DepositsDataStore,
    private val databaseDataStore: DepositsDataStore,
    private val freshDataHandler: SimpleFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : DepositsRepository {


    @Synchronized
    override suspend fun refresh() {
        freshDataHandler.refresh()
    }


    @Synchronized
    override suspend fun save(deposits: List<Deposit>) {
        databaseDataStore.save(deposits)
    }


    @Synchronized
    override suspend fun deleteAll() {
        databaseDataStore.deleteAll()
    }


    @Synchronized
    override suspend fun clear() {
        deleteAll()

        freshDataHandler.reset()
    }


    @Synchronized
    override suspend fun search(params: TransactionParameters): RepositoryResult<List<Deposit>> {
        // Fetching the data to make sure it is present since the search is
        // performed solely on database records
        val result = get(params)

        return if(result.isSuccessful()) {
            RepositoryResult(databaseResult = databaseDataStore.search(params))
        } else {
            result
        }
    }


    @Synchronized
    override suspend fun get(params: TransactionParameters): RepositoryResult<List<Deposit>> {
        val result = RepositoryResult<List<Deposit>>()

        if(freshDataHandler.shouldLoadFreshData(connectionProvider)) {
            result.serverResult = serverDataStore.get(params)

            if(result.isServerResultSuccessful()) {
                save(result.getSuccessfulResultValue())
            }
        }

        if(result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.get(params)
        }

        return result
    }


}