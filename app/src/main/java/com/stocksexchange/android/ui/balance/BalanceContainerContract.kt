package com.stocksexchange.android.ui.balance

import com.stocksexchange.android.ui.base.viewpager.ViewPagerView
import com.stocksexchange.android.ui.views.popupmenu.PopupMenuItemData
import com.stocksexchange.api.model.rest.TransactionType
import com.stocksexchange.api.model.rest.WalletBalanceType

interface BalanceContainerContract {


    interface View : ViewPagerView {

        fun showToolbarPreRightButton(animate: Boolean)

        fun hideToolbarPreRightButton(animate: Boolean)

        fun showPopupMenu(items: List<PopupMenuItemData>)

        fun hidePopupMenu()

        fun showAppBar(animate: Boolean)

        fun navigateToWalletsSearchScreen()

        fun navigateToTransactionsSearchScreen(transactionType: TransactionType)

        fun setShowEmptyWallets(showEmptyWallets: Boolean)

        fun setWalletsSortBalanceType(walletBalanceType: WalletBalanceType)

        fun getSelectedTabPosition(): Int

    }


    interface ActionListener {

        fun onToolbarPreRightButtonClicked()

        fun onToolbarRightButtonClicked()

        fun onPopupMenuItemClicked(item: PopupMenuItemData)

    }


}