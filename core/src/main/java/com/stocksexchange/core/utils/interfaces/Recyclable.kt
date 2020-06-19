package com.stocksexchange.core.utils.interfaces

/**
 * An interface to implement to mark a class to be recyclable.
 */
interface Recyclable {


    /**
     * This method is responsible for recycling the object's
     * recyclable components to avoid bugs and memory leaks.
     */
    fun recycle()


}