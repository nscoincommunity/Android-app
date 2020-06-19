package com.stocksexchange.android.data.stores.currencypairs

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class CurrencyPairsServerDataStore(
    private val stexRestApi: StexRestApi
) : CurrencyPairsDataStore {


    override suspend fun save(currencyPair: CurrencyPair) {
        throw UnsupportedOperationException()
    }


    override suspend fun save(currencyPairs: List<CurrencyPair>) {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun search(query: String): Result<List<CurrencyPair>> {
        throw UnsupportedOperationException()
    }


    override suspend fun get(currencyPairId: Int): Result<CurrencyPair> {
        return performBackgroundOperation {
            stexRestApi.getCurrencyPair(currencyPairId)
        }
    }


    override suspend fun getAll(): Result<List<CurrencyPair>> {
        return performBackgroundOperation {
            stexRestApi.getAllCurrencyPairs()
        }
    }


}