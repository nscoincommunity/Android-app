package com.stocksexchange.android.di.features

import com.stocksexchange.android.di.utils.factory
import com.stocksexchange.android.di.utils.get
import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.SettingHandler
import com.stocksexchange.android.ui.settings.handlers.concrete.account.*
import com.stocksexchange.android.ui.settings.handlers.concrete.appearance.*
import com.stocksexchange.android.ui.settings.handlers.concrete.appearance.candlestickstyle.BearishCandleStickStyleSettingHandler
import com.stocksexchange.android.ui.settings.handlers.concrete.appearance.candlestickstyle.BullishCandleStickStyleSettingHandler
import com.stocksexchange.android.ui.settings.handlers.concrete.debug.AnalyticsToastsSettingHandler
import com.stocksexchange.android.ui.settings.handlers.concrete.debug.DeviceMetricsSettingHandler
import com.stocksexchange.android.ui.settings.handlers.concrete.debug.UserDataClearingSettingHandler
import com.stocksexchange.android.ui.settings.handlers.concrete.general.*
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val settingsModule = module {

    factory<SettingHandler>(SettingId.SIGN_OUT.name) { (view: SettingsContract.View, model: SettingsModel) ->
        LogoutSettingHandler(view, model, get(), get(), get())
    }
    factory<SettingHandler>(SettingId.CHANGE_PIN.name) { (view: SettingsContract.View, model: SettingsModel) ->
        PinUpdateSettingHandler(view, model, get(), get())
    }
    factory<SettingHandler>(SettingId.AUTHENTICATION_SESSION_DURATION.name) { (view: SettingsContract.View, model: SettingsModel) ->
        AuthSessionDurationSettingHandler(view, model, get(), get())
    }
    factory<SettingHandler>(SettingId.FINGERPRINT_UNLOCK.name) { (view: SettingsContract.View, model: SettingsModel) ->
        FingerprintUnlockSettingHandler(view, model, get(), get(), get())
    }
    factory<SettingHandler>(SettingId.FORCE_AUTHENTICATION_ON_APP_STARTUP.name) { (view: SettingsContract.View, model: SettingsModel) ->
        AuthEnforcementSettingHandler(view, model, get())
    }
    factory<SettingHandler>(SettingId.APP_VERSION.name) { (view: SettingsContract.View, model: SettingsModel) ->
        InAppUpdateSettingHandler(view, model, get(), get())
    }
    factory<SettingHandler>(SettingId.LANGUAGE.name) { (view: SettingsContract.View, model: SettingsModel) ->
        LanguageSettingHandler(view, model, get())
    }
    factory<SettingHandler>(SettingId.FIAT_CURRENCY.name) { (view: SettingsContract.View, model: SettingsModel) ->
        FiatCurrencySettingHandler(view, model, get(), get())
    }
    factory<SettingHandler>(SettingId.KEEP_SCREEN_ON.name) { (view: SettingsContract.View, model: SettingsModel) ->
        ScreenAwakenessSettingHandler(view, model, get())
    }
    factory<SettingHandler>(SettingId.RESTORE_DEFAULTS.name) { (view: SettingsContract.View, model: SettingsModel) ->
        DefaultsRestorationSettingHandler(view, model, get(), get(), get(), get())
    }
    factory<SettingHandler>(SettingId.PUSH_NOTIFICATION.name) { (view: SettingsContract.View, model: SettingsModel) ->
        PushNotificationsSettingHandler(view, model, get())
    }
    factory<SettingHandler>(SettingId.NOTIFICATION_REPORT.name) { (view: SettingsContract.View, model: SettingsModel) ->
        NotificationReportSettingHandler(view, model, get())
    }
    factory<SettingHandler>(SettingId.ANIMATE_CHARTS.name) { (view: SettingsContract.View, model: SettingsModel) ->
        ChartsAnimationSettingHandler(view, model, get())
    }
    factory<SettingHandler>(SettingId.BULLISH_CANDLE_STICK_STYLE.name) { (view: SettingsContract.View, model: SettingsModel) ->
        BullishCandleStickStyleSettingHandler(view, model, get(), get())
    }
    factory<SettingHandler>(SettingId.BEARISH_CANDLE_STICK_STYLE.name) { (view: SettingsContract.View, model: SettingsModel) ->
        BearishCandleStickStyleSettingHandler(view, model, get(), get())
    }
    factory<SettingHandler>(SettingId.ZOOM_IN_ON_PRICE_CHART.name) { (view: SettingsContract.View, model: SettingsModel) ->
        PriceChartZoomSettingHandler(view, model, get())
    }
    factory<SettingHandler>(SettingId.DEPTH_CHART_LINE_STYLE.name) { (view: SettingsContract.View, model: SettingsModel) ->
        DepthChartLineStyleSettingHandler(view, model, get(), get())
    }
    factory<SettingHandler>(SettingId.HIGHLIGHT_ORDERBOOK_REAL_TIME_UPDATES.name) { (view: SettingsContract.View, model: SettingsModel) ->
        OrderbookUpdatesHighlightingSettingHandler(view, model, get())
    }
    factory<SettingHandler>(SettingId.HIGHLIGHT_NEW_TRADES_REAL_TIME_ADDITION.name) { (view: SettingsContract.View, model: SettingsModel) ->
        NewTradesAdditionsHighlightingSettingHandler(view, model, get())
    }
    factory<SettingHandler>(SettingId.DEVICE_METRICS.name) { (view: SettingsContract.View, model: SettingsModel) ->
        DeviceMetricsSettingHandler(view, model, get())
    }
    factory<SettingHandler>(SettingId.CLEAR_USER_DATA.name) { (view: SettingsContract.View, model: SettingsModel) ->
        UserDataClearingSettingHandler(view, model, get(), get())
    }
    factory<SettingHandler>(SettingId.ANALYTICS_TOASTS.name) { (view: SettingsContract.View, model: SettingsModel) ->
        AnalyticsToastsSettingHandler(view, model, get())
    }

    factory<List<SettingHandler>> { (view: SettingsContract.View, model: SettingsModel) ->
        mutableListOf<SettingHandler>().apply {
            for(id in SettingId.values()) {
                add(this@factory.get(id.name) { parametersOf(view, model) })
            }
        }
    }

}