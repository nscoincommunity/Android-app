package com.stocksexchange.android.ui.settings.handlers.concrete.general

import com.stocksexchange.android.events.SettingsEvent
import com.stocksexchange.android.model.MaterialDialogBuilder
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.api.model.rest.FiatCurrency
import org.greenrobot.eventbus.EventBus

class FiatCurrencySettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager,
    private val stringProvider: StringProvider
) : BaseSettingHandler<Setting>(view, model, sessionManager) {


    override fun onSettingClicked(setting: Setting) {
        val items = FiatCurrency.values()
            .map { stringProvider.getString(it.stringId) }
            .toTypedArray()

        view.showMaterialDialog(MaterialDialogBuilder.listDialog(
            items = items,
            itemsCallback = {
                onFiatCurrencyPicked(it)
            }
        ))
    }


    private fun onFiatCurrencyPicked(fiatCurrency: String) {
        val newFiatCurrency = FiatCurrency.values().first {
            fiatCurrency == stringProvider.getString(it.stringId)
        }
        val oldSettings = sessionManager.getSettings()

        if(newFiatCurrency == oldSettings.fiatCurrency) {
            return
        }

        val onFinish: ((Settings, Settings) -> Unit) = { newSettings, _ ->
            view.updateSettingWith(model.getItemForId(
                SettingId.FIAT_CURRENCY,
                newSettings
            ))

            EventBus.getDefault().post(SettingsEvent.changeFiatCurrency(
                attachment = newSettings,
                source = this
            ))
        }

        updateSettings(onFinish) {
            it.copy(fiatCurrency = newFiatCurrency)
        }
    }


    override fun getSettingId(): SettingId = SettingId.FIAT_CURRENCY


}