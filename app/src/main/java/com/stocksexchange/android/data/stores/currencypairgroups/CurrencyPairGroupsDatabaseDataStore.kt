package com.stocksexchange.android.data.stores.currencypairgroups

import com.stocksexchange.api.model.rest.CurrencyPairGroup
import com.stocksexchange.android.database.tables.CurrencyPairGroupsTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class CurrencyPairGroupsDatabaseDataStore(
    private val currencyPairGroupsTable: CurrencyPairGroupsTable
) : CurrencyPairGroupsDataStore {


    override suspend fun save(currencyPairGroups: List<CurrencyPairGroup>) {
        executeBackgroundOperation {
            currencyPairGroupsTable.save(currencyPairGroups)
        }
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            currencyPairGroupsTable.deleteAll()
        }
    }


    override suspend fun getAll(): Result<List<CurrencyPairGroup>> {
        return performBackgroundOperation {
            currencyPairGroupsTable.getAll()
        }
    }


}