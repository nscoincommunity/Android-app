package com.stocksexchange.android.utils

import androidx.annotation.VisibleForTesting
import com.stocksexchange.android.Constants
import com.stocksexchange.android.model.RemoteServiceUrls
import com.stocksexchange.android.model.RemoteServiceUrlsWrapper
import com.stocksexchange.core.handlers.PreferenceHandler

class RemoteServiceUrlGenerator(
    private val preferenceHandler: PreferenceHandler
) {


    companion object {

        @VisibleForTesting
        const val SUBDOMAIN_API3 = "api3."

        @VisibleForTesting
        const val SUBDOMAIN_SOCKET = "socket."

        @VisibleForTesting
        const val SUBDOMAIN_RSS = ""

    }


    fun generateUrls(): RemoteServiceUrlsWrapper {
        val defaultUrls = getDefaultUrls()
        val lastWorkingUrls = getDefaultUrls()
        val candidateUrls = listOf(getDefaultUrls())

        return RemoteServiceUrlsWrapper(
            defaultUrls = defaultUrls,
            _lastWorkingUrls = lastWorkingUrls,
            _candidateUrls = candidateUrls
        )
    }


    private fun getDefaultUrls(): RemoteServiceUrls {
        return RemoteServiceUrls(
            apiBaseUrl = Constants.STEX_API_BASE_URL,
            rssUrl = Constants.STEX_WEBSITE_URL,
            socketUrl = Constants.STEX_SOCKET_URL
        )
    }


}