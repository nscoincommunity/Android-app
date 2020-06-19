package com.stocksexchange.android.data.repositories.inbox

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.InboxFreshDataHandler
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.data.stores.inbox.InboxDataStore
import com.stocksexchange.api.model.rest.parameters.InboxParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.exceptions.NoInternetException

class InboxRepositoryImpl(
    private val serverDataStore: InboxDataStore,
    private val databaseDataStore: InboxDataStore,
    private val freshDataHandler: InboxFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : InboxRepository {


    @Synchronized
    override suspend fun refresh(params: InboxParameters) {
        freshDataHandler.refresh(params)
    }


    @Synchronized
    override suspend fun save(inbox: List<Inbox>) {
        databaseDataStore.save(inbox)
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
    override suspend fun get(params: InboxParameters): RepositoryResult<List<Inbox>> {
        val result = RepositoryResult<List<Inbox>>()

        if (freshDataHandler.shouldLoadFreshData(connectionProvider, params)) {
            result.serverResult = serverDataStore.get(params)

            if (result.isServerResultSuccessful()) {
                save(result.getSuccessfulResultValue())
            }
        }

        if (result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.get(params)
        }

        return result
    }


    override suspend fun deleteInboxItem(id: String): RepositoryResult<InboxDeleteItemResponse> {
        return performTask {
            serverDataStore.deleteInboxItem(id)
            databaseDataStore.deleteInboxItem(id)
        }
    }


    override suspend fun setInboxReadAll(): RepositoryResult<InboxSetReadAllResponse> {
        return performTask {
            serverDataStore.setInboxReadAll()
        }
    }


    override suspend fun getInboxUnreadCount(): RepositoryResult<InboxGetUnreadCountResponse> {
        return performTask {
            serverDataStore.getInboxUnreadCount()
        }
    }


    private suspend fun <T> performTask(getServerResult: suspend (() -> Result<T>)): RepositoryResult<T> {
        if(!connectionProvider.isNetworkAvailable()) {
            return RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }

        return RepositoryResult(serverResult = getServerResult())
    }


}