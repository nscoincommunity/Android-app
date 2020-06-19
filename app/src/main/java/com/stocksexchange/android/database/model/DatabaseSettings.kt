package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stocksexchange.api.model.rest.FiatCurrency
import com.stocksexchange.android.database.model.DatabaseSettings.Companion.TABLE_NAME
import com.stocksexchange.android.model.*
import com.stocksexchange.android.theming.model.Theme

/**
 * A Room database model for the [Settings] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseSettings(
    @PrimaryKey @ColumnInfo(name = ID) var id: Int,
    @ColumnInfo(name = IS_FINGERPRINT_UNLOCK_ENABLED) var isFingerprintUnlockEnabled: Boolean,
    @ColumnInfo(name = IS_NOTIFICATION_ENABLED) var isNotificationEnabled: Boolean,
    @ColumnInfo(name = IS_FORCE_AUTHENTICATION_ON_APP_STARTUP_IS_ENABLED) var isForceAuthenticationOnAppStartupEnabled: Boolean,
    @ColumnInfo(name = IS_ORDERBOOK_REAL_TIME_UPDATES_HIGHLIGHTING_ENABLED) var isOrderbookRealTimeUpdatesHighlightingEnabled: Boolean,
    @ColumnInfo(name = IS_NEW_TRADES_REAL_TIME_ADDITION_HIGHLIGHTING_ENABLED) var isNewTradesRealTimeAdditionHighlightingEnabled: Boolean,
    @ColumnInfo(name = IS_PRICE_CHART_ZOOM_IN_ENABLED) var isPriceChartZoomInEnabled: Boolean,
    @ColumnInfo(name = ARE_MARKET_PREVIEW_CHARTS_VISIBLE) var areMarketPreviewChartsVisible: Boolean,
    @ColumnInfo(name = ARE_ANALYTICS_TOASTS_ENABLED) var areAnalyticsToastsEnabled: Boolean,
    @ColumnInfo(name = ARE_EMPTY_WALLETS_HIDDEN) var areEmptyWalletsHidden: Boolean,
    @ColumnInfo(name = SHOULD_ANIMATE_CHARTS) var shouldAnimateCharts: Boolean,
    @ColumnInfo(name = SHOULD_KEEP_SCREEN_ON) var shouldKeepScreenOn: Boolean,
    @ColumnInfo(name = PIN_CODE) var pinCode: PinCode,
    @ColumnInfo(name = BULLISH_CANDLE_STICK_STYLE) var bullishCandleStickStyle: CandleStickStyle,
    @ColumnInfo(name = BEARISH_CANDLE_STICK_STYLE) var bearishCandleStickStyle: CandleStickStyle,
    @ColumnInfo(name = DEPTH_CHART_LINE_STYLE) var depthChartLineStyle: DepthChartLineStyle,
    @ColumnInfo(name = AUTHENTICATION_SESSION_DURATION) var authenticationSessionDuration: AuthenticationSessionDuration,
    @ColumnInfo(name = LANGUAGE) var language: Language,
    @ColumnInfo(name = FIAT_CURRENCY) var fiatCurrency: FiatCurrency,
    @ColumnInfo(name = THEME_ID) var theme: Theme
) {

    companion object {

        const val TABLE_NAME = "settings"

        const val ID = "id"
        const val IS_FINGERPRINT_UNLOCK_ENABLED = "is_fingerprint_unlock_enabled"
        const val IS_NOTIFICATION_ENABLED = "is_notification_enabled"
        const val IS_FORCE_AUTHENTICATION_ON_APP_STARTUP_IS_ENABLED = "is_force_authentication_on_app_startup_is_enabled"
        const val IS_ORDERBOOK_REAL_TIME_UPDATES_HIGHLIGHTING_ENABLED = "is_orderbook_real_time_updates_highlighting_enabled"
        const val IS_NEW_TRADES_REAL_TIME_ADDITION_HIGHLIGHTING_ENABLED = "is_new_trades_real_time_addition_highlighting_enabled"
        const val IS_PRICE_CHART_ZOOM_IN_ENABLED = "is_price_chart_zoom_in_enabled"
        const val ARE_MARKET_PREVIEW_CHARTS_VISIBLE = "are_market_preview_charts_visible"
        const val ARE_ANALYTICS_TOASTS_ENABLED = "are_analytics_toasts_enabled"
        const val ARE_EMPTY_WALLETS_HIDDEN = "are_empty_wallets_hidden"
        const val SHOULD_ANIMATE_CHARTS = "should_animate_charts"
        const val SHOULD_KEEP_SCREEN_ON = "should_keep_screen_on"
        const val PIN_CODE = "pin_code"
        const val BULLISH_CANDLE_STICK_STYLE = "bullish_candle_stick_style"
        const val BEARISH_CANDLE_STICK_STYLE = "bearish_candle_stick_style"
        const val DEPTH_CHART_LINE_STYLE = "depth_chart_line_style"
        const val AUTHENTICATION_SESSION_DURATION = "authentication_session_duration"
        const val LANGUAGE = "language"
        const val FIAT_CURRENCY = "fiat_currency"
        const val THEME_ID = "theme_id"

    }


    constructor(): this(
        -1,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        PinCode.getEmptyPinCode(),
        CandleStickStyle.SOLID,
        CandleStickStyle.SOLID,
        DepthChartLineStyle.LINEAR,
        AuthenticationSessionDuration.FIVE_MINUTES,
        Language.ENGLISH,
        FiatCurrency.USD,
        Theme.STUB
    )

}