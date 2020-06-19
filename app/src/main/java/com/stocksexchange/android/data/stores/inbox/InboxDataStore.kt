package com.stocksexchange.android.data.stores.inbox

import com.stocksexchange.api.model.rest.InboxDeleteItemResponse
import com.stocksexchange.api.model.rest.Inbox
import com.stocksexchange.api.model.rest.InboxGetUnreadCountResponse
import com.stocksexchange.api.model.rest.InboxSetReadAllResponse
import com.stocksexchange.android.data.base.InboxData
import com.stocksexchange.core.model.Result

interface InboxDataStore :
    InboxData<Result<List<Inbox>>,
    Result<InboxDeleteItemResponse>,
    Result<InboxSetReadAllResponse>,
    Result<InboxGetUnreadCountResponse>
>