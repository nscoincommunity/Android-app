package com.stocksexchange.android.ui.balance

import com.stocksexchange.android.R
import com.stocksexchange.android.events.PerformedWalletActionsEvent
import com.stocksexchange.android.model.BalanceTab
import com.stocksexchange.android.model.MaterialDialogBuilder
import com.stocksexchange.android.ui.base.viewpager.BaseViewPagerPresenter
import com.stocksexchange.android.ui.views.popupmenu.PopupMenuItemData
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.helpers.handlePerformedWalletActionsEvent
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.api.model.rest.TransactionType
import com.stocksexchange.api.model.rest.WalletBalanceType
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class BalanceContainerPresenter(
    view: BalanceContainerContract.View,
    model: BalanceContainerModel,
    private val sessionManager: SessionManager
) : BaseViewPagerPresenter<BalanceContainerContract.View, BalanceContainerModel>(view, model),
    BalanceContainerContract.ActionListener, BalanceContainerModel.ActionListener {


    var wasWithdrawalJustConfirmed: Boolean = false
    var wasWithdrawalJustCancelled: Boolean = false

    var selectedTab: BalanceTab = BalanceTab.WALLETS

    private var mWalletsSortBalanceType: WalletBalanceType = WalletBalanceType.CURRENT




    init {
        model.setActionListener(this)
    }


    override fun stop() {
        super.stop()

        mView.hidePopupMenu()
    }


    override fun onToolbarPreRightButtonClicked() {
        mView.showPopupMenu(mModel.getPopupMenuItems())
    }


    override fun onToolbarRightButtonClicked() {
        when(mView.getSelectedTabPosition()) {
            BalanceTab.WALLETS.ordinal -> mView.navigateToWalletsSearchScreen()
            BalanceTab.DEPOSITS.ordinal -> navigateToTransactionsSearchScreen(TransactionType.DEPOSITS)
            BalanceTab.WITHDRAWALS.ordinal -> navigateToTransactionsSearchScreen(TransactionType.WITHDRAWALS)
        }
    }


    override fun onPopupMenuItemClicked(item: PopupMenuItemData) {
        when(item.id) {
            BalanceContainerModel.POPUP_MENU_ITEM_EMPTY_WALLETS -> onPopupMenuWalletsFlagItemClicked()
            BalanceContainerModel.POPUP_MENU_ITEM_SORT -> onPopupMenuSortItemClicked()
        }
    }


    private fun onPopupMenuWalletsFlagItemClicked() {
        val oldSettings = sessionManager.getSettings()
        val newSettings = oldSettings.copy(
            areEmptyWalletsHidden = !oldSettings.areEmptyWalletsHidden
        )

        mModel.updateSettings(newSettings) {
            sessionManager.setSettings(newSettings)

            mView.setShowEmptyWallets(!newSettings.areEmptyWalletsHidden)
        }
    }


    private fun onPopupMenuSortItemClicked() {
        val items = mStringProvider.getStringList(
            R.string.wallets_sort_column_picker_dialog_item_current_balance,
            R.string.wallets_sort_column_picker_dialog_item_frozen_balance,
            R.string.wallets_sort_column_picker_dialog_item_bonus_balance,
            R.string.wallets_sort_column_picker_dialog_item_total_balance
        )
        val title = mStringProvider.getString(R.string.action_sort_by)
        val positiveBtnText = mStringProvider.getString(R.string.action_select)

        mView.showMaterialDialog(MaterialDialogBuilder.listDialog(
            title = title,
            items = items.toTypedArray(),
            selectedItemIndex = mWalletsSortBalanceType.ordinal,
            positiveBtnText = positiveBtnText,
            itemsCallbackSingleChoice = {
                onWalletsSortBalanceTypePicked(it)
            }
        ))
    }


    private fun onWalletsSortBalanceTypePicked(selectedIndex: Int) {
        val sortColumn = WalletBalanceType.values().first { it.ordinal == selectedIndex }

        if(mWalletsSortBalanceType != sortColumn) {
            mWalletsSortBalanceType = sortColumn

            mView.setWalletsSortBalanceType(sortColumn)
        }
    }


    private fun navigateToTransactionsSearchScreen(transactionType: TransactionType) {
        mView.navigateToTransactionsSearchScreen(transactionType)
    }


    override fun onScrollToTopRequested(position: Int) {
        mView.showAppBar(true)

        super.onScrollToTopRequested(position)
    }


    override fun onTabSelected(position: Int) {
        super.onTabSelected(position)

        if(position == BalanceTab.WALLETS.ordinal) {
            mView.showToolbarPreRightButton(true)
        } else {
            mView.hideToolbarPreRightButton(true)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: PerformedWalletActionsEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        handlePerformedWalletActionsEvent(event)

        event.consume()
    }


    override fun canReceiveEvents(): Boolean = true


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            wasWithdrawalJustConfirmed = it.wasWithdrawalJustConfirmed
            wasWithdrawalJustCancelled = it.wasWithdrawalJustCancelled
            selectedTab = it.selectedTab
            mWalletsSortBalanceType = it.walletsSortBalanceType
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            wasWithdrawalJustConfirmed = wasWithdrawalJustConfirmed,
            wasWithdrawalJustCancelled = wasWithdrawalJustCancelled,
            selectedTab = selectedTab,
            walletsSortBalanceType = mWalletsSortBalanceType
        ))
    }


}