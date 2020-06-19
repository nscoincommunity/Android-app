package com.stocksexchange.android.ui.pinrecovery

import com.stocksexchange.android.data.repositories.settings.SettingsRepository
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.ui.pinrecovery.PinRecoveryModel.ActionListener
import com.stocksexchange.android.utils.handlers.UserDataClearingHandler

class PinRecoveryModel(
    private val userDataClearingHandler: UserDataClearingHandler,
    private val settingsRepository: SettingsRepository
) : BaseModel<ActionListener>() {


    fun clearUserData(onFinish: suspend (() -> Unit)) {
        createUiLaunchCoroutine {
            userDataClearingHandler.clearAllUserData(onFinish)
        }
    }


    fun updateSettings(settings: Settings, onFinish: () -> Unit) {
        createUiLaunchCoroutine {
            settingsRepository.save(settings)

            onFinish()
        }
    }


    interface ActionListener : BaseActionListener


}