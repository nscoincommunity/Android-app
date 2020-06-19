package com.stocksexchange.android.ui.settings.handlers.concrete.appearance

import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.utils.managers.SessionManager

class OrderbookUpdatesHighlightingSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager
) : BaseSettingHandler<Setting>(view, model, sessionManager) {


    override fun onSettingClicked(setting: Setting) {
        updateSettings {
            it.copy(isOrderbookRealTimeUpdatesHighlightingEnabled = setting.isChecked)
        }
    }


    override fun getSettingId(): SettingId = SettingId.HIGHLIGHT_ORDERBOOK_REAL_TIME_UPDATES


}