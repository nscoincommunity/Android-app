package com.stocksexchange.cache

/**
 * A factory for returning a specific [Cache] implementation.
 */
class CacheFactory {


    companion object {


        /**
         * Retrieves a particular implementation of the cache
         * based on the passed parameters.
         *
         * @param isConcurrent Whether the cache should be concurrent or not
         *
         * @return The particular implementation of the cache
         */
        fun <Key, Value> getCache(isConcurrent: Boolean): Cache<Key, Value> {
            val cache = MemoryCache<Key, Value>()

            return if(isConcurrent) {
                ConcurrentCache(cache)
            } else {
                cache
            }
        }


    }


}