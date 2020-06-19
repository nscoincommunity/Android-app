package com.stocksexchange.android.database.daos

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.stocksexchange.android.database.model.DatabaseInbox

@Dao
interface InboxDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(inbox: List<DatabaseInbox>)


    @Query("DELETE FROM ${DatabaseInbox.TABLE_NAME}")
    fun deleteAll()


    @Query(
        """
        SELECT * FROM ${DatabaseInbox.TABLE_NAME}
        """
    )
    fun get(): List<DatabaseInbox>


    @Query(
        """
        DELETE FROM ${DatabaseInbox.TABLE_NAME}
        WHERE ${DatabaseInbox.ID} = :inboxId
        """
    )
    fun delete(inboxId: String)


}