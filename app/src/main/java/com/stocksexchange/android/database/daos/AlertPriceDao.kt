package com.stocksexchange.android.database.daos

import androidx.room.*
import com.stocksexchange.android.database.model.DatabaseAlertPrice

@Dao
interface AlertPriceDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: DatabaseAlertPrice)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: List<DatabaseAlertPrice>)


    @Query("DELETE FROM ${DatabaseAlertPrice.TABLE_NAME}")
    fun deleteAll()


    @Query(
        """
        SELECT * FROM ${DatabaseAlertPrice.TABLE_NAME}
        """
    )
    fun get(): List<DatabaseAlertPrice>


    @Query(
        """
        SELECT * FROM ${DatabaseAlertPrice.TABLE_NAME}
        WHERE ${DatabaseAlertPrice.CURRENCY_PAIR_ID} = :pairId
        """
    )
    fun getAlertPriceByPairId(pairId: Int): List<DatabaseAlertPrice>


    @Query(
        """
        DELETE FROM ${DatabaseAlertPrice.TABLE_NAME} 
        WHERE ${DatabaseAlertPrice.ID} = :itemId
        """
    )
    fun delete(itemId: Int)


}