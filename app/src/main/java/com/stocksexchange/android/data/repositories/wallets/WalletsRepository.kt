package com.stocksexchange.android.data.repositories.wallets

import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.android.data.base.WalletsData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface WalletsRepository : WalletsData<
    RepositoryResult<Wallet>,
    RepositoryResult<Wallet>,
    RepositoryResult<List<Wallet>>
>, Repository {

    suspend fun refresh()

    suspend fun createDepositAddressData(wallet: Wallet, protocolId: Int): RepositoryResult<Wallet>

    suspend fun getByCurrencyIds(currencyIds: List<Int>): RepositoryResult<List<Wallet>>

}