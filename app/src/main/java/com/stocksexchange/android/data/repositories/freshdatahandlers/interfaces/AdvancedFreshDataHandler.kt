package com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces

import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.utils.interfaces.HasUniqueKey

/**
 * An advanced fresh data handler that requires extra data to
 * determine whether to load fresh data or not. See [SimpleFreshDataHandler]
 * to see an example where extra data is not needed for determination.
 */
interface AdvancedFreshDataHandler<T : HasUniqueKey> : FreshDataHandler {


    /**
     * Should request fresh data to be downloaded on subsequent
     * data loading requests.
     *
     * @param params The extra parameters holding data that are
     * used to decide what data to refresh
     */
    fun refresh(params: T)


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
     * @param params The extra parameters holding data that are
     * used to decide what data to refresh
     *
     * @return true if fresh data should be loaded; false otherwise
     */
    fun shouldLoadFreshData(connectionProvider: ConnectionProvider, params: T): Boolean


}