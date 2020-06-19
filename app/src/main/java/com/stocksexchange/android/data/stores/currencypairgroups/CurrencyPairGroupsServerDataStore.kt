package com.stocksexchange.android.data.stores.currencypairgroups

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.CurrencyPairGroup
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class CurrencyPairGroupsServerDataStore(
    private val stexRestApi: StexRestApi
) : CurrencyPairGroupsDataStore {


    override suspend fun save(currencyPairGroups: List<CurrencyPairGroup>) {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun getAll(): Result<List<CurrencyPairGroup>> {
        return performBackgroundOperation {
            stexRestApi.getCurrencyPairGroups()
        }
    }


}