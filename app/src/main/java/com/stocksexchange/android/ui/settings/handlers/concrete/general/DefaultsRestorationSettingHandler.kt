package com.stocksexchange.android.ui.settings.handlers.concrete.general

import com.stocksexchange.android.R
import com.stocksexchange.android.events.SettingsEvent
import com.stocksexchange.android.factories.SettingsFactory
import com.stocksexchange.android.model.MaterialDialogBuilder
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.formatters.NumberFormatter
import org.greenrobot.eventbus.EventBus

class DefaultsRestorationSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager,
    private val stringProvider: StringProvider,
    private val numberFormatter: NumberFormatter,
    private val settingsFactory: SettingsFactory
) : BaseSettingHandler<Setting>(view, model, sessionManager) {


    override fun onSettingClicked(setting: Setting) {
        showRestoreDefaultsConfirmationDialog()
    }


    private fun showRestoreDefaultsConfirmationDialog() {
        val title = stringProvider.getString(
            R.string.settings_fragment_restore_defaults_dialog_title_text
        )
        val content = stringProvider.getString(
            R.string.settings_fragment_restore_defaults_dialog_text
        )

        view.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            title = title,
            content = content,
            negativeBtnText = stringProvider.getString(R.string.no),
            positiveBtnText = stringProvider.getString(R.string.yes),
            positiveBtnClick = {
                onRestoreDefaultsConfirmed()
            }
        ))
    }


    private fun onRestoreDefaultsConfirmed() {
        val onFinish: ((Settings, Settings) -> Unit) = { newSettings, _ ->
            EventBus.getDefault().post(SettingsEvent.restoreDefaults(
                attachment = newSettings,
                source = this
            ))
        }

        updateSettings(onFinish) {
            settingsFactory.getDefaultSettings().copy(pinCode = it.pinCode)
        }
    }


    override fun getSettingId(): SettingId = SettingId.RESTORE_DEFAULTS


}