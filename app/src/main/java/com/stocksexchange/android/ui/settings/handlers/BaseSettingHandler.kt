package com.stocksexchange.android.ui.settings.handlers

import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.model.SettingAction
import com.stocksexchange.android.ui.settings.handlers.model.SettingEvent
import com.stocksexchange.android.utils.managers.SessionManager

abstract class BaseSettingHandler<T>(
    protected val view: SettingsContract.View,
    protected val model: SettingsModel,
    protected val sessionManager: SessionManager
) : SettingHandler {


    protected fun updateSettings(onFinish: ((Settings, Settings) -> Unit)? = null,
                                 getNewSettings: (Settings) -> Settings) {
        val oldSettings = sessionManager.getSettings()
        val newSettings = getNewSettings(oldSettings)

        model.updateSettings(newSettings) {
            sessionManager.setSettings(newSettings)

            onFinish?.invoke(newSettings, oldSettings)
        }
    }


    @Suppress("UNCHECKED_CAST", "NON_EXHAUSTIVE_WHEN")
    override fun onHandleEvent(event: SettingEvent) {
        when(event.action) {
            SettingAction.CLICKED -> onSettingClicked(event.payload as? T ?: return)
        }
    }


    protected open fun onSettingClicked(setting: T) {
        // Stub
    }


    override fun candleHandleEvent(event: SettingEvent): Boolean {
        return ((event.id == getSettingId()) && (event.action in getAllowedActions()))
    }


    protected open fun getAllowedActions(): List<SettingAction> {
        return listOf(SettingAction.CLICKED)
    }


}