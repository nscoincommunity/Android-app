package com.stocksexchange.android.data.stores.notification

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.NotificationStatusResponse
import com.stocksexchange.api.model.rest.NotificationStatus
import com.stocksexchange.api.model.rest.NotificationTokenUpdateResponse
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class NotificationServerDataStore(
    private val stexRestApi: StexRestApi
) : NotificationDataStore {


    override suspend fun updateNotificationToken(token: String): Result<NotificationTokenUpdateResponse> {
        return performBackgroundOperation {
            stexRestApi.updateNotificationToken(token)
        }
    }


    override suspend fun updateNotificationStatus(status: NotificationStatus): Result<NotificationStatusResponse> {
        return performBackgroundOperation {
            stexRestApi.setNotificationStatus(status)
        }
    }


}