package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.api.model.rest.FiatCurrency
import com.stocksexchange.android.theming.model.Theme
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Settings(
    val id: Int,
    val isFingerprintUnlockEnabled: Boolean,
    val isNotificationEnabled: Boolean,
    val isForceAuthenticationOnAppStartupEnabled: Boolean,
    val isOrderbookRealTimeUpdatesHighlightingEnabled: Boolean,
    val isNewTradesRealTimeAdditionHighlightingEnabled: Boolean,
    val isPriceChartZoomInEnabled: Boolean,
    val areMarketPreviewChartsVisible: Boolean,
    val areAnalyticsToastsEnabled: Boolean,
    val areEmptyWalletsHidden: Boolean,
    val shouldAnimateCharts: Boolean,
    val shouldKeepScreenOn: Boolean,
    val pinCode: PinCode,
    val bullishCandleStickStyle: CandleStickStyle,
    val bearishCandleStickStyle: CandleStickStyle,
    val depthChartLineStyle: DepthChartLineStyle,
    val authenticationSessionDuration: AuthenticationSessionDuration,
    val language: Language,
    val fiatCurrency: FiatCurrency,
    val theme: Theme
) : Parcelable {


    companion object {

        const val SETTINGS_ID = 1

    }


    val hasPinCode: Boolean
        get() = pinCode.hasCode()


    val isOrderbookHighlightingEnabled: Boolean
        get() = isOrderbookRealTimeUpdatesHighlightingEnabled


    val isNewTradesHighlightingEnabled: Boolean
        get() = isNewTradesRealTimeAdditionHighlightingEnabled


}