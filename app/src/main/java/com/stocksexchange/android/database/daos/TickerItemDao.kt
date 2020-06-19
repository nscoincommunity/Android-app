package com.stocksexchange.android.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stocksexchange.android.database.model.DatabaseTickerItem

@Dao
interface TickerItemDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tickerItem: DatabaseTickerItem)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tickerItems: List<DatabaseTickerItem>)


    @Query("DELETE FROM ${DatabaseTickerItem.TABLE_NAME}")
    fun deleteAll()


    @Query(
        """
        SELECT * FROM ${DatabaseTickerItem.TABLE_NAME} 
        WHERE ${DatabaseTickerItem.ID} = :currencyPairId
        """
    )
    fun get(currencyPairId: Int): DatabaseTickerItem?


    @Query("SELECT * FROM ${DatabaseTickerItem.TABLE_NAME}")
    fun getAll(): List<DatabaseTickerItem>


}