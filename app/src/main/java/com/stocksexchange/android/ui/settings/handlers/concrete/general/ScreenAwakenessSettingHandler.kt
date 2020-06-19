package com.stocksexchange.android.ui.settings.handlers.concrete.general

import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.utils.managers.SessionManager

class ScreenAwakenessSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager
) : BaseSettingHandler<Setting>(view, model, sessionManager) {


    override fun onSettingClicked(setting: Setting) {
        view.setScreenNotAsleep(setting.isChecked)

        updateSettings {
            it.copy(shouldKeepScreenOn = setting.isChecked)
        }
    }


    override fun getSettingId(): SettingId = SettingId.KEEP_SCREEN_ON


}