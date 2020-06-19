package com.stocksexchange.android.utils.helpers

import com.stocksexchange.android.data.stores.base.BaseCacheDataStore

/**
 * Adds or updates an item inside the passed in cache.
 *
 * @param cache The cache to add an item to or update in it
 * @param item The item itself to add or update
 * @param areItemsPresent The method that checks whether the cache items
 * are present or not
 * @param fetchItems The method that retrieves items from the cache
 * @param areEqual The method that checks whether the two items are equal
 * @param getIndexToInsertAt The method that retrieves an index to insert
 * the item at
 * @param saveCache The method that actually saves items in the cache
 */
fun <Cache: BaseCacheDataStore, Item> addOrUpdateItemToCache(
    cache: Cache,
    item: Item,
    areItemsPresent: (Cache.() -> Boolean),
    fetchItems: (Cache.() -> List<Item>),
    areEqual: ((Item, Item) -> Boolean),
    getIndexToInsertAt: ((List<Item>, Item) -> Int),
    saveCache: (Cache.(List<Item>) -> Unit)
) {
    val items = if(cache.areItemsPresent()) {
        val cacheItems = cache.fetchItems().toMutableList()
        var found = false

        for(index in cacheItems.indices) {
            if(areEqual(cacheItems[index], item)) {
                cacheItems[index] = item
                found = true

                break
            }
        }

        if(!found) {
            cacheItems.add(
                getIndexToInsertAt(cacheItems, item),
                item
            )
        }

        cacheItems
    } else {
        listOf(item)
    }

    cache.saveCache(items)
}


/**
 * Removes the item from the cache if present. Otherwise, does nothing.
 *
 * @param cache The cache to remove an item from
 * @param item The item itself to remove
 * @param areItemsPresent The method that checks whether the cache items
 * are present or not
 * @param fetchItems The method that retrieves items from the cache
 * @param areEqual The method that checks whether the two items are equal
 * @param saveCache The method that actually saves items in the cache
 */
fun <Cache: BaseCacheDataStore, Item> removeItemFromCache(
    cache: Cache,
    item: Item,
    areItemsPresent: (Cache.() -> Boolean),
    fetchItems: (Cache.() -> List<Item>),
    areEqual: ((Item, Item) -> Boolean),
    saveCache: (Cache.(List<Item>) -> Unit)
) {
    if(!cache.areItemsPresent()) {
        return
    }

    val cacheItems = cache.fetchItems().toMutableList()
    var indexToRemove: Int? = null

    for(index in cacheItems.indices) {
        if(areEqual(cacheItems[index], item)) {
            indexToRemove = index
            break
        }
    }

    if(indexToRemove != null) {
        cacheItems.removeAt(indexToRemove)
    }

    cache.saveCache(cacheItems)
}