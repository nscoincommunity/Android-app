package com.stocksexchange.android.database.daos

import androidx.room.*
import com.stocksexchange.android.database.model.DatabaseCandleStick

@Dao
interface CandleStickDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(candleStick: DatabaseCandleStick)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(candleSticks: List<DatabaseCandleStick>)


    @Query(
        """
        DELETE FROM ${DatabaseCandleStick.TABLE_NAME} 
        WHERE ${DatabaseCandleStick.CURRENCY_PAIR_ID} = :currencyPairId AND 
        ${DatabaseCandleStick.INTERVAL_NAME} = :intervalName
        """
    )
    fun delete(currencyPairId: Int, intervalName: String)


    @Query(
        """
         SELECT * FROM (SELECT * FROM ${DatabaseCandleStick.TABLE_NAME} 
         WHERE ${DatabaseCandleStick.CURRENCY_PAIR_ID} = :currencyPairId AND 
         ${DatabaseCandleStick.INTERVAL_NAME} = :intervalName 
         ORDER BY ${DatabaseCandleStick.TIMESTAMP} DESC 
         LIMIT :count) ORDER BY ${DatabaseCandleStick.TIMESTAMP} ASC
        """
    )
    fun get(currencyPairId: Int, intervalName: String, count: Int): List<DatabaseCandleStick>


}