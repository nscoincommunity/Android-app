package com.stocksexchange.android.data.stores.base

import com.stocksexchange.cache.Cache
import com.stocksexchange.cache.CacheFactory

abstract class BaseCacheDataStore : CacheDataStore {


    /**
     * A cache for storing the user-related data.
     */
    protected val cache: Cache<String, Any> = CacheFactory.getCache(true)




    override suspend fun clear() = cache.clear()


    override suspend fun isEmpty(): Boolean = cache.isEmpty()


}