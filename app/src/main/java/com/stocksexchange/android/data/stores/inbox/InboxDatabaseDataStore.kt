package com.stocksexchange.android.data.stores.inbox

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.database.tables.InboxTable
import com.stocksexchange.api.model.rest.parameters.InboxParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class InboxDatabaseDataStore(
    private val inboxTable: InboxTable
) : InboxDataStore {


    override suspend fun save(inbox: List<Inbox>) {
        executeBackgroundOperation {
            inboxTable.save(inbox)
        }
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            inboxTable.deleteAll()
        }
    }


    override suspend fun get(params: InboxParameters): Result<List<Inbox>> {
        return performBackgroundOperation {
            inboxTable.get(params)
        }
    }


    override suspend fun deleteInboxItem(inboxId: String): Result<InboxDeleteItemResponse> {
        return performBackgroundOperation {
            inboxTable.delete(inboxId)
        }
    }


    override suspend fun setInboxReadAll(): Result<InboxSetReadAllResponse> {
        throw UnsupportedOperationException()
    }


    override suspend fun getInboxUnreadCount(): Result<InboxGetUnreadCountResponse> {
        throw UnsupportedOperationException()
    }


}