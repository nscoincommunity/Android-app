package com.stocksexchange.android.data.stores.utilities

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.PingResponse
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class UtilitiesServerDataStore(
    private val stexRestApi: StexRestApi
) : UtilitiesDataStore {


    override suspend fun ping(url: String): Result<PingResponse> {
        return performBackgroundOperation {
            stexRestApi.ping(url)
        }
    }


}