package com.stocksexchange.android.data.repositories.notification

import com.stocksexchange.api.model.rest.NotificationStatusResponse
import com.stocksexchange.api.model.rest.NotificationTokenUpdateResponse
import com.stocksexchange.android.data.base.NotificationData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface NotificationRepository : NotificationData<
    RepositoryResult<NotificationStatusResponse>,
    RepositoryResult<NotificationTokenUpdateResponse>
>, Repository