package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.NotificationStatus

interface NotificationData<
    NotificationResult,
    NotificationTokenUpdateResult
> {

    suspend fun updateNotificationToken(token: String): NotificationTokenUpdateResult

    suspend fun updateNotificationStatus(status: NotificationStatus): NotificationResult

}
