package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.Deposit
import com.stocksexchange.api.model.rest.parameters.TransactionParameters

interface DepositsData<
    DepositsFetchingResult
> {

    suspend fun save(deposits: List<Deposit>)

    suspend fun deleteAll()

    suspend fun search(params: TransactionParameters): DepositsFetchingResult

    suspend fun get(params: TransactionParameters): DepositsFetchingResult

}