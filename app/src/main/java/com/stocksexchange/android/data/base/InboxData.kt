package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.Inbox
import com.stocksexchange.api.model.rest.parameters.InboxParameters

interface InboxData<
    InboxFetchingResult,
    InboxDeleteResult,
    InboxReadAllResult,
    InboxUnreadCountResult
> {

    suspend fun save(inbox: List<Inbox>)

    suspend fun deleteAll()

    suspend fun get(params: InboxParameters): InboxFetchingResult

    suspend fun deleteInboxItem(id: String): InboxDeleteResult

    suspend fun setInboxReadAll(): InboxReadAllResult

    suspend fun getInboxUnreadCount(): InboxUnreadCountResult

}