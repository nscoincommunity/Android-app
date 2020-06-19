package com.stocksexchange.cache

/**
 * An interface for implementing a cache.
 */
interface Cache<Key, Value> {


    /**
     * Puts a value into the cache.
     *
     * @param key The key to associate with the value
     * @param value The value to put
     *
     * @return The previous value associated with key, or
     * null if there was no mapping for key
     */
    fun put(key: Key, value: Value): Value?


    /**
     * Returns the value to which the specified key is mapped,
     * or default value if there is no mapping for the key.
     *
     * @param key The key to get the value for
     * @param defaultValue The default value if the key is
     * not mapped to any value
     *
     * @return The value the key is mapped on or default value
     */
    fun get(key: Key, defaultValue: Value? = null): Value?


    /**
     * Removes a mapping from the specified key from the cache if present.
     *
     * @param key The key to remove the value for
     *
     * @return The previous value associated with key or defaultValue
     * if there was no mapping for the key
     */
    fun remove(key: Key, defaultValue: Value? = null): Value?


    /**
     * Clears all keys and their associated values from the cache.
     */
    fun clear()


    /**
     * Checks if the cache has a mapping for the specified key.
     *
     * @param key The key to check if it has a mapping
     *
     * @return true if it has; false otherwise
     */
    fun contains(key: Key): Boolean


    /**
     * Checks whether the cache has any keys and values.
     *
     * @return true if it is empty; false otherwise
     */
    fun isEmpty(): Boolean


    /**
     * Returns a collection of keys packed in an instance of immutable list.
     *
     * @return The list holding the keys of the map
     */
    fun getKeys(): List<Key>


    /**
     * Returns a collection of values packed in an instance of immutable list.
     *
     * @return The list holding the values of the map
     */
    fun getValues(): List<Value>


}