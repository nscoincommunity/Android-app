package com.stocksexchange.android.data.repositories.alertprice

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.PriceAlertFreshDataHandler
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.data.stores.alertprice.AlertPriceDataStore
import com.stocksexchange.api.model.rest.parameters.AlertPriceParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.exceptions.NoInternetException

class AlertPriceRepositoryImpl(
    private val serverDataStore: AlertPriceDataStore,
    private val databaseDataStore: AlertPriceDataStore,
    private val freshDataHandler: PriceAlertFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : AlertPriceRepository {


    @Synchronized
    override suspend fun refresh(params: AlertPriceParameters) {
        freshDataHandler.refresh(params)
    }


    @Synchronized
    override suspend fun clear() {
        deleteAll()

        freshDataHandler.reset()
    }


    @Synchronized
    override suspend fun create(alertPriceParameters: AlertPriceParameters): RepositoryResult<AlertPrice> {
        return performTask {
            serverDataStore.create(alertPriceParameters).also {
                if (it is Result.Success) {
                    save(listOf(it.value))
                }
            }
        }
    }


    override suspend fun delete(id: Int): RepositoryResult<AlertPriceDeleteResponse> {
        databaseDataStore.delete(id)

        return performTask {
            serverDataStore.delete(id)
        }
    }


    @Synchronized
    override suspend fun deleteAll() {
        databaseDataStore.deleteAll()
    }


    @Synchronized
    override suspend fun get(params: AlertPriceParameters): RepositoryResult<List<AlertPrice>> {
        val result = RepositoryResult<List<AlertPrice>>()

        if (freshDataHandler.shouldLoadFreshData(connectionProvider, params)) {
            result.serverResult = serverDataStore.get(params)

            if (result.isServerResultSuccessful()) {
                save(result.getSuccessfulResultValue())
            }
        }

        if (result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.get(params)
        }

        return result
    }


    @Synchronized
    override suspend fun getAlertPriceListByPairId(id: Int): RepositoryResult<List<AlertPrice>> {
        val result = RepositoryResult<List<AlertPrice>>()

        result.serverResult = serverDataStore.getAlertPriceListByPairId(id)

        if (result.isServerResultSuccessful()) {
            save(result.getSuccessfulResultValue())
        }

        if (result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.getAlertPriceListByPairId(id)
        }

        return result
    }


    @Synchronized
    override suspend fun save(alertPrices: List<AlertPrice>) {
        databaseDataStore.save(alertPrices)
    }


    private suspend fun <T> performTask(getServerResult: suspend (() -> Result<T>)): RepositoryResult<T> {
        if(!connectionProvider.isNetworkAvailable()) {
            return RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }

        return RepositoryResult(serverResult = getServerResult())
    }


}