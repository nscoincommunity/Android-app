package com.stocksexchange.android.ui.settings.handlers.concrete.appearance.candlestickstyle

import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.concrete.appearance.candlestickstyle.BaseCandleStickStyleSettingHandler
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.StringProvider

class BearishCandleStickStyleSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager,
    stringProvider: StringProvider
) : BaseCandleStickStyleSettingHandler(view, model, sessionManager, stringProvider) {


    override fun getSettingId(): SettingId = SettingId.BEARISH_CANDLE_STICK_STYLE


}