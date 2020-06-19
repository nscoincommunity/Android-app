package com.stocksexchange.android.data.repositories.freshdatahandlers.concrete

import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.SimpleFreshDataHandler
import com.stocksexchange.core.providers.ConnectionProvider

/**
 * A simple fresh data handler implementation that uses a
 * single boolean value for determining whether to load
 * fresh data or not.
 */
class SimpleFreshDataHandlerImpl : SimpleFreshDataHandler {


    private var mShouldLoadFreshData: Boolean = true




    override fun refresh() {
        mShouldLoadFreshData = true
    }


    override fun reset() {
        mShouldLoadFreshData = true
    }


    override fun shouldLoadFreshData(connectionProvider: ConnectionProvider): Boolean {
        val result = (connectionProvider.isNetworkAvailable() && mShouldLoadFreshData)

        if(result) {
            mShouldLoadFreshData = false
        }

        return result
    }


}