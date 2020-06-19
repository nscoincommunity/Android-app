package com.stocksexchange.android.data.repositories.currencies

import com.stocksexchange.api.model.rest.Currency
import com.stocksexchange.android.data.stores.currencies.CurrenciesDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.model.Result
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.SimpleFreshDataHandler
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.exceptions.NotFoundException

class CurrenciesRepositoryImpl(
    private val serverDataStore: CurrenciesDataStore,
    private val databaseDataStore: CurrenciesDataStore,
    private val freshDataHandler: SimpleFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : CurrenciesRepository {


    @Synchronized
    override suspend fun refresh() {
        freshDataHandler.refresh()
    }


    @Synchronized
    override suspend fun save(currency: Currency) {
        databaseDataStore.save(currency)
    }


    @Synchronized
    override suspend fun save(currencies: List<Currency>) {
        databaseDataStore.save(currencies)
    }


    @Synchronized
    override suspend fun deleteAll() {
        databaseDataStore.deleteAll()
    }


    @Synchronized
    override suspend fun get(currencyId: Int): RepositoryResult<Currency> {
        return get {
            it.id == currencyId
        }
    }


    private suspend fun get(condition: ((Currency) -> Boolean)): RepositoryResult<Currency> {
        val result = getAll()

        if(result.isSuccessful()) {
            for(currency in result.getSuccessfulResultValue()) {
                if(condition(currency)) {
                    return RepositoryResult.newSuccessfulInstance(
                        successfulResult = result,
                        successfulValue = currency
                    )
                }
            }
        }

        return RepositoryResult(cacheResult = Result.Failure(NotFoundException()))
    }


    @Synchronized
    override suspend fun getAll(): RepositoryResult<List<Currency>> {
        val result = RepositoryResult<List<Currency>>()

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