package com.stocksexchange.android.data.repositories.tickeritems

import com.stocksexchange.api.model.rest.TickerItem
import com.stocksexchange.android.data.base.TickerItemsData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface TickerItemsRepository : TickerItemsData<
    RepositoryResult<TickerItem>,
    RepositoryResult<List<TickerItem>>
>, Repository {

    suspend fun refresh()

}