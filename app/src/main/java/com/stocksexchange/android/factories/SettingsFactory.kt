package com.stocksexchange.android.factories

import com.stocksexchange.android.BuildConfig
import com.stocksexchange.api.model.rest.FiatCurrency
import com.stocksexchange.android.model.*
import com.stocksexchange.android.model.Settings.Companion.SETTINGS_ID
import com.stocksexchange.android.theming.factories.ThemeFactory
import com.stocksexchange.android.utils.providers.InitialLanguageProvider

/**
 * A factory producing instances of the [Settings] class.
 */
class SettingsFactory(
    private val initialLanguageProvider: InitialLanguageProvider,
    private val themeFactory: ThemeFactory
) {


    fun getDefaultSettings(): Settings {
        return Settings(
            id = SETTINGS_ID,
            isFingerprintUnlockEnabled = false,
            isNotificationEnabled = true,
            isForceAuthenticationOnAppStartupEnabled = !BuildConfig.DEBUG,
            isOrderbookRealTimeUpdatesHighlightingEnabled = true,
            isNewTradesRealTimeAdditionHighlightingEnabled = true,
            isPriceChartZoomInEnabled = false,
            shouldAnimateCharts = true,
            shouldKeepScreenOn = false,
            areMarketPreviewChartsVisible = true,
            areAnalyticsToastsEnabled = false,
            areEmptyWalletsHidden = false,
            pinCode = PinCode.getEmptyPinCode(),
            bullishCandleStickStyle = CandleStickStyle.SOLID,
            bearishCandleStickStyle = CandleStickStyle.SOLID,
            depthChartLineStyle = DepthChartLineStyle.LINEAR,
            authenticationSessionDuration = getDefaultAuthenticationSessionDuration(),
            language = initialLanguageProvider.getInitialLanguage(),
            fiatCurrency = FiatCurrency.USD,
            theme = themeFactory.getDefaultTheme()
        )
    }


    /**
     * Returns settings after user logs out by adjusting specific
     * fields related to the user.
     *
     * @param oldSettings The old settings
     *
     * @return The new settings after user logout
     */
    fun getSettingsAfterUserLogout(oldSettings: Settings): Settings {
        return oldSettings.copy(
            pinCode = PinCode.getEmptyPinCode(),
            isFingerprintUnlockEnabled = false,
            isForceAuthenticationOnAppStartupEnabled = !BuildConfig.DEBUG,
            authenticationSessionDuration = getDefaultAuthenticationSessionDuration()
        )
    }


    private fun getDefaultAuthenticationSessionDuration(): AuthenticationSessionDuration {
        return if(BuildConfig.DEBUG) {
            AuthenticationSessionDuration.TWELVE_HOURS
        } else {
            AuthenticationSessionDuration.FIVE_MINUTES
        }
    }


}