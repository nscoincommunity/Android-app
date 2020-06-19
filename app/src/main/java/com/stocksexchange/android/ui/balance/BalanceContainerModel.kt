package com.stocksexchange.android.ui.balance

import com.stocksexchange.android.R
import com.stocksexchange.android.data.repositories.settings.SettingsRepository
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.ui.balance.BalanceContainerModel.ActionListener
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.ui.views.popupmenu.PopupMenuItemData
import com.stocksexchange.android.utils.managers.SessionManager

class BalanceContainerModel(
    private val sessionManager: SessionManager,
    private val settingsRepository: SettingsRepository
) : BaseModel<ActionListener>() {


    companion object {

        internal const val POPUP_MENU_ITEM_EMPTY_WALLETS = 0
        internal const val POPUP_MENU_ITEM_SORT = 1

    }




    fun getPopupMenuItems(): List<PopupMenuItemData> {
        return mutableListOf<PopupMenuItemData>().apply {
            add(PopupMenuItemData(
                id = POPUP_MENU_ITEM_EMPTY_WALLETS,
                title = mStringProvider.getString(if(sessionManager.getSettings().areEmptyWalletsHidden) {
                    R.string.action_show_empty_wallets
                } else {
                    R.string.action_hide_empty_wallets
                })
            ))
            add(PopupMenuItemData(
                id = POPUP_MENU_ITEM_SORT,
                title = mStringProvider.getString(R.string.action_sort)
            ))
        }
    }


    fun updateSettings(settings: Settings, onFinish: (() -> Unit)) {
        createUiLaunchCoroutine {
            settingsRepository.save(settings)

            onFinish()
        }
    }


    interface ActionListener : BaseActionListener


}