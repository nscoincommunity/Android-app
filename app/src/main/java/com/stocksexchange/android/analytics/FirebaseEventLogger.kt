package com.stocksexchange.android.analytics

import android.app.Activity

interface FirebaseEventLogger {

    fun onScreenOpened(activity: Activity, screenName: String)

    fun onCurrencyMarketsContainerSearchButtonClicked()

    fun onAboutVisitOurWebsiteButtonClicked()

    fun onAboutOurTermsOfUseButtonClicked()

    fun onAboutPrivacyPolicyButtonClicked()

    fun onAboutCandyLinkButtonClicked()

    fun onIntercomUnidentifiableUserRegistered()

    fun onIntercomIdentifiableUserRegistered()

    fun onMarketPreviewChartsBecameVisible()

    fun onMarketPreviewChartsBecameInvisible()

    fun onMarketPreviewCurrencyMarketFavorited(pairName: String)

    fun onMarketPreviewPriceChartSelected()

    fun onMarketPreviewDepthChartSelected()

    fun onMarketPreviewOrderbookSelected()

    fun onMarketPreviewTradeHistorySelected()

    fun onMarketPreviewMyOrdersSelected()

    fun onMarketPreviewMyHistorySelected()

    fun onTradingStopLimitOrderTypeSelected()

    fun onRemoteServiceApiBaseUrlSelected(url: String)

    fun onRemoteServiceApiBaseUrlUnavailable(url: String)

    fun onRemoteServiceUrlsHaveDifferentDomainName(apiBaseUrl: String, rssUrl: String, socketUrl: String)

}