package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.Inbox
import com.stocksexchange.api.model.rest.InboxDeleteItemResponse
import com.stocksexchange.android.database.daos.InboxDao
import com.stocksexchange.android.database.model.DatabaseInbox
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.mappings.mapToDatabaseInboxList
import com.stocksexchange.android.mappings.mapToInboxList
import com.stocksexchange.api.model.rest.parameters.InboxParameters
import com.stocksexchange.core.model.Result

class InboxTable(
    private val inboxDao: InboxDao
) : BaseTable() {


    @Synchronized
    fun save(inbox: List<Inbox>) {
        inboxDao.insert(inbox.mapToDatabaseInboxList())
    }


    @Synchronized
    fun deleteAll() {
        inboxDao.deleteAll()
    }


    @Synchronized
    fun get(params: InboxParameters): Result<List<Inbox>> {
        return inboxDao.get().toResult(
            List<DatabaseInbox>::mapToInboxList
        )
    }


    @Synchronized
    fun delete(inboxId: String): Result<InboxDeleteItemResponse> {
        inboxDao.delete(inboxId)

        return Result.Success(InboxDeleteItemResponse("ok"))
    }


}