package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.Deposit
import com.stocksexchange.android.database.daos.DepositDao
import com.stocksexchange.android.database.model.DatabaseDeposit
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.database.utils.QueryBuilder
import com.stocksexchange.android.mappings.mapToDatabaseDepositList
import com.stocksexchange.android.mappings.mapToDepositList
import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import com.stocksexchange.core.model.Result

class DepositsTable(
    private val depositDao: DepositDao
) : BaseTable() {


    @Synchronized
    fun save(deposits: List<Deposit>) {
        depositDao.insert(deposits.mapToDatabaseDepositList())
    }


    @Synchronized
    fun deleteAll() {
        depositDao.deleteAll()
    }


    @Synchronized
    fun search(params: TransactionParameters): Result<List<Deposit>> {
        val query = QueryBuilder.Deposits.getSearchQuery(
            query = params.lowercasedSearchQuery,
            sortOrder = params.sortOrder.name,
            limit = params.limit,
            offset = params.offset
        )

        return depositDao.search(query).toResult(
            List<DatabaseDeposit>::mapToDepositList
        )
    }


    @Synchronized
    fun get(params: TransactionParameters): Result<List<Deposit>> {
        val query = QueryBuilder.Deposits.getQuery(
            sortOrder = params.sortOrder.name,
            limit = params.limit,
            offset = params.offset
        )

        return depositDao.get(query).toResult(
            List<DatabaseDeposit>::mapToDepositList
        )
    }


}