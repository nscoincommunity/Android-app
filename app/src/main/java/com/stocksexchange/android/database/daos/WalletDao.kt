package com.stocksexchange.android.database.daos

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.stocksexchange.android.database.model.DatabaseWallet

@Dao
interface WalletDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(wallet: DatabaseWallet)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(wallets: List<DatabaseWallet>)


    @Query("DELETE FROM ${DatabaseWallet.TABLE_NAME}")
    fun deleteAll()


    @RawQuery
    fun search(query: SupportSQLiteQuery): List<DatabaseWallet>


    @Query(
        """
        SELECT * FROM ${DatabaseWallet.TABLE_NAME} 
        WHERE ${DatabaseWallet.ID} = :walletId AND 
        (${DatabaseWallet.DEPOSIT_ADDRESS_DATA} IS NOT NULL OR 
        ${DatabaseWallet.MULTI_DEPOSIT_ADDRESSES} != '')
        """
    )
    fun get(walletId: Long): DatabaseWallet?


    @RawQuery
    fun getAll(query: SupportSQLiteQuery): List<DatabaseWallet>


}