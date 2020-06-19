package com.stocksexchange.cache

/**
 * A cache that resides inside the memory of the device.
 */
class MemoryCache<Key, Value> : Cache<Key, Value> {


    private val cacheMap: HashMap<Key, Value> = HashMap()




    override fun put(key: Key, value: Value): Value? {
        return cacheMap.put(key, value)
    }


    override fun get(key: Key, defaultValue: Value?): Value? {
        return if(contains(key)) {
            cacheMap[key]
        } else {
            defaultValue
        }
    }


    override fun remove(key: Key, defaultValue: Value?): Value? {
        return if(contains(key)) {
            cacheMap.remove(key)
        } else {
            defaultValue
        }
    }


    override fun clear() {
        cacheMap.clear()
    }


    override fun contains(key: Key): Boolean {
        return cacheMap.contains(key)
    }


    override fun isEmpty(): Boolean {
        return cacheMap.isEmpty()
    }


    override fun getKeys(): List<Key> {
        return mutableListOf<Key>().apply {
            for(entry in cacheMap) {
                add(entry.key)
            }
        }
    }


    override fun getValues(): List<Value> {
        return mutableListOf<Value>().apply {
            for(entry in cacheMap) {
                add(entry.value)
            }
        }
    }


}