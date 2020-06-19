package com.stocksexchange.core.utils.listeners

import com.stocksexchange.core.utils.listeners.adapters.TextWatcherAdapter

/**
 * An implementation of the TextWatcher listener that provides
 * callbacks to get notified when the query is entered and removed.
 */
class QueryListener(private val callback: Callback) : TextWatcherAdapter {


    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        if(text.isNotEmpty()) {
            callback.onQueryEntered(text.toString())
        } else {
            callback.onQueryRemoved()
        }
    }


    /**
     * A helper interface to get notified whenever a query is entered
     * or is removed.
     */
    interface Callback {

        /**
         * Gets called whenever a character is entered.
         *
         * @param query The whole query entered
         */
        fun onQueryEntered(query: String) {
            // Stub
        }

        /**
         * Gets called whenever a query is removed.
         */
        fun onQueryRemoved() {
            // Stub
        }

    }


}