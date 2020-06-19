package com.stocksexchange.android.database.daos

import androidx.room.*
import com.stocksexchange.android.database.model.DatabaseProfileInfo

@Dao
interface ProfileInfoDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(profileInfo: DatabaseProfileInfo)


    @Query("DELETE FROM ${DatabaseProfileInfo.TABLE_NAME}")
    fun deleteAll()


    @Query(
        """
        SELECT * FROM ${DatabaseProfileInfo.TABLE_NAME} 
        WHERE ${DatabaseProfileInfo.EMAIL} = :email
        """
    )
    fun get(email: String): DatabaseProfileInfo?


}