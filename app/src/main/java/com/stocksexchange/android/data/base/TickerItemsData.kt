package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.TickerItem

interface TickerItemsData<
    TickerItemFetchingResult,
    TickerItemsFetchingResult
> {

    suspend fun save(tickerItem: TickerItem)

    suspend fun save(tickerItems: List<TickerItem>)

    suspend fun deleteAll()

    suspend fun get(currencyPairId: Int): TickerItemFetchingResult

    suspend fun getAll(): TickerItemsFetchingResult

}