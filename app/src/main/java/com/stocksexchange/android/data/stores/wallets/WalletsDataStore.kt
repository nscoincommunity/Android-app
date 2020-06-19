package com.stocksexchange.android.data.stores.wallets

import com.stocksexchange.api.model.rest.TransactionAddressData
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.android.data.base.WalletsData
import com.stocksexchange.core.model.Result

interface WalletsDataStore : WalletsData<
    Result<Wallet>,
    Result<Wallet>,
    Result<List<Wallet>>
> {

    suspend fun createDepositAddressData(walletId: Long, protocolId: Int): Result<TransactionAddressData>

}