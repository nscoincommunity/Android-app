package com.stocksexchange.android.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stocksexchange.android.database.model.DatabaseCurrencyPair
import com.stocksexchange.core.Constants

@Dao
interface CurrencyPairDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currencyPair: DatabaseCurrencyPair)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currencyPairs: List<DatabaseCurrencyPair>)


    @Query("DELETE FROM ${DatabaseCurrencyPair.TABLE_NAME}")
    fun deleteAll()


    @Query(
        """
        SELECT * FROM ${DatabaseCurrencyPair.TABLE_NAME} 
        WHERE LOWER(${DatabaseCurrencyPair.BASE_CURRENCY_SYMBOL}) = :query OR 
        LOWER(${DatabaseCurrencyPair.QUOTE_CURRENCY_SYMBOL}) = :query OR (
        REPLACE(LOWER(${DatabaseCurrencyPair.NAME}), '${Constants.CURRENCY_MARKET_SEPARATOR}', ' / ') LIKE (:query || '%')) OR 
        LOWER(${DatabaseCurrencyPair.BASE_CURRENCY_NAME}) LIKE (:query || '%') OR 
        LOWER(${DatabaseCurrencyPair.QUOTE_CURRENCY_NAME}) LIKE (:query || '%')
        """
    )
    fun search(query: String): List<DatabaseCurrencyPair>


    @Query(
        """
        SELECT * FROM ${DatabaseCurrencyPair.TABLE_NAME} 
        WHERE ${DatabaseCurrencyPair.ID} = :currencyPairId
        """
    )
    fun get(currencyPairId: Int): DatabaseCurrencyPair?


    @Query("SELECT * FROM ${DatabaseCurrencyPair.TABLE_NAME}")
    fun getAll(): List<DatabaseCurrencyPair>



}