package com.stocksexchange.android.data.stores.tickeritems

import com.stocksexchange.api.model.rest.TickerItem
import com.stocksexchange.android.data.base.TickerItemsData
import com.stocksexchange.core.model.Result

interface TickerItemsDataStore : TickerItemsData<
    Result<TickerItem>,
    Result<List<TickerItem>>
>