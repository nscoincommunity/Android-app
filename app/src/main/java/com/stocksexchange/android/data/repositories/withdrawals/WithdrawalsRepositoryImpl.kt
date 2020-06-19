package com.stocksexchange.android.data.repositories.withdrawals

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.data.stores.withdrawals.WithdrawalsDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.SimpleFreshDataHandler
import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import com.stocksexchange.api.model.rest.parameters.WithdrawalConfirmationParameters
import com.stocksexchange.api.model.rest.parameters.WithdrawalCreationParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.exceptions.NoInternetException

class WithdrawalsRepositoryImpl(
    private val serverDataStore: WithdrawalsDataStore,
    private val databaseDataStore: WithdrawalsDataStore,
    private val freshDataHandler: SimpleFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : WithdrawalsRepository {


    @Synchronized
    override suspend fun refresh() {
        freshDataHandler.refresh()
    }


    @Synchronized
    override suspend fun save(withdrawal: Withdrawal) {
        databaseDataStore.save(withdrawal)
    }


    @Synchronized
    override suspend fun save(withdrawals: List<Withdrawal>) {
        databaseDataStore.save(withdrawals)
    }


    @Synchronized
    override suspend fun create(params: WithdrawalCreationParameters): RepositoryResult<Withdrawal> {
        return performTask {
            serverDataStore.create(params).also {
                if(it is Result.Success) {
                    save(it.value)
                }
            }
        }
    }


    @Synchronized
    override suspend fun sendConfirmationEmail(withdrawalId: Long): RepositoryResult<WithdrawalConfirmationEmailSendingResponse> {
        return performTask {
            serverDataStore.sendConfirmationEmail(withdrawalId)
        }
    }


    @Synchronized
    override suspend fun confirm(params: WithdrawalConfirmationParameters): RepositoryResult<WithdrawalConfirmationResponse> {
        return performTask {
            serverDataStore.confirm(params)
        }
    }


    @Synchronized
    override suspend fun cancel(withdrawalId: Long): RepositoryResult<WithdrawalCancellationResponse> {
        return performTask {
            serverDataStore.cancel(withdrawalId)
        }
    }


    private suspend fun <T> performTask(getServerResult: suspend (() -> Result<T>)): RepositoryResult<T> {
        if(!connectionProvider.isNetworkAvailable()) {
            return RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }

        return RepositoryResult(serverResult = getServerResult())
    }


    @Synchronized
    override suspend fun deleteAll() {
        databaseDataStore.deleteAll()
    }


    @Synchronized
    override suspend fun clear() {
        deleteAll()

        freshDataHandler.reset()
    }


    @Synchronized
    override suspend fun search(params: TransactionParameters): RepositoryResult<List<Withdrawal>> {
        // Fetching the data to make sure it is present since the search is
        // performed solely on database records
        val result = get(params)

        return if(result.isSuccessful()) {
            RepositoryResult(databaseResult = databaseDataStore.search(params))
        } else {
            result
        }
    }


    @Synchronized
    override suspend fun get(params: TransactionParameters): RepositoryResult<List<Withdrawal>> {
        val result = RepositoryResult<List<Withdrawal>>()

        if(freshDataHandler.shouldLoadFreshData(connectionProvider)) {
            result.serverResult = serverDataStore.get(params)

            if(result.isServerResultSuccessful()) {
                save(result.getSuccessfulResultValue())
            }
        }

        if(result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.get(params)
        }

        return result
    }


}