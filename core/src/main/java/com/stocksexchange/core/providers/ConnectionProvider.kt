package com.stocksexchange.core.providers

import android.content.Context
import com.stocksexchange.core.utils.extensions.isNetworkAvailable

class ConnectionProvider(private val context: Context) {


    fun isNetworkAvailable() : Boolean = context.isNetworkAvailable()


}