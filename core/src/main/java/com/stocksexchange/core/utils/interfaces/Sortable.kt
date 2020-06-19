package com.stocksexchange.core.utils.interfaces

/**
 * An interface to implement to mark a class to be sortable.
 */
interface Sortable {


    /**
     * This method is responsible for sorting the object's data
     * using the given payload.
     *
     * @param payload The payload to use for sorting
     */
    fun sort(payload: Any)


}