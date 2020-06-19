package com.stocksexchange.android.data.stores.wallets

import com.stocksexchange.api.model.rest.TransactionAddressData
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.api.model.rest.parameters.WalletParameters
import com.stocksexchange.android.database.tables.WalletsTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class WalletsDatabaseDataStore(
    private val walletsTable: WalletsTable
) : WalletsDataStore {


    override suspend fun save(wallet: Wallet) {
        executeBackgroundOperation {
            walletsTable.save(wallet)
        }
    }


    override suspend fun save(wallets: List<Wallet>) {
        executeBackgroundOperation {
            walletsTable.save(wallets)
        }
    }


    override suspend fun create(currencyId: Int, protocolId: Int): Result<Wallet> {
        throw UnsupportedOperationException()
    }


    override suspend fun createDepositAddressData(walletId: Long, protocolId: Int): Result<TransactionAddressData> {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            walletsTable.deleteAll()
        }
    }


    override suspend fun search(params: WalletParameters): Result<List<Wallet>> {
        return performBackgroundOperation {
            walletsTable.search(params)
        }
    }


    override suspend fun get(walletId: Long): Result<Wallet> {
        return performBackgroundOperation {
            walletsTable.get(walletId)
        }
    }


    override suspend fun getAll(params: WalletParameters): Result<List<Wallet>> {
        return performBackgroundOperation {
            walletsTable.getAll(params)
        }
    }


}