package com.stocksexchange.android.ui.balance

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.stocksexchange.android.R
import com.stocksexchange.android.mappings.mapToPopupMenuItems
import com.stocksexchange.android.model.BalanceTab
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.viewpager.BaseViewPagerFragment
import com.stocksexchange.android.ui.transactions.fragment.TransactionsFragment
import com.stocksexchange.android.ui.transactions.fragment.newStandardInstance
import com.stocksexchange.android.ui.transactions.search.TransactionsSearchFragment
import com.stocksexchange.android.ui.transactions.search.newArgs
import com.stocksexchange.android.ui.views.popupmenu.PopupMenu
import com.stocksexchange.android.ui.views.popupmenu.PopupMenuItemData
import com.stocksexchange.android.ui.views.toolbars.Toolbar
import com.stocksexchange.android.ui.wallets.fragment.WalletsFragment
import com.stocksexchange.android.ui.wallets.fragment.newStandardInstance
import com.stocksexchange.api.model.rest.TransactionType
import com.stocksexchange.api.model.rest.WalletBalanceType
import com.stocksexchange.core.utils.extensions.ctx
import com.stocksexchange.core.utils.extensions.extract
import com.stocksexchange.core.utils.listeners.ProgressBarListener
import kotlinx.android.synthetic.main.balance_container_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class BalanceContainerFragment : BaseViewPagerFragment<
    BalanceContainerViewPagerAdapter,
    BalanceContainerPresenter
    >(), BalanceContainerContract.View {


    companion object {}


    override val mPresenter: BalanceContainerPresenter by inject { parametersOf(this) }

    private var mPopupMenu: PopupMenu? = null




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.wasWithdrawalJustConfirmed = it.wasWithdrawalJustConfirmed
            mPresenter.wasWithdrawalJustCancelled = it.wasWithdrawalJustCancelled
            mPresenter.selectedTab = it.selectedTab
        }
    }


    override fun initToolbar() {
        super.initToolbar()

        getToolbar()?.apply {
            if(mPresenter.selectedTab == BalanceTab.WALLETS) {
                showPreRightButton()
            } else {
                hidePreRightButton()
            }

            setOnPreRightButtonClickListener {
                mPresenter.onToolbarPreRightButtonClicked()
            }

            setOnRightButtonClickListener {
                mPresenter.onToolbarRightButtonClicked()
            }
        }
    }


    override fun populateAdapter() = with(mAdapter) {
        addFragment(getWalletsFragment())
        addFragment(getTransactionsFragment(BalanceTab.DEPOSITS, TransactionType.DEPOSITS))
        addFragment(getTransactionsFragment(BalanceTab.WITHDRAWALS, TransactionType.WITHDRAWALS))
    }


    override fun initTabLayoutTabs() = with(mRootView.tabLayout) {
        for(tab in BalanceTab.values()) {
            getTabAt(tab.ordinal)?.text = getStr(tab.titleId)
        }
    }


    override fun showToolbarPreRightButton(animate: Boolean) {
        mRootView.toolbar.showPreRightButton(animate)
    }


    override fun hideToolbarPreRightButton(animate: Boolean) {
        mRootView.toolbar.hidePreRightButton(animate)
    }


    override fun showPopupMenu(items: List<PopupMenuItemData>) {
        PopupMenu.newInstance(ctx).apply {
            ThemingUtil.Wallets.popupMenu(this, getAppTheme())

            setItems(items.mapToPopupMenuItems())

            onItemClickListener = { item ->
                mPresenter.onPopupMenuItemClicked(item.itemModel)
            }

            horizontalOffset = -resources.getDimensionPixelSize(R.dimen.popup_menu_offset)
            verticalOffset = (-mRootView.toolbar.height + resources.getDimensionPixelSize(R.dimen.popup_menu_offset))

            show(mRootView.toolbar.getRightButtonIv())
        }.also {
            mPopupMenu = it
        }
    }


    override fun hidePopupMenu() {
        mPopupMenu?.dismiss()
    }


    override fun showAppBar(animate: Boolean) = with(mRootView.appBarLayout) {
        setExpanded(true, animate)
    }


    override fun navigateToWalletsSearchScreen() {
        navigate(R.id.walletsSearchDest)
    }


    override fun navigateToTransactionsSearchScreen(transactionType: TransactionType) {
        navigate(
            destinationId = R.id.transactionsSearchDest,
            arguments = TransactionsSearchFragment.newArgs(transactionType)
        )
    }


    override fun setShowEmptyWallets(showEmptyWallets: Boolean) {
        (mAdapter.getFragment(BalanceTab.WALLETS.ordinal) as? WalletsFragment)
            ?.onShowEmptyWalletsFlagChanged(showEmptyWallets)
    }


    override fun setWalletsSortBalanceType(walletBalanceType: WalletBalanceType) {
        (mAdapter.getFragment(BalanceTab.WALLETS.ordinal) as? WalletsFragment)
            ?.onSortColumnChanged(walletBalanceType)
    }


    override fun getContentLayoutResourceId(): Int = R.layout.balance_container_fragment_layout


    override fun getInitialTabPosition(): Int = mPresenter.selectedTab.ordinal


    override fun getSelectedTabPosition(): Int = mRootView.tabLayout.selectedTabPosition


    override fun getToolbarTitle(): String = getStr(R.string.balance)


    override fun getToolbar(): Toolbar? = mRootView.toolbar


    override fun getTabLayout(): TabLayout = mRootView.tabLayout


    override fun getViewPager(): ViewPager = mRootView.viewPager


    override fun getViewPagerAdapter(): BalanceContainerViewPagerAdapter {
        return BalanceContainerViewPagerAdapter(childFragmentManager)
    }


    private fun BalanceContainerViewPagerAdapter.getWalletsFragment(): WalletsFragment {
        return ((getFragment(BalanceTab.WALLETS.ordinal)
                ?: WalletsFragment.newStandardInstance()) as WalletsFragment)
    }


    private fun BalanceContainerViewPagerAdapter.getTransactionsFragment(
        balanceTab: BalanceTab,
        transactionType: TransactionType
    ): TransactionsFragment {
        return getFragment(balanceTab.ordinal).let {
            if(it == null) {
                when(transactionType) {
                    TransactionType.DEPOSITS -> TransactionsFragment.newStandardInstance(transactionType)
                    TransactionType.WITHDRAWALS -> TransactionsFragment.newStandardInstance(
                        type = transactionType,
                        wasWithdrawalJustConfirmed = mPresenter.wasWithdrawalJustConfirmed,
                        wasWithdrawalJustCancelled = mPresenter.wasWithdrawalJustCancelled
                    )
                }
            } else {
                (it as TransactionsFragment)
            }.apply {
                toolbarProgressBarListener = mToolbarProgressBarListener
            }
        }
    }


    private val mToolbarProgressBarListener = object : ProgressBarListener {

        override fun showProgressBar() = with(mRootView.toolbar) {
            hideRightButton()
            showProgressBar()
        }

        override fun hideProgressBar() = with(mRootView.toolbar) {
            hideProgressBar()
            showRightButton()
        }

        override fun setInboxCountMessage(count: Int) {

        }

    }


}