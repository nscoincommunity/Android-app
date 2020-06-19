package com.stocksexchange.android.utils

import android.os.Bundle
import com.stocksexchange.android.Constants
import com.stocksexchange.android.model.NavigationDeepLinkData
import com.stocksexchange.android.utils.helpers.tag
import com.stocksexchange.core.utils.extensions.getParcelableOrDefault

class NavigationDeepLinkHandler(
    private val navigator: Navigator
) {


    companion object {

        private val CLASS = NavigationDeepLinkHandler::class.java

        val KEY_NAVIGATION_DEEP_LINK_DATA = tag(CLASS, "navigation_deep_link_data")

        private val KEY_WAS_DEEP_LINK_HANDLED = tag(CLASS, "was_deep_link_handled")

    }


    private var mWasDeepLinkHandled: Boolean = false

    private var mNavigationDeepLinkData: NavigationDeepLinkData = NavigationDeepLinkData.STUB




    fun handleDeepLink() {
        if(mNavigationDeepLinkData.isStub || mWasDeepLinkHandled) {
            return
        }

        mWasDeepLinkHandled = true

        navigator.navigateTo(
            destinationId = mNavigationDeepLinkData.destinationId,
            destinationArgs = mNavigationDeepLinkData.destinationArgs?.apply {
                // Need to do this because sometimes class loader is null somehow
                classLoader = Constants.CLASS_LOADER
            }
        )
    }


    fun onFetchExtras(state: Bundle) = fetchDataFromBundle(state)


    fun onRestoreState(state: Bundle) = fetchDataFromBundle(state)


    private fun fetchDataFromBundle(bundle: Bundle) = with(bundle) {
        mWasDeepLinkHandled = getBoolean(KEY_WAS_DEEP_LINK_HANDLED, false)
        mNavigationDeepLinkData = getParcelableOrDefault(
            KEY_NAVIGATION_DEEP_LINK_DATA,
            NavigationDeepLinkData.STUB
        )
    }


    fun onSaveState(state: Bundle) = with(state) {
        putBoolean(KEY_WAS_DEEP_LINK_HANDLED, mWasDeepLinkHandled)
        putParcelable(KEY_NAVIGATION_DEEP_LINK_DATA, mNavigationDeepLinkData)
    }


    interface Navigator {

        fun navigateTo(destinationId: Int, destinationArgs: Bundle?)

    }


}