package com.stocksexchange.android.database.daos

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.stocksexchange.android.database.model.DatabaseOrder

@Dao
interface OrderDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(order: DatabaseOrder)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(orders: List<DatabaseOrder>)


    @Delete
    fun delete(order: DatabaseOrder)


    @RawQuery
    fun delete(query: SupportSQLiteQuery): Int


    @Query("DELETE FROM ${DatabaseOrder.TABLE_NAME}")
    fun deleteAll()


    @RawQuery
    fun search(query: SupportSQLiteQuery): List<DatabaseOrder>


    @Query(
        """
        SELECT * FROM ${DatabaseOrder.TABLE_NAME} 
        WHERE ${DatabaseOrder.ID} = :id
        """
    )
    fun get(id: Long): DatabaseOrder?


    @RawQuery
    fun get(query: SupportSQLiteQuery): List<DatabaseOrder>


}