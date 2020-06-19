package com.stocksexchange.android.data.repositories.notification

import com.stocksexchange.api.model.rest.NotificationStatusResponse
import com.stocksexchange.api.model.rest.NotificationStatus
import com.stocksexchange.api.model.rest.NotificationTokenUpdateResponse
import com.stocksexchange.android.data.stores.notification.NotificationDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.exceptions.NoInternetException

class NotificationRepositoryImpl(
    private val serverDataStore: NotificationDataStore,
    private val connectionProvider: ConnectionProvider
) : NotificationRepository {


    override suspend fun updateNotificationToken(token: String): RepositoryResult<NotificationTokenUpdateResponse> {
        return performTask {
            serverDataStore.updateNotificationToken(token)
        }
    }


    override suspend fun updateNotificationStatus(status: NotificationStatus): RepositoryResult<NotificationStatusResponse> {
        return performTask {
            serverDataStore.updateNotificationStatus(status)
        }
    }


    private suspend fun <T> performTask(getServerResult: suspend (() -> Result<T>)): RepositoryResult<T> {
        if(!connectionProvider.isNetworkAvailable()) {
            return RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }

        return RepositoryResult(serverResult = getServerResult())
    }


}