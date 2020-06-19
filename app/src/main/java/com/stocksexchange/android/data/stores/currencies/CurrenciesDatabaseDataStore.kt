package com.stocksexchange.android.data.stores.currencies

import com.stocksexchange.api.model.rest.Currency
import com.stocksexchange.android.database.tables.CurrenciesTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class CurrenciesDatabaseDataStore(
    private val currenciesTable: CurrenciesTable
) : CurrenciesDataStore {


    override suspend fun save(currency: Currency) {
        executeBackgroundOperation {
            currenciesTable.save(currency)
        }
    }


    override suspend fun save(currencies: List<Currency>) {
        executeBackgroundOperation {
            currenciesTable.save(currencies)
        }
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            currenciesTable.deleteAll()
        }
    }


    override suspend fun get(currencyId: Int): Result<Currency> {
        return performBackgroundOperation {
            currenciesTable.get(currencyId)
        }
    }


    override suspend fun getAll(): Result<List<Currency>> {
        return performBackgroundOperation {
            currenciesTable.getAll()
        }
    }


}