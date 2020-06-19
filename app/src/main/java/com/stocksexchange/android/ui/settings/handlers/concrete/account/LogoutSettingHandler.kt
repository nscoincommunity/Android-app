package com.stocksexchange.android.ui.settings.handlers.concrete.account

import com.stocksexchange.android.R
import com.stocksexchange.android.events.UserEvent
import com.stocksexchange.android.factories.SettingsFactory
import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.StringProvider
import org.greenrobot.eventbus.EventBus

class LogoutSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager,
    private val stringProvider: StringProvider,
    private val settingsFactory: SettingsFactory
) : BaseSettingHandler<SettingAccount>(view, model, sessionManager) {


    override fun onSettingClicked(setting: SettingAccount) {
        showSignOutConfirmationDialog()
    }


    private fun showSignOutConfirmationDialog() {
        val title = stringProvider.getString(
            R.string.settings_fragment_sign_out_dialog_title_text
        )
        val content = stringProvider.getString(
            R.string.settings_fragment_sign_out_dialog_text
        )

        view.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            title = title,
            content = content,
            negativeBtnText = stringProvider.getString(R.string.no),
            positiveBtnText = stringProvider.getString(R.string.yes),
            positiveBtnClick = {
                onSignOutConfirmed()
            }
        ))
    }


    private fun onSignOutConfirmed() {
        view.showProgressBar()

        model.clearAllUserData {
            val onFinish: ((Settings, Settings) -> Unit) = { _, _ ->
                view.hideProgressBar()

                EventBus.getDefault().post(UserEvent.signOut(this))
            }

            updateSettings(onFinish) {
                settingsFactory.getSettingsAfterUserLogout(it)
            }
        }
    }


    override fun getSettingId(): SettingId = SettingId.SIGN_OUT


}