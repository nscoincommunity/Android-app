package com.stocksexchange.android.ui.settings.handlers.concrete.general

import com.stocksexchange.android.R
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.ui.settings.handlers.model.SettingAction
import com.stocksexchange.android.ui.settings.handlers.model.SettingEvent
import com.stocksexchange.android.utils.InAppUpdateHelper
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.StringProvider

class InAppUpdateSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager,
    private val stringProvider: StringProvider
) : BaseSettingHandler<Setting>(view, model, sessionManager) {


    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun onHandleEvent(event: SettingEvent) {
        super.onHandleEvent(event)

        when(event.action) {
            SettingAction.IN_APP_UPDATE_HELPER_STATE_CHANGED -> {
                onInAppUpdateHelperStateChanged((event.payload as? InAppUpdateHelper.State) ?: return)
            }
        }
    }


    override fun onSettingClicked(setting: Setting) {
        view.proceedWithInAppUpdate()
    }


    private fun onInAppUpdateHelperStateChanged(state: InAppUpdateHelper.State) {
        if(view.isDataSetEmpty()) {
            return
        }

        val settingItem = model.getItemForId(SettingId.APP_VERSION, sessionManager.getSettings()).copy(
            description = stringProvider.getString(when(state) {
                InAppUpdateHelper.State.NO_UPDATES,
                InAppUpdateHelper.State.UPDATE_FAILED,
                InAppUpdateHelper.State.UPDATE_CANCELLED -> R.string.app_update_state_up_to_date_version_text
                InAppUpdateHelper.State.PENDING_UPDATE -> R.string.app_update_state_pending_update_text
                InAppUpdateHelper.State.DOWNLOADING_UPDATE -> R.string.app_update_state_downloading_update_text
                InAppUpdateHelper.State.UPDATE_DOWNLOADED -> R.string.app_update_state_update_downloaded_text
            })
        )

        view.updateSettingWith(settingItem)
    }


    override fun getAllowedActions(): List<SettingAction> {
        return (super.getAllowedActions() + listOf(SettingAction.IN_APP_UPDATE_HELPER_STATE_CHANGED))
    }


    override fun getSettingId(): SettingId = SettingId.APP_VERSION


}