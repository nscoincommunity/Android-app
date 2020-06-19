package com.stocksexchange.android.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stocksexchange.android.database.model.DatabaseCurrencyPairGroup

@Dao
interface CurrencyPairGroupDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currencyPairGroups: List<DatabaseCurrencyPairGroup>)


    @Query("DELETE FROM ${DatabaseCurrencyPairGroup.TABLE_NAME}")
    fun deleteAll()


    @Query(
        """
        SELECT * FROM ${DatabaseCurrencyPairGroup.TABLE_NAME} 
        ORDER BY ${DatabaseCurrencyPairGroup.POSITION} ASC
        """
    )
    fun getAll(): List<DatabaseCurrencyPairGroup>


}