package com.stocksexchange.android.ui.settings

import com.stocksexchange.android.Constants
import com.stocksexchange.android.events.InboxCountItemChangeEvent
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingAccount
import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.ui.settings.handlers.SettingHandler
import com.stocksexchange.android.ui.settings.handlers.model.SettingAction
import com.stocksexchange.android.ui.settings.handlers.model.SettingEvent
import com.stocksexchange.android.utils.InAppUpdateHelper
import com.stocksexchange.android.utils.managers.SessionManager
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class SettingsPresenter(
    view: SettingsContract.View,
    model: SettingsModel,
    private val sessionManager: SessionManager
) : BasePresenter<SettingsContract.View, SettingsModel>(view, model),
    SettingsContract.ActionListener, SettingsModel.ActionListener {


    private val mSettingHandlers: List<SettingHandler> by inject { parametersOf(view, model) }




    init {
        model.setActionListener(this)
    }


    override fun start() {
        super.start()

        if(mView.isDataSetEmpty()) {
            setSettingItems()
        }

        dispatchNotificationsCountChangeEvent()
    }


    private fun setSettingItems() {
        val items = mModel.getSettingItems(
            sessionManager.getSettings(),
            sessionManager.getProfileInfo()
        )

        mView.setItems(
            items = items,
            notifyAboutChange = true
        )
    }


    override fun onInAppUpdateHelperStateChanged(state: InAppUpdateHelper.State) {
        dispatchSettingEvent(
            id = SettingId.APP_VERSION,
            action = SettingAction.IN_APP_UPDATE_HELPER_STATE_CHANGED,
            payload = state
        )
    }


    override fun onSignOutClicked(setting: SettingAccount) {
        dispatchSettingEvent(
            id = setting.settingId,
            action = SettingAction.CLICKED,
            payload = setting
        )
    }


    override fun onSettingSwitchClicked(setting: Setting, isChecked: Boolean) {
        setting.isChecked = isChecked
        onSettingItemClicked(setting)
    }


    override fun onSettingItemClicked(setting: Setting) {
        dispatchSettingEvent(
            id = setting.id,
            action = SettingAction.CLICKED,
            payload = setting
        )
    }


    private fun dispatchNotificationsCountChangeEvent() {
        dispatchSettingEvent(
            id = SettingId.NOTIFICATION_REPORT,
            action = SettingAction.NOTIFICATIONS_COUNT_CHANGED
        )
    }


    private fun dispatchSettingEvent(id: SettingId, action: SettingAction, payload: Any? = null) {
        dispatchSettingEvent(SettingEvent(
            id = id,
            action = action,
            payload = payload
        ))
    }


    private fun dispatchSettingEvent(event: SettingEvent) {
        mSettingHandlers.firstOrNull { it.candleHandleEvent(event) }?.onHandleEvent(event)
    }


    override fun onPinCodeChanged() {
        dispatchSettingEvent(
            id = SettingId.CHANGE_PIN,
            action = SettingAction.PIN_CODE_CHANGED
        )
    }


    override fun onFingerprintDialogButtonClicked() {
        dispatchSettingEvent(
            id = SettingId.FINGERPRINT_UNLOCK,
            action = SettingAction.FINGERPRINT_DIALOG_BUTTON_CLICKED
        )
    }


    override fun onFingerprintUnlockConfirmed() {
        dispatchSettingEvent(
            id = SettingId.FINGERPRINT_UNLOCK,
            action = SettingAction.FINGERPRINT_UNLOCK_CONFIRMED
        )
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: InboxCountItemChangeEvent) {
        if(event.isOriginatedFrom(this) ||
            event.isConsumed ||
            !Constants.IMPLEMENTATION_NOTIFICATION_TURN_ON) {
            return
        }

        dispatchNotificationsCountChangeEvent()

        event.consume()
    }


    override fun onBackPressed(): Boolean = onNavigateUpPressed()


    override fun canReceiveEvents(): Boolean = true


}