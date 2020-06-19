package com.stocksexchange.android.ui.settings.handlers.concrete.account

import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.StringProvider

class AuthSessionDurationSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager,
    private val stringProvider: StringProvider
) : BaseSettingHandler<Setting>(view, model, sessionManager) {


    override fun onSettingClicked(setting: Setting) {
        val itemList = mutableListOf<String>().apply {
            for(duration in AuthenticationSessionDuration.values()) {
                if((duration == AuthenticationSessionDuration.FIVE_SECONDS) && !BuildConfig.DEBUG) {
                    continue
                }

                add(stringProvider.getString(duration.titleId))
            }
        }

        showAuthenticationSessionDurationsDialog(itemList)
    }


    private fun showAuthenticationSessionDurationsDialog(items: List<String>) {
        view.showMaterialDialog(MaterialDialogBuilder.listDialog(
            items = items.toTypedArray(),
            itemsCallback = {
                onAuthenticationSessionDurationItemPicked(it)
            }
        ))
    }


    private fun onAuthenticationSessionDurationItemPicked(authenticationSessionDuration: String) {
        val newAuthenticationSessionDuration = AuthenticationSessionDuration.values().first {
            authenticationSessionDuration == stringProvider.getString(it.titleId)
        }
        val oldSettings = sessionManager.getSettings()

        if(newAuthenticationSessionDuration == oldSettings.authenticationSessionDuration) {
            return
        }

        val onFinish: ((Settings, Settings) -> Unit) = { newSettings, _ ->
            view.updateSettingWith(model.getItemForId(
                SettingId.AUTHENTICATION_SESSION_DURATION,
                newSettings
            ))

            // Updating the last interaction time to put into action the
            // selected new session duration
            view.updateLastInteractionTime()
        }

        updateSettings(onFinish) {
            it.copy(authenticationSessionDuration = newAuthenticationSessionDuration)
        }
    }


    override fun getSettingId(): SettingId = SettingId.AUTHENTICATION_SESSION_DURATION


}