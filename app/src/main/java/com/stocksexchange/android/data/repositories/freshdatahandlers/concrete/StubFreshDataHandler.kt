package com.stocksexchange.android.data.repositories.freshdatahandlers.concrete

import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.SimpleFreshDataHandler
import com.stocksexchange.core.providers.ConnectionProvider

class StubFreshDataHandler : SimpleFreshDataHandler {


    override fun refresh() {
        // Stub
    }


    override fun reset() {
        // Stub
    }


    override fun shouldLoadFreshData(connectionProvider: ConnectionProvider): Boolean {
        return false
    }


}