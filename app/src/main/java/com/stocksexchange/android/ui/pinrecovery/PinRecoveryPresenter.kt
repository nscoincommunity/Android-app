package com.stocksexchange.android.ui.pinrecovery

import com.stocksexchange.android.factories.SettingsFactory
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.managers.SessionManager

class PinRecoveryPresenter(
    view: PinRecoveryContract.View,
    model: PinRecoveryModel,
    private val settingsFactory: SettingsFactory,
    private val sessionManager: SessionManager
) : BasePresenter<PinRecoveryContract.View, PinRecoveryModel>(view, model),
    PinRecoveryContract.ActionListener, PinRecoveryModel.ActionListener {


    init {
        model.setActionListener(this)
    }


    override fun onCancellationButtonClicked() {
        mView.finishActivity()
    }


    override fun onConfirmationButtonClicked() {
        mModel.clearUserData {
            val updatedSettings = settingsFactory.getSettingsAfterUserLogout(
                sessionManager.getSettings()
            )

            mModel.updateSettings(updatedSettings) {
                sessionManager.setSettings(updatedSettings)

                mView.launchDashboardActivity()
                mView.finishActivity()
            }
        }
    }


}