package com.stocksexchange.android.utils.handlers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.android.receivers.CustomTabsReceiver
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.utils.providers.CustomTabsProvider
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.utils.extensions.canIntentBeHandled
import com.stocksexchange.core.utils.extensions.shortToast

class BrowserHandler(
    private val customTabsProvider: CustomTabsProvider,
    private val stringProvider: StringProvider
) {


    /**
     * Launches a browser (either Chrome Custom Tabs or a standard web-view
     * if Chrome is not installed on the device) to view a specific url.
     *
     * @param url The url to view
     */
    fun launchBrowser(context: Context, url: String, theme: Theme) {
        val uri = url.toUri()

        if(customTabsProvider.hasSupportForCustomTabs()) {
            val intentBuilder = CustomTabsIntent.Builder()
            intentBuilder.setShowTitle(true)
            intentBuilder.addMenuItem(
                stringProvider.getString(R.string.menu_item_share_via),
                CustomTabsReceiver.init(context, uri, Constants.REQUEST_CODE_SHARE_VIA)
            )
            intentBuilder.addMenuItem(
                stringProvider.getString(R.string.menu_item_copy_link),
                CustomTabsReceiver.init(context, uri, Constants.REQUEST_CODE_COPY_LINK)
            )

            ThemingUtil.apply(intentBuilder, theme)

            val customTabsIntent = intentBuilder.build()
            customTabsIntent.intent.`package` = customTabsProvider.getPackageNameToUse()
            customTabsIntent.launchUrl(context, uri)
        } else {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = uri

                if(context !is AppCompatActivity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }

            if(context.canIntentBeHandled(intent)) {
                context.startActivity(intent)
            } else {
                context.shortToast(stringProvider.getString(
                    R.string.error_no_browser_client_template,
                    stringProvider.getString(R.string.error_unable_to_proceed)
                ))
            }
        }
    }


}