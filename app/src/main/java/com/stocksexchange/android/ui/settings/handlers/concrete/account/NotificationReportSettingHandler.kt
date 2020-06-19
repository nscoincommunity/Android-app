package com.stocksexchange.android.ui.settings.handlers.concrete.account

import com.stocksexchange.android.Constants
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.ui.settings.handlers.model.SettingAction
import com.stocksexchange.android.ui.settings.handlers.model.SettingEvent
import com.stocksexchange.android.utils.managers.SessionManager

class NotificationReportSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager
) : BaseSettingHandler<Setting>(view, model, sessionManager) {


    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun onHandleEvent(event: SettingEvent) {
        super.onHandleEvent(event)

        when(event.action) {
            SettingAction.NOTIFICATIONS_COUNT_CHANGED -> onNotificationReportCountChange()
        }
    }


    override fun onSettingClicked(setting: Setting) {
        if(!sessionManager.isUserSignedIn()) {
            return
        }

        view.navigateToInboxScreen()
    }


    private fun onNotificationReportCountChange() {
        if(view.isDataSetEmpty() || !Constants.IMPLEMENTATION_NOTIFICATION_TURN_ON) {
            return
        }

        val settingItem = model.getItemForId(SettingId.NOTIFICATION_REPORT, sessionManager.getSettings()).copy(
            description = model.getNotificationReportText(),
            customDescriptionColor = model.isNotificationReportCustomDescriptionColor()
        )

        view.updateSettingWith(settingItem)
    }


    override fun getAllowedActions(): List<SettingAction> {
        return (super.getAllowedActions() + listOf(SettingAction.NOTIFICATIONS_COUNT_CHANGED))
    }


    override fun getSettingId(): SettingId = SettingId.NOTIFICATION_REPORT


}