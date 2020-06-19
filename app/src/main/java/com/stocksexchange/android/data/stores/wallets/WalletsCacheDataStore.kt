package com.stocksexchange.android.data.stores.wallets

import com.stocksexchange.android.data.stores.base.BaseCacheDataStore
import com.stocksexchange.api.model.rest.TransactionAddressData
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.api.model.rest.parameters.WalletParameters
import com.stocksexchange.core.exceptions.NotFoundException
import com.stocksexchange.core.model.Result

class WalletsCacheDataStore : BaseCacheDataStore(), WalletsDataStore {


    override suspend fun save(wallet: Wallet) {
        if(!wallet.hasId) {
            return
        }

        cache.put(wallet.id.toString(), wallet)
    }


    override suspend fun save(wallets: List<Wallet>) {
        throw UnsupportedOperationException()
    }


    override suspend fun create(currencyId: Int, protocolId: Int): Result<Wallet> {
        throw UnsupportedOperationException()
    }


    override suspend fun createDepositAddressData(walletId: Long, protocolId: Int): Result<TransactionAddressData> {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        clear()
    }


    override suspend fun search(params: WalletParameters): Result<List<Wallet>> {
        throw UnsupportedOperationException()
    }


    override suspend fun get(walletId: Long): Result<Wallet> {
        val walletIdStr = walletId.toString()

        return if(cache.contains(walletIdStr)) {
            Result.Success(cache.get(walletIdStr) as Wallet)
        } else {
            Result.Failure(NotFoundException())
        }
    }


    override suspend fun getAll(params: WalletParameters): Result<List<Wallet>> {
        throw UnsupportedOperationException()
    }


}