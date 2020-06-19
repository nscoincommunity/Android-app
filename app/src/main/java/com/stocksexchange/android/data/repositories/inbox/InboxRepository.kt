package com.stocksexchange.android.data.repositories.inbox

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.data.base.InboxData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.api.model.rest.parameters.InboxParameters

interface InboxRepository : InboxData<
    RepositoryResult<List<Inbox>>,
    RepositoryResult<InboxDeleteItemResponse>,
    RepositoryResult<InboxSetReadAllResponse>,
    RepositoryResult<InboxGetUnreadCountResponse>
>, Repository {

    suspend fun refresh(params: InboxParameters)

}