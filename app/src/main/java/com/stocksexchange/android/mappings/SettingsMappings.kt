package com.stocksexchange.android.mappings

import com.stocksexchange.android.database.model.DatabaseSettings
import com.stocksexchange.android.model.Settings

fun Settings.mapToDatabaseSettings(): DatabaseSettings {
    return DatabaseSettings(
        id = id,
        isFingerprintUnlockEnabled = isFingerprintUnlockEnabled,
        isNotificationEnabled = isNotificationEnabled,
        isForceAuthenticationOnAppStartupEnabled = isForceAuthenticationOnAppStartupEnabled,
        isOrderbookRealTimeUpdatesHighlightingEnabled = isOrderbookRealTimeUpdatesHighlightingEnabled,
        isNewTradesRealTimeAdditionHighlightingEnabled = isNewTradesRealTimeAdditionHighlightingEnabled,
        isPriceChartZoomInEnabled = isPriceChartZoomInEnabled,
        areMarketPreviewChartsVisible = areMarketPreviewChartsVisible,
        areAnalyticsToastsEnabled = areAnalyticsToastsEnabled,
        areEmptyWalletsHidden = areEmptyWalletsHidden,
        shouldAnimateCharts = shouldAnimateCharts,
        shouldKeepScreenOn = shouldKeepScreenOn,
        pinCode = pinCode,
        bullishCandleStickStyle = bullishCandleStickStyle,
        bearishCandleStickStyle = bearishCandleStickStyle,
        depthChartLineStyle = depthChartLineStyle,
        authenticationSessionDuration = authenticationSessionDuration,
        language = language,
        fiatCurrency = fiatCurrency,
        theme = theme
    )
}


fun DatabaseSettings.mapToSettings(): Settings {
    return Settings(
        id = id,
        isFingerprintUnlockEnabled = isFingerprintUnlockEnabled,
        isNotificationEnabled = isNotificationEnabled,
        isForceAuthenticationOnAppStartupEnabled = isForceAuthenticationOnAppStartupEnabled,
        isOrderbookRealTimeUpdatesHighlightingEnabled = isOrderbookRealTimeUpdatesHighlightingEnabled,
        isNewTradesRealTimeAdditionHighlightingEnabled = isNewTradesRealTimeAdditionHighlightingEnabled,
        isPriceChartZoomInEnabled = isPriceChartZoomInEnabled,
        areMarketPreviewChartsVisible = areMarketPreviewChartsVisible,
        areAnalyticsToastsEnabled = areAnalyticsToastsEnabled,
        areEmptyWalletsHidden = areEmptyWalletsHidden,
        shouldAnimateCharts = shouldAnimateCharts,
        shouldKeepScreenOn = shouldKeepScreenOn,
        pinCode = pinCode,
        bullishCandleStickStyle = bullishCandleStickStyle,
        bearishCandleStickStyle = bearishCandleStickStyle,
        depthChartLineStyle = depthChartLineStyle,
        authenticationSessionDuration = authenticationSessionDuration,
        language = language,
        fiatCurrency = fiatCurrency,
        theme = theme
    )
}