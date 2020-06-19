package com.stocksexchange.android.ui.settings.handlers.concrete.account

import com.stocksexchange.android.R
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.ui.settings.handlers.model.SettingAction
import com.stocksexchange.android.ui.settings.handlers.model.SettingEvent
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.StringProvider

class PinUpdateSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager,
    private val stringProvider: StringProvider
) : BaseSettingHandler<Setting>(view, model, sessionManager) {


    override fun onHandleEvent(event: SettingEvent) {
        super.onHandleEvent(event)

        if(event.action == SettingAction.PIN_CODE_CHANGED) {
            onPinCodeChanged()
        }
    }


    override fun onSettingClicked(setting: Setting) {
        view.launchPinCodeChangeActivity()
    }


    private fun onPinCodeChanged() {
        view.showToast(stringProvider.getString(R.string.pin_code_changed))
    }


    override fun getAllowedActions(): List<SettingAction> {
        return (super.getAllowedActions() + listOf(SettingAction.PIN_CODE_CHANGED))
    }


    override fun getSettingId(): SettingId = SettingId.CHANGE_PIN


}