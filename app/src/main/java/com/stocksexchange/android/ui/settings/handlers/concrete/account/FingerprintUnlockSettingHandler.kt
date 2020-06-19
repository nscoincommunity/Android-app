package com.stocksexchange.android.ui.settings.handlers.concrete.account

import com.stocksexchange.android.R
import com.stocksexchange.android.model.MaterialDialogBuilder
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.ui.settings.handlers.model.SettingAction
import com.stocksexchange.android.ui.settings.handlers.model.SettingEvent
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.providers.FingerprintProvider

class FingerprintUnlockSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager,
    private val fingerprintProvider: FingerprintProvider,
    private val stringProvider: StringProvider
) : BaseSettingHandler<Setting>(view, model, sessionManager) {


    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun onHandleEvent(event: SettingEvent) {
        super.onHandleEvent(event)

        when(event.action) {
            SettingAction.FINGERPRINT_DIALOG_BUTTON_CLICKED -> onFingerprintDialogButtonClicked()
            SettingAction.FINGERPRINT_UNLOCK_CONFIRMED -> onFingerprintUnlockConfirmed()
        }
    }


    override fun onSettingClicked(setting: Setting) {
        if(setting.isChecked) {
            showDisableFingerprintUnlockDialog()
        } else {
            if(!fingerprintProvider.hasEnrolledFingerprints()) {
                showNoFingerprintsAvailableDialog()
                return
            }

            view.showFingerprintDialog()
        }
    }


    private fun showDisableFingerprintUnlockDialog() {
        val title = stringProvider.getString(
            R.string.settings_fragment_disable_fingerprint_unlock_dialog_title_text
        )
        val content = stringProvider.getString(
            R.string.settings_fragment_disable_fingerprint_unlock_dialog_text
        )

        view.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            title = title,
            content = content,
            negativeBtnText = stringProvider.getString(R.string.no),
            positiveBtnText = stringProvider.getString(R.string.yes),
            positiveBtnClick = {
                updateFingerprintUnlockSetting(false)
            }
        ))
    }


    private fun updateFingerprintUnlockSetting(isFingerprintUnlockEnabled: Boolean) {
        val onFinish: ((Settings, Settings) -> Unit) = { newSettings, _ ->
            view.updateSettingWith(model.getItemForId(
                SettingId.FINGERPRINT_UNLOCK,
                newSettings
            ))
        }

        updateSettings(onFinish) {
            it.copy(isFingerprintUnlockEnabled = isFingerprintUnlockEnabled)
        }
    }


    private fun showNoFingerprintsAvailableDialog() {
        val title = stringProvider.getString(
            R.string.settings_fragment_no_fingerprints_available_dialog_title_text
        )
        val content = stringProvider.getString(
            R.string.settings_fragment_no_fingerprints_available_dialog_text
        )

        view.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            title = title,
            content = content,
            negativeBtnText = stringProvider.getString(R.string.no),
            positiveBtnText = stringProvider.getString(R.string.yes),
            positiveBtnClick = {
                view.launchSecuritySettings()
            }
        ))
    }


    private fun onFingerprintDialogButtonClicked() {
        view.hideFingerprintDialog()
    }


    private fun onFingerprintUnlockConfirmed() {
        updateFingerprintUnlockSetting(true)
    }


    override fun getAllowedActions(): List<SettingAction> {
        val extraActions = listOf(
            SettingAction.FINGERPRINT_DIALOG_BUTTON_CLICKED,
            SettingAction.FINGERPRINT_UNLOCK_CONFIRMED
        )

        return (super.getAllowedActions() + extraActions)
    }


    override fun getSettingId(): SettingId = SettingId.FINGERPRINT_UNLOCK


}