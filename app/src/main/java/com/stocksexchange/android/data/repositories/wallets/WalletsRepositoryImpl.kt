package com.stocksexchange.android.data.repositories.wallets

import com.stocksexchange.api.model.rest.TransactionAddressData
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.api.model.rest.parameters.WalletParameters
import com.stocksexchange.android.data.stores.wallets.WalletsDataStore
import com.stocksexchange.android.mappings.mapToCurrencyIdWalletMap
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.model.Result
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.SimpleFreshDataHandler
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.exceptions.NoInternetException

class WalletsRepositoryImpl(
    private val serverDataStore: WalletsDataStore,
    private val databaseDataStore: WalletsDataStore,
    private val cacheDataStore: WalletsDataStore,
    private val freshDataHandler: SimpleFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : WalletsRepository {


    @Synchronized
    override suspend fun refresh() {
        freshDataHandler.refresh()
        cacheDataStore.deleteAll()
    }


    @Synchronized
    override suspend fun save(wallet: Wallet) {
        databaseDataStore.save(wallet)
        cacheDataStore.save(wallet)
    }


    @Synchronized
    override suspend fun save(wallets: List<Wallet>) {
        databaseDataStore.save(wallets)
    }


    @Synchronized
    override suspend fun create(currencyId: Int, protocolId: Int): RepositoryResult<Wallet> {
        if(!connectionProvider.isNetworkAvailable()) {
            return RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }

        val walletCreationResult = serverDataStore.create(currencyId, protocolId).also {
            if(it is Result.Success) {
                save(it.value)
            }
        }

        return RepositoryResult(serverResult = walletCreationResult)
    }


    @Synchronized
    override suspend fun createDepositAddressData(wallet: Wallet, protocolId: Int): RepositoryResult<Wallet> {
        if(!connectionProvider.isNetworkAvailable()) {
            return RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }

        val depositAddressDataResult = serverDataStore.createDepositAddressData(wallet.id, protocolId)

        if(depositAddressDataResult is Result.Failure) {
            return RepositoryResult(serverResult = depositAddressDataResult)
        }

        val depositAddressData = (depositAddressDataResult as Result.Success).value
        val updatedWallet = if(protocolId != -1) {
            wallet.copy(_multiDepositAddresses = mutableListOf<TransactionAddressData>().apply {
                for(multiDepositAddress in wallet.multiDepositAddresses) {
                    if(multiDepositAddress.protocolId != protocolId) {
                        add(multiDepositAddress)
                    }
                }

                add(depositAddressData)
            })
        } else {
            wallet.copy(depositAddressData = depositAddressData)
        }

        save(updatedWallet)

        return RepositoryResult(serverResult = Result.Success(updatedWallet))
    }


    @Synchronized
    override suspend fun deleteAll() {
        databaseDataStore.deleteAll()
        cacheDataStore.deleteAll()
    }


    @Synchronized
    override suspend fun clear() {
        deleteAll()

        freshDataHandler.reset()
    }


    @Synchronized
    override suspend fun search(params: WalletParameters): RepositoryResult<List<Wallet>> {
        // Fetching the data to make sure it is present since the search is
        // performed solely on database records
        val result = getAll(params)

        return if(result.isSuccessful()) {
            RepositoryResult(databaseResult = databaseDataStore.search(params))
        } else {
            result
        }
    }


    @Synchronized
    override suspend fun getByCurrencyIds(currencyIds: List<Int>): RepositoryResult<List<Wallet>> {
        val walletsResult = getAll(WalletParameters.getDefaultParameters())

        if(walletsResult.isErroneous()) {
            return RepositoryResult.newErroneousInstance(walletsResult)
        }

        val walletsMap = walletsResult.getSuccessfulResultValue().mapToCurrencyIdWalletMap()
        val wallets = mutableListOf<Wallet>().apply {
            for(currencyId in currencyIds) {
                if(!walletsMap.containsKey(currencyId)) {
                    continue
                }

                add(walletsMap.getValue(currencyId))
            }
        }

        return RepositoryResult.newSuccessfulInstance(
            successfulResult = walletsResult,
            successfulValue = wallets
        )
    }


    @Synchronized
    override suspend fun get(walletId: Long): RepositoryResult<Wallet> {
        cacheDataStore.get(walletId).also {
            if(it is Result.Success) {
                return@get RepositoryResult(cacheResult = it)
            }
        }

        val result = RepositoryResult<Wallet>()

        if(connectionProvider.isNetworkAvailable()) {
            result.serverResult = serverDataStore.get(walletId)

            if(result.isServerResultSuccessful()) {
                save(result.getSuccessfulResultValue())
            }
        }

        if(result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.get(walletId)

            if(result.isDatabaseResultSuccessful()) {
                cacheDataStore.save(result.getSuccessfulResultValue())
            }
        }

        return result
    }


    @Synchronized
    override suspend fun getAll(params: WalletParameters): RepositoryResult<List<Wallet>> {
        val result = RepositoryResult<List<Wallet>>()

        if(freshDataHandler.shouldLoadFreshData(connectionProvider)) {
            result.serverResult = serverDataStore.getAll(params)

            if(result.isServerResultSuccessful()) {
                deleteAll()
                save(result.getSuccessfulResultValue())
            }
        }

        if(result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.getAll(params)
        }

        return result
    }


}