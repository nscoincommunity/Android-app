package com.stocksexchange.android.data.stores.currencypairs

import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.android.database.tables.CurrencyPairsTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class CurrencyPairsDatabaseDataStore(
    private val currencyPairsTable: CurrencyPairsTable
) : CurrencyPairsDataStore {


    override suspend fun save(currencyPair: CurrencyPair) {
        executeBackgroundOperation {
            currencyPairsTable.save(currencyPair)
        }
    }


    override suspend fun save(currencyPairs: List<CurrencyPair>) {
        executeBackgroundOperation {
            currencyPairsTable.save(currencyPairs)
        }
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            currencyPairsTable.deleteAll()
        }
    }


    override suspend fun search(query: String): Result<List<CurrencyPair>> {
        return performBackgroundOperation {
            currencyPairsTable.search(query)
        }
    }


    override suspend fun get(currencyPairId: Int): Result<CurrencyPair> {
        return performBackgroundOperation {
            currencyPairsTable.get(currencyPairId)
        }
    }


    override suspend fun getAll(): Result<List<CurrencyPair>> {
        return performBackgroundOperation {
            currencyPairsTable.getAll()
        }
    }


}