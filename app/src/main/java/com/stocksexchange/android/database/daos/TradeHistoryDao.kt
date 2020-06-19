package com.stocksexchange.android.database.daos

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.stocksexchange.android.database.model.DatabaseTrade

@Dao
interface TradeHistoryDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trade: DatabaseTrade)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trades: List<DatabaseTrade>)


    @Query(
        """
        DELETE FROM ${DatabaseTrade.TABLE_NAME} 
        WHERE ${DatabaseTrade.CURRENCY_PAIR_ID} = :currencyPairId
        """
    )
    fun delete(currencyPairId: Int)


    @RawQuery
    fun get(query: SupportSQLiteQuery): List<DatabaseTrade>


}