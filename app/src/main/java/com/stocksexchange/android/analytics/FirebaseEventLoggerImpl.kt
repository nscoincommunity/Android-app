package com.stocksexchange.android.analytics

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.stocksexchange.android.R
import com.stocksexchange.android.analytics.model.Event
import com.stocksexchange.android.analytics.model.Parameter
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.utils.extensions.shortToast

@SuppressLint("DefaultLocale")
class FirebaseEventLoggerImpl(
    private val context: Context,
    private val stringProvider: StringProvider,
    private val sessionManager: SessionManager
) : FirebaseEventLogger {


    private val mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)




    private fun showAnalyticsToast(message: String, showToast: Boolean = true) {
        if(sessionManager.getSettings().areAnalyticsToastsEnabled && showToast) {
            context.shortToast(message)
        }
    }


    private fun logEvent(event: Event, params: Bundle? = null, showToast: Boolean = true) {
        val eventName = event.name.toLowerCase()

        mFirebaseAnalytics.logEvent(eventName, params)

        showAnalyticsToast(
            if (params == null) {
                stringProvider.getString(
                    R.string.firebase_analytics_event_without_params_template,
                    eventName
                )
            } else {
                stringProvider.getString(
                    R.string.firebase_analytics_event_with_params_template,
                    eventName,
                    params.toString()
                )
            },
            showToast
        )
    }


    override fun onScreenOpened(activity: Activity, screenName: String) {
        mFirebaseAnalytics.setCurrentScreen(activity, screenName, screenName)

        showAnalyticsToast(stringProvider.getString(
            R.string.firebase_analytics_current_screen_template,
            screenName
        ))
    }


    override fun onCurrencyMarketsContainerSearchButtonClicked() {
        logEvent(Event.UI, bundleOf(
            Parameter.SEARCH_START.paramName to true
        ))
    }


    override fun onAboutVisitOurWebsiteButtonClicked() {
        logEvent(Event.UI, bundleOf(
            Parameter.VISIT_OUR_WEBSITE.paramName to true
        ))
    }


    override fun onAboutOurTermsOfUseButtonClicked() {
        logEvent(Event.UI, bundleOf(
            Parameter.OUR_TERMS_OF_USE.paramName to true
        ))
    }


    override fun onAboutPrivacyPolicyButtonClicked() {
        logEvent(Event.UI, bundleOf(
            Parameter.PRIVACY_POLICY.paramName to true
        ))
    }


    override fun onAboutCandyLinkButtonClicked() {
        logEvent(Event.UI, bundleOf(
            Parameter.CANDY_LINK.paramName to true
        ))
    }


    override fun onIntercomUnidentifiableUserRegistered() {
        logEvent(Event.INTERCOM, bundleOf(
            Parameter.REGISTER_UNIDENTIFIABLE_USER.paramName to true
        ))
    }


    override fun onIntercomIdentifiableUserRegistered() {
        logEvent(Event.INTERCOM, bundleOf(
            Parameter.REGISTER_IDENTIFIABLE_USER.paramName to true
        ))
    }


    override fun onMarketPreviewChartsBecameVisible() {
        logEvent(Event.UI, bundleOf(
            Parameter.SHOW_PRICE_DEPTH_CHART.paramName to true
        ))
    }


    override fun onMarketPreviewChartsBecameInvisible() {
        logEvent(Event.UI, bundleOf(
            Parameter.HIDE_PRICE_DEPTH_CHART.paramName to true
        ))
    }


    override fun onMarketPreviewCurrencyMarketFavorited(pairName: String) {
        logEvent(Event.UI, bundleOf(
            Parameter.SAVE_FAVORITES.paramName to pairName
        ))
    }


    override fun onMarketPreviewPriceChartSelected() {
        logEvent(Event.UI, bundleOf(
            Parameter.PRICE_CHART.paramName to true
        ))
    }


    override fun onMarketPreviewDepthChartSelected() {
        logEvent(Event.UI, bundleOf(
            Parameter.DEPTH_CHART.paramName to true
        ))
    }


    override fun onMarketPreviewOrderbookSelected() {
        logEvent(Event.UI, bundleOf(
            Parameter.ORDERBOOK.paramName to true
        ))
    }


    override fun onMarketPreviewTradeHistorySelected() {
        logEvent(Event.UI, bundleOf(
            Parameter.TRADE_HISTORY.paramName to true
        ))
    }


    override fun onMarketPreviewMyOrdersSelected() {
        logEvent(Event.UI, bundleOf(
            Parameter.MY_ORDERS.paramName to true
        ))
    }


    override fun onMarketPreviewMyHistorySelected() {
        logEvent(Event.UI, bundleOf(
            Parameter.MY_HISTORY.paramName to true
        ))
    }


    override fun onTradingStopLimitOrderTypeSelected() {
        logEvent(Event.UI, bundleOf(
            Parameter.STOP_LIMIT.paramName to true
        ))
    }


    override fun onRemoteServiceApiBaseUrlSelected(url: String) {
        logEvent(Event.REMOTE_SERVICE_URLS, bundleOf(
            Parameter.WORKING_API_BASE_URL.paramName to url)
        )
    }


    override fun onRemoteServiceApiBaseUrlUnavailable(url: String) {
        logEvent(
            Event.REMOTE_SERVICE_URLS,
            bundleOf(Parameter.NOT_WORKING_API_BASE_URL.paramName to url),
            false
        )
    }


    override fun onRemoteServiceUrlsHaveDifferentDomainName(apiBaseUrl: String,
                                                            rssUrl: String,
                                                            socketUrl: String) {
        val urls = "$apiBaseUrl $rssUrl $socketUrl"

        logEvent(
            Event.REMOTE_SERVICE_URLS,
            bundleOf(Parameter.DIFFENT_DOMAINS_IN_URLS.paramName to urls),
            false
        )
    }


}