package com.stocksexchange.android.data.repositories.utilities

import com.stocksexchange.api.model.rest.PingResponse
import com.stocksexchange.android.data.stores.utilities.UtilitiesDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.exceptions.NoInternetException

class UtilitiesRepositoryImpl(
    private val serverDataStore: UtilitiesDataStore,
    private val connectionProvider: ConnectionProvider
) : UtilitiesRepository {


    override suspend fun ping(url: String): RepositoryResult<PingResponse> {
        if(!connectionProvider.isNetworkAvailable()) {
            return RepositoryResult(NoInternetException())
        }

        return RepositoryResult(serverResult = serverDataStore.ping(url))
    }


}