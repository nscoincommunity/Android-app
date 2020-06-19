package com.stocksexchange.android.data.stores.notification

import com.stocksexchange.api.model.rest.NotificationStatusResponse
import com.stocksexchange.api.model.rest.NotificationTokenUpdateResponse
import com.stocksexchange.android.data.base.NotificationData
import com.stocksexchange.core.model.Result

interface NotificationDataStore : NotificationData<
    Result<NotificationStatusResponse>, Result<NotificationTokenUpdateResponse>
>