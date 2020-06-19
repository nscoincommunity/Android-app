package com.stocksexchange.android.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stocksexchange.android.database.model.DatabaseTradingFees

@Dao
interface TradingFeesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tradingFees: DatabaseTradingFees)


    @Query(
        """
        SELECT * FROM ${DatabaseTradingFees.TABLE_NAME} 
        WHERE ${DatabaseTradingFees.CURRENCY_PAIR_ID} = :currencyPairId
        """
    )
    fun get(currencyPairId: Int): DatabaseTradingFees?


}