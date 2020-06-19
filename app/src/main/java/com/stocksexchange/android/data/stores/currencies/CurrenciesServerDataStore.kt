package com.stocksexchange.android.data.stores.currencies

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.Currency
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class CurrenciesServerDataStore(
    private val stexRestApi: StexRestApi
) : CurrenciesDataStore {


    override suspend fun save(currency: Currency) {
        throw UnsupportedOperationException()
    }


    override suspend fun save(currencies: List<Currency>) {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun get(currencyId: Int): Result<Currency> {
        return performBackgroundOperation {
            stexRestApi.getCurrency(currencyId)
        }
    }


    override suspend fun getAll(): Result<List<Currency>> {
        return performBackgroundOperation {
            stexRestApi.getCurrencies()
        }
    }


}