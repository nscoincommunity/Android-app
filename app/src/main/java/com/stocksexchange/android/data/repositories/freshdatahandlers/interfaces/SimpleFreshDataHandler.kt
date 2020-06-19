package com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces

import com.stocksexchange.core.providers.ConnectionProvider

/**
 * A simple fresh data handler that does not require any extra
 * data to determine whether to load fresh data or not. See
 * [AdvancedFreshDataHandler] to see an example where extra data
 * is needed for determination.
 */
interface SimpleFreshDataHandler : FreshDataHandler {


    /**
     * Should request fresh data to be downloaded on subsequent
     * data loading requests.
     */
    fun refresh()


    /**
     * Should reset the fresh data handler to its initial state.
     */
    fun reset()


    /**
     * Should return a boolean value denoting whether the
     * fresh data should be loaded or not.
     *
     * @param connectionProvider The connection provider to use
     * to check for network availability
     *
     * @return true if fresh data should be loaded; false otherwise
     */
    fun shouldLoadFreshData(connectionProvider: ConnectionProvider): Boolean


}