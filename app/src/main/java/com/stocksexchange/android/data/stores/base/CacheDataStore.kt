package com.stocksexchange.android.data.stores.base

interface CacheDataStore {

    suspend fun clear()

    suspend fun isEmpty(): Boolean

}