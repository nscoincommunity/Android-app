package com.stocksexchange.core.utils.extensions


/**
 * Gets a value from the map if the key exists or returns
 * a default value.
 *
 * @param key The key to search a value for
 * @param default The default value to return if the key is absent
 *
 * @return The value from the map or the default value
 */
fun <K, V> Map<K, V>.getWithDefault(key: K, default: V): V {
    return if(containsKey(key)) {
        get(key) ?: default
    } else {
        default
    }
}