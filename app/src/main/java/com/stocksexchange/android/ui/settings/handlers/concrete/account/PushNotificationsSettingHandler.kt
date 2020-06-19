package com.stocksexchange.android.ui.settings.handlers.concrete.account

import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.api.model.rest.NotificationStatus

class PushNotificationsSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager
) : BaseSettingHandler<Setting>(view, model, sessionManager) {


    override fun onSettingClicked(setting: Setting) {
        updateSettings {
            it.copy(isNotificationEnabled = setting.isChecked)
        }

        if(setting.isChecked) {
            model.updateNotificationStatus(NotificationStatus.ENABLE)
        } else {
            model.updateNotificationStatus(NotificationStatus.DISABLE)
        }
    }


    override fun getSettingId(): SettingId = SettingId.PUSH_NOTIFICATION


}