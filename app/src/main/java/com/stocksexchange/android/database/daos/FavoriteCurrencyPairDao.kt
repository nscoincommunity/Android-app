package com.stocksexchange.android.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stocksexchange.android.database.model.DatabaseFavoriteCurrencyPair

@Dao
interface FavoriteCurrencyPairDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currencyPair: DatabaseFavoriteCurrencyPair)


    @Query(
        """
        DELETE FROM ${DatabaseFavoriteCurrencyPair.TABLE_NAME} 
        WHERE ${DatabaseFavoriteCurrencyPair.ID} = :currencyPairId
        """
    )
    fun delete(currencyPairId: Int)


    @Query("SELECT Count(*) FROM ${DatabaseFavoriteCurrencyPair.TABLE_NAME}")
    fun getCount(): Int


    @Query(
        """
        SELECT ${DatabaseFavoriteCurrencyPair.ID} 
        FROM ${DatabaseFavoriteCurrencyPair.TABLE_NAME}
        """
    )
    fun getAll(): List<Int>


}