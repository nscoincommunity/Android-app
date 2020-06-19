package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.api.model.rest.parameters.WalletParameters

interface WalletsData<
    WalletCreationResult,
    WalletFetchingResult,
    WalletsFetchingResult
> {

    suspend fun save(wallet: Wallet)

    suspend fun save(wallets: List<Wallet>)

    suspend fun create(currencyId: Int, protocolId: Int): WalletCreationResult

    suspend fun deleteAll()

    suspend fun search(params: WalletParameters): WalletsFetchingResult

    suspend fun get(walletId: Long): WalletFetchingResult

    suspend fun getAll(params: WalletParameters): WalletsFetchingResult

}