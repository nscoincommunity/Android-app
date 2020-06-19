package com.stocksexchange.android.data.stores.wallets

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.TransactionAddressData
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.api.model.rest.parameters.WalletParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class WalletsServerDataStore(
    private val stexRestApi: StexRestApi
) : WalletsDataStore {


    override suspend fun save(wallet: Wallet) {
        throw UnsupportedOperationException()
    }


    override suspend fun save(wallets: List<Wallet>) {
        throw UnsupportedOperationException()
    }


    override suspend fun create(currencyId: Int, protocolId: Int): Result<Wallet> {
        return performBackgroundOperation {
            stexRestApi.createWallet(currencyId, protocolId)
        }
    }


    override suspend fun createDepositAddressData(walletId: Long, protocolId: Int): Result<TransactionAddressData> {
        return performBackgroundOperation {
            stexRestApi.createDepositAddress(walletId, protocolId)
        }
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun search(params: WalletParameters): Result<List<Wallet>> {
        throw UnsupportedOperationException()
    }


    override suspend fun get(walletId: Long): Result<Wallet> {
        return performBackgroundOperation {
            stexRestApi.getWallet(walletId)
        }
    }


    override suspend fun getAll(params: WalletParameters): Result<List<Wallet>> {
        return performBackgroundOperation {
            stexRestApi.getWallets(params)
        }
    }


}