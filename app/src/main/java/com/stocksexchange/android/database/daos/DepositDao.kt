package com.stocksexchange.android.database.daos

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.stocksexchange.android.database.model.DatabaseDeposit

@Dao
interface DepositDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deposits: List<DatabaseDeposit>)


    @Query("DELETE FROM ${DatabaseDeposit.TABLE_NAME}")
    fun deleteAll()


    @RawQuery
    fun search(query: SupportSQLiteQuery): List<DatabaseDeposit>


    @RawQuery
    fun get(query: SupportSQLiteQuery): List<DatabaseDeposit>


}