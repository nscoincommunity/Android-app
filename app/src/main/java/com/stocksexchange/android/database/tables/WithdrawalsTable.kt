package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.Withdrawal
import com.stocksexchange.android.database.daos.WithdrawalDao
import com.stocksexchange.android.database.model.DatabaseWithdrawal
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.database.utils.QueryBuilder
import com.stocksexchange.android.mappings.mapToDatabaseWithdrawal
import com.stocksexchange.android.mappings.mapToDatabaseWithdrawalList
import com.stocksexchange.android.mappings.mapToWithdrawalList
import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import com.stocksexchange.core.model.Result

class WithdrawalsTable(
    private val withdrawalDao: WithdrawalDao
) : BaseTable() {


    @Synchronized
    fun save(withdrawal: Withdrawal) {
        withdrawalDao.insert(withdrawal.mapToDatabaseWithdrawal())
    }


    @Synchronized
    fun save(withdrawals: List<Withdrawal>) {
        withdrawalDao.insert(withdrawals.mapToDatabaseWithdrawalList())
    }


    @Synchronized
    fun deleteAll() {
        withdrawalDao.deleteAll()
    }


    @Synchronized
    fun search(params: TransactionParameters): Result<List<Withdrawal>> {
        val query = QueryBuilder.Withdrawals.getSearchQuery(
            query = params.lowercasedSearchQuery,
            sortOrder = params.sortOrder.name,
            limit = params.limit,
            offset = params.offset
        )

        return withdrawalDao.search(query).toResult(
            List<DatabaseWithdrawal>::mapToWithdrawalList
        )
    }


    @Synchronized
    fun get(params: TransactionParameters): Result<List<Withdrawal>> {
        val query = QueryBuilder.Withdrawals.getQuery(
            sortOrder = params.sortOrder.name,
            limit = params.limit,
            offset = params.offset
        )

        return withdrawalDao.get(query).toResult(
            List<DatabaseWithdrawal>::mapToWithdrawalList
        )
    }


}