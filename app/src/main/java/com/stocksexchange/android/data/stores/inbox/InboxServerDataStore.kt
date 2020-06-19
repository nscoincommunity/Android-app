package com.stocksexchange.android.data.stores.inbox

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.api.model.rest.parameters.InboxParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class InboxServerDataStore(
    private val stexRestApi: StexRestApi
) : InboxDataStore {


    override suspend fun save(inbox: List<Inbox>) {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun get(params: InboxParameters): Result<List<Inbox>> {
        return performBackgroundOperation {
            stexRestApi.getInbox(params)
        }
    }


    override suspend fun deleteInboxItem(id: String): Result<InboxDeleteItemResponse> {
        return performBackgroundOperation {
            stexRestApi.deleteInboxItem(id)
        }
    }


    override suspend fun setInboxReadAll(): Result<InboxSetReadAllResponse> {
        return performBackgroundOperation {
            stexRestApi.setInboxReadAll()
        }
    }


    override suspend fun getInboxUnreadCount(): Result<InboxGetUnreadCountResponse> {
        return performBackgroundOperation {
            stexRestApi.getInboxUnreadCount()
        }
    }


}