package com.stocksexchange.android.data.stores.deposits

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.Deposit
import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class DepositsServerDataStore(
    private val stexRestApi: StexRestApi
) : DepositsDataStore {


    override suspend fun save(deposits: List<Deposit>) {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun search(params: TransactionParameters): Result<List<Deposit>> {
        throw UnsupportedOperationException()
    }


    override suspend fun get(params: TransactionParameters): Result<List<Deposit>> {
        return performBackgroundOperation {
            stexRestApi.getDeposits(params)
        }
    }


}