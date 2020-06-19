package com.stocksexchange.android.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stocksexchange.android.database.model.DatabaseCurrency

@Dao
interface CurrencyDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currency: DatabaseCurrency)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currencies: List<DatabaseCurrency>)


    @Query("DELETE FROM ${DatabaseCurrency.TABLE_NAME}")
    fun deleteAll()


    @Query(
        """
        SELECT * FROM ${DatabaseCurrency.TABLE_NAME} 
        WHERE ${DatabaseCurrency.ID} = :currencyId
        """
    )
    fun get(currencyId: Int): DatabaseCurrency?


    @Query("SELECT * FROM ${DatabaseCurrency.TABLE_NAME}")
    fun getAll(): List<DatabaseCurrency>


}