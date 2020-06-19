package com.stocksexchange.android.data.repositories.freshdatahandlers.concrete

import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.AdvancedFreshDataHandler
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.utils.extensions.getWithDefault
import com.stocksexchange.core.utils.interfaces.HasUniqueKey

/**
 * An advanced fresh data handler implementation that uses a map
 * for determining whether to load fresh data or not.
 */
open class AdvancedFreshDataHandlerImpl<T : HasUniqueKey> : AdvancedFreshDataHandler<T> {


    protected val mShouldLoadFreshDataMap: MutableMap<String, Boolean> = mutableMapOf()




    override fun refresh(params: T) {
        mShouldLoadFreshDataMap[params.uniqueKey] = true
    }


    override fun reset() {
        mShouldLoadFreshDataMap.clear()
    }


    override fun shouldLoadFreshData(connectionProvider: ConnectionProvider,
                                     params: T): Boolean {
        val result = (connectionProvider.isNetworkAvailable() &&
                      mShouldLoadFreshDataMap.getWithDefault(params.uniqueKey, true))

        if(result) {
            mShouldLoadFreshDataMap[params.uniqueKey] = false
        }

        return result
    }


}