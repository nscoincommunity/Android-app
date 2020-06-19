package com.stocksexchange.android.data.stores.deposits

import com.stocksexchange.api.model.rest.Deposit
import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import com.stocksexchange.android.database.tables.DepositsTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class DepositsDatabaseDataStore(
    private val depositsTable: DepositsTable
) : DepositsDataStore {


    override suspend fun save(deposits: List<Deposit>) {
        executeBackgroundOperation {
            depositsTable.save(deposits)
        }
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            depositsTable.deleteAll()
        }
    }


    override suspend fun search(params: TransactionParameters): Result<List<Deposit>> {
        return performBackgroundOperation {
            depositsTable.search(params)
        }
    }


    override suspend fun get(params: TransactionParameters): Result<List<Deposit>> {
        return performBackgroundOperation {
            depositsTable.get(params)
        }
    }


}