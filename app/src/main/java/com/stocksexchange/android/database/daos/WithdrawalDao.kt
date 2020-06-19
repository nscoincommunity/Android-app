package com.stocksexchange.android.database.daos

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.stocksexchange.android.database.model.DatabaseWithdrawal

@Dao
interface WithdrawalDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(withdrawal: DatabaseWithdrawal)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(withdrawals: List<DatabaseWithdrawal>)


    @Query("DELETE FROM ${DatabaseWithdrawal.TABLE_NAME}")
    fun deleteAll()


    @RawQuery
    fun search(query: SupportSQLiteQuery): List<DatabaseWithdrawal>


    @RawQuery
    fun get(query: SupportSQLiteQuery): List<DatabaseWithdrawal>


}