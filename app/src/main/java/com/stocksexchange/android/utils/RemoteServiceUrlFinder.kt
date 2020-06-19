package com.stocksexchange.android.utils

import com.stocksexchange.android.Constants
import com.stocksexchange.android.analytics.FirebaseEventLogger
import com.stocksexchange.api.utils.UrlBuilder
import com.stocksexchange.android.data.repositories.utilities.UtilitiesRepository
import com.stocksexchange.android.model.RemoteServiceUrls
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.handlers.CoroutineHandler
import com.stocksexchange.core.handlers.PreferenceHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteServiceUrlFinder(
    private val urlGenerator: RemoteServiceUrlGenerator,
    private val preferenceHandler: PreferenceHandler,
    private val connectionProvider: ConnectionProvider,
    private val coroutineHandler: CoroutineHandler,
    private val utilitiesRepository: UtilitiesRepository,
    private val urlBuilder: UrlBuilder,
    private val firebaseEventLogger: FirebaseEventLogger
) {


    companion object {

        private const val MAX_RETRY_COUNT = 3

    }


    private var mIsRunning: Boolean = false

    private var mBrokenRemoteServiceUrlsSet: MutableSet<RemoteServiceUrls> = mutableSetOf()

    var listener: Listener? = null




    fun run() {
        if(mIsRunning) {
            return
        }

        val urls = urlGenerator.generateUrls()

        if(!connectionProvider.isNetworkAvailable() ||
            !Constants.IS_REMOTE_SERVICES_URLS_FINDER_ENABLED) {
            onFindingProcessFinished(urls.defaultUrls)
            return
        }

        coroutineHandler.createBgLaunchCoroutine {
            val workingUrls = findWorkingUrls(urls.candidateUrls)

            withContext(Dispatchers.Main) {
                onFindingProcessFinished(workingUrls)
            }
        }

        onFindingProcessStarted()
    }


    private suspend fun findWorkingUrls(candidateUrls: List<RemoteServiceUrls>): RemoteServiceUrls? {
        var retryCount = 1

        while(retryCount <= MAX_RETRY_COUNT) {
            retryCount++

            mBrokenRemoteServiceUrlsSet.clear()

            for(item in candidateUrls) {
                if(!mBrokenRemoteServiceUrlsSet.contains(item)) {
                    val pingUrl = urlBuilder.buildPingUrl(item.apiBaseUrl)
                    val result = utilitiesRepository.ping(pingUrl).isSuccessful()

                    if(result) {
                        return item
                    } else {
                        mBrokenRemoteServiceUrlsSet.add(item)
                    }
                }
            }
        }

        return null
    }


    private fun onFindingProcessStarted() {
        mIsRunning = true

        listener?.onFindingProcessStarted()
    }


    private fun onFindingProcessEnded() {
        mIsRunning = false

        listener?.onFindingProcessEnded()
    }


    private fun onFindingProcessFinished(workingUrls: RemoteServiceUrls?) {
        onFindingProcessEnded()

        if(workingUrls != null) {
            saveWorkingUrlsToPreferences(workingUrls)
            sendDataToAnalytics(workingUrls, mBrokenRemoteServiceUrlsSet)

            listener?.onFindingProcessSucceeded(workingUrls)
        } else {
            listener?.onFindingProcessFailed()
        }
    }


    private fun saveWorkingUrlsToPreferences(workingUrls: RemoteServiceUrls) {
        with(preferenceHandler) {
            saveLastWorkingApiBaseUrl(workingUrls.apiBaseUrl)
            saveLastWorkingRssUrl(workingUrls.rssUrl)
            saveLastWorkingSocketUrl(workingUrls.socketUrl)
            saveLastWorkingHashedDomainName(workingUrls.hashedDomainName)
        }
    }


    private fun sendDataToAnalytics(workingUrls: RemoteServiceUrls,
                                    brokenRemoteServiceUrlsSet: Set<RemoteServiceUrls>) {
        firebaseEventLogger.onRemoteServiceApiBaseUrlSelected(workingUrls.apiBaseUrl)

        for(brokenRemoteServiceUrl in brokenRemoteServiceUrlsSet) {
            firebaseEventLogger.onRemoteServiceApiBaseUrlUnavailable(brokenRemoteServiceUrl.apiBaseUrl)
        }

        if(workingUrls.hasHashedDomainName) {
            val hash = workingUrls.hashedDomainName
            val apiBaseUrl = workingUrls.apiBaseUrl
            val rssUrl = workingUrls.rssUrl
            val socketUrl = workingUrls.socketUrl

            if((hash !in apiBaseUrl) || (hash !in rssUrl) || (hash !in socketUrl)) {
                firebaseEventLogger.onRemoteServiceUrlsHaveDifferentDomainName(
                    apiBaseUrl = apiBaseUrl,
                    rssUrl = rssUrl,
                    socketUrl = socketUrl
                )
            }
        }
    }


    interface Listener {

        fun onFindingProcessStarted()

        fun onFindingProcessEnded()

        fun onFindingProcessSucceeded(workingUrls: RemoteServiceUrls)

        fun onFindingProcessFailed()

    }


}