package com.stocksexchange.core.utils.interfaces

/**
 * An interface to implement to mark a class to be searchable.
 */
interface Searchable {


    /**
     * This method is called to notify you that the search
     * has to be performed with the given query.
     *
     * @param query The query to perform search on
     */
    fun onPerformSearch(query: String)


    /**
     * This method is called to notify you that the current
     * search (assuming there is one going on) has to be
     * canceled.
     */
    fun onCancelSearch()


}