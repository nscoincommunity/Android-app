package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.api.model.rest.WalletBalanceType
import com.stocksexchange.android.database.daos.WalletDao
import com.stocksexchange.android.database.model.DatabaseWallet
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.database.utils.QueryBuilder
import com.stocksexchange.android.mappings.mapToDatabaseWallet
import com.stocksexchange.android.mappings.mapToDatabaseWalletList
import com.stocksexchange.android.mappings.mapToWallet
import com.stocksexchange.android.mappings.mapToWalletList
import com.stocksexchange.api.exceptions.rest.WalletException
import com.stocksexchange.api.model.rest.parameters.WalletParameters
import com.stocksexchange.core.model.Result

class WalletsTable(
    private val walletDao: WalletDao
) : BaseTable() {


    @Synchronized
    fun save(wallet: Wallet) {
        walletDao.insert(wallet.mapToDatabaseWallet())
    }


    @Synchronized
    fun save(wallets: List<Wallet>) {
        walletDao.insert(wallets.mapToDatabaseWalletList())
    }


    @Synchronized
    fun deleteAll() {
        walletDao.deleteAll()
    }


    @Synchronized
    fun search(parameters: WalletParameters): Result<List<Wallet>> {
        val query = QueryBuilder.Wallets.getSearchQuery(
            query = parameters.lowercasedSearchQuery,
            sortColumn = getColumnForBalanceType(parameters.sortColumn),
            sortOrder = parameters.sortOrder.name
        )

        return walletDao.search(query).toResult(
            List<DatabaseWallet>::mapToWalletList
        )
    }


    @Synchronized
    fun get(walletId: Long): Result<Wallet> {
        return walletDao.get(walletId).toResult(
            DatabaseWallet::mapToWallet
        )
    }


    @Synchronized
    fun getAll(parameters: WalletParameters): Result<List<Wallet>> {
        val query = QueryBuilder.Wallets.getAllQuery(
            sortColumn = getColumnForBalanceType(parameters.sortColumn),
            sortOrder = parameters.sortOrder.name
        )

        return walletDao.getAll(query).toResult(
            List<DatabaseWallet>::mapToWalletList
        )
    }


    private fun getColumnForBalanceType(balanceType: WalletBalanceType): String {
        return when(balanceType) {
            WalletBalanceType.CURRENT -> DatabaseWallet.CURRENT_BALANCE
            WalletBalanceType.FROZEN -> DatabaseWallet.FROZEN_BALANCE
            WalletBalanceType.BONUS -> DatabaseWallet.BONUS_BALANCE
            WalletBalanceType.TOTAL -> DatabaseWallet.TOTAL_BALANCE
        }
    }


    override fun getNotFoundException(): Exception {
        return WalletException.walletNotFound()
    }


}