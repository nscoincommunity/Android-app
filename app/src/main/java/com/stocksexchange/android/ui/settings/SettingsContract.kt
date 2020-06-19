package com.stocksexchange.android.ui.settings

import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingAccount
import com.stocksexchange.android.ui.base.mvp.views.BaseView
import com.stocksexchange.android.utils.InAppUpdateHelper

interface SettingsContract {


    interface View : BaseView {

        fun showProgressBar()

        fun hideProgressBar()

        fun showFingerprintDialog()

        fun hideFingerprintDialog()

        fun showDeviceMetricsDialog()

        fun hideDeviceMetricsDialog()

        fun navigateToInboxScreen()

        fun navigateToLanguageScreen()

        fun launchPinCodeChangeActivity()

        fun launchSecuritySettings()

        fun updateSettingWith(setting: Setting, notifyAboutChange: Boolean = true)

        fun updateLastInteractionTime()

        fun notifyDataSetChanged()

        fun proceedWithInAppUpdate()

        fun postActionDelayed(delay: Long, action: () -> Unit)

        fun setScreenNotAsleep(isAwake: Boolean)

        fun setItems(items: MutableList<Any>, notifyAboutChange: Boolean = true)

        fun isDataSetEmpty(): Boolean

    }


    interface ActionListener {

        fun onInAppUpdateHelperStateChanged(state: InAppUpdateHelper.State)

        fun onSignOutClicked(setting: SettingAccount)

        fun onSettingSwitchClicked(setting: Setting, isChecked: Boolean)

        fun onSettingItemClicked(setting: Setting)

        fun onPinCodeChanged()

        fun onFingerprintDialogButtonClicked()

        fun onFingerprintUnlockConfirmed()

    }


}