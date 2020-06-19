package com.stocksexchange.android.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stocksexchange.android.database.model.DatabaseSettings

@Dao
interface SettingsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(settings: DatabaseSettings)


    @Query(
        """
        SELECT * FROM ${DatabaseSettings.TABLE_NAME} 
        WHERE ${DatabaseSettings.ID} = :id
        """
    )
    fun get(id: Int): DatabaseSettings?


}