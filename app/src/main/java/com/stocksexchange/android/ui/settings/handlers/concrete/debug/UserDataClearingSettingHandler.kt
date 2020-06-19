package com.stocksexchange.android.ui.settings.handlers.concrete.debug

import com.stocksexchange.android.R
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.StringProvider

class UserDataClearingSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager,
    private val stringProvider: StringProvider
) : BaseSettingHandler<Setting>(view, model, sessionManager) {


    override fun onSettingClicked(setting: Setting) {
        model.clearPrivateUserData {
            view.showToast(stringProvider.getString(R.string.success))
        }
    }


    override fun getSettingId(): SettingId = SettingId.CLEAR_USER_DATA


}