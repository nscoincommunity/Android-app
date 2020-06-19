package com.stocksexchange.android.ui.orders

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.viewpager.BaseViewPagerFragment
import com.stocksexchange.android.ui.orders.fragment.*
import com.stocksexchange.android.ui.orders.search.OrdersSearchFragment
import com.stocksexchange.android.ui.orders.search.newArgs
import com.stocksexchange.android.ui.views.toolbars.Toolbar
import com.stocksexchange.api.model.rest.OrderLifecycleType
import com.stocksexchange.api.model.rest.OrderSelectivityType
import com.stocksexchange.core.utils.extensions.containsBits
import com.stocksexchange.core.utils.extensions.getSerializableOrThrow
import com.stocksexchange.core.utils.listeners.ProgressBarListener
import kotlinx.android.synthetic.main.orders_container_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class OrdersContainerFragment : BaseViewPagerFragment<
    OrdersContainerViewPagerAdapter,
    OrdersContainerPresenter
    >(), OrdersContainerContract.View {


    companion object {

        const val PAGE_ALL = 0b111
        const val PAGE_ACTIVE_ORDERS = 0b001
        const val PAGE_COMPLETED_ORDERS = 0b010
        const val PAGE_CANCELLED_ORDERS = 0b100

        private const val KEY_ENABLED_PAGES = "enabled_pages"
        private const val KEY_CURRENCY_PAIR_ID = "currency_pair_id"
        private const val KEY_SELECTIVITY_TYPE = "selectivity_type"


        fun newArgs(
            enabledPages: Int = PAGE_ALL,
            currencyPairId: Int = 0,
            selectivityType: OrderSelectivityType = OrderSelectivityType.ANY_PAIR_ID
        ): Bundle {
            return bundleOf(
                KEY_ENABLED_PAGES to enabledPages,
                KEY_CURRENCY_PAIR_ID to currencyPairId,
                KEY_SELECTIVITY_TYPE to selectivityType
            )
        }

    }


    override val mPresenter: OrdersContainerPresenter by inject { parametersOf(this) }

    private var mEnabledPages: Int = PAGE_ALL
    private var mCurrencyPairId: Int = 0

    private var mSelectivityType: OrderSelectivityType = OrderSelectivityType.ANY_PAIR_ID




    override fun initToolbar() = with(mRootView.toolbar) {
        super.initToolbar()

        setOnRightButtonClickListener {
            mPresenter.onToolbarRightButtonClicked()
        }
    }


    override fun populateAdapter() = with(mAdapter) {
        if(hasPage(PAGE_ACTIVE_ORDERS)) {
            addFragment(getActiveOrdersFragment())
        }

        if(hasPage(PAGE_COMPLETED_ORDERS)) {
            addFragment(getCompletedOrdersFragment())
        }

        if(hasPage(PAGE_CANCELLED_ORDERS)) {
            addFragment(getCancelledOrdersFragment())
        }
    }


    override fun initTabLayoutTabs() = with(mRootView.tabLayout) {
        if(hasPage(PAGE_ACTIVE_ORDERS)) {
            getTabAt(getIndexForPage(PAGE_ACTIVE_ORDERS))?.text = (
                getStr(R.string.orders_container_tab_active_orders_title)
            )
        }

        if(hasPage(PAGE_COMPLETED_ORDERS)) {
            getTabAt(getIndexForPage(PAGE_COMPLETED_ORDERS))?.text = (
                getStr(R.string.orders_container_tab_completed_orders_title)
            )
        }

        if(hasPage(PAGE_CANCELLED_ORDERS)) {
            getTabAt(getIndexForPage(PAGE_CANCELLED_ORDERS))?.text = (
                getStr(R.string.orders_container_tab_cancelled_orders_title)
            )
        }
    }


    override fun showAppBar(animate: Boolean) = with(mRootView.appBarLayout) {
        setExpanded(true, animate)
    }


    override fun navigateToOrdersSearchScreen() {
        val getPageIndex: ((Int) -> Int) = {
            if(hasPage(it)) getIndexForPage(it) else -1
        }

        val orderLifecycleType = when(mRootView.tabLayout.selectedTabPosition) {
            getPageIndex(PAGE_ACTIVE_ORDERS) -> OrderLifecycleType.ACTIVE
            getPageIndex(PAGE_COMPLETED_ORDERS) -> OrderLifecycleType.COMPLETED
            getPageIndex(PAGE_CANCELLED_ORDERS) -> OrderLifecycleType.CANCELLED

            else -> throw IllegalStateException()
        }

        navigate(
            destinationId = R.id.ordersSearchDest,
            arguments = OrdersSearchFragment.newArgs(orderLifecycleType)
        )
    }


    private fun hasPage(page: Int): Boolean = mEnabledPages.containsBits(page)


    override fun getPageCount(): Int {
        var pageCount = 0

        if(hasPage(PAGE_ACTIVE_ORDERS)) {
            pageCount++
        }

        if(hasPage(PAGE_COMPLETED_ORDERS)) {
            pageCount++
        }

        if(hasPage(PAGE_CANCELLED_ORDERS)) {
            pageCount++
        }

        return pageCount
    }


    private fun getIndexForPage(page: Int): Int {
        val pageCount = getPageCount()

        return if(page == PAGE_COMPLETED_ORDERS) {
            if((pageCount == 3) || ((pageCount == 2) && hasPage(PAGE_ACTIVE_ORDERS))) {
                1
            } else {
                0
            }
        } else if(page == PAGE_CANCELLED_ORDERS) {
            if(pageCount == 3) {
                2
            } else if((pageCount == 2) && (hasPage(PAGE_ACTIVE_ORDERS) || hasPage(PAGE_COMPLETED_ORDERS))) {
                1
            } else {
                0
            }
        } else {
            0
        }
    }


    override fun getContentLayoutResourceId(): Int = R.layout.orders_container_fragment_layout


    override fun getToolbarTitle(): String {
        val containsOnlyHistoryOrders = (
            (getPageCount() == 2) &&
            hasPage(PAGE_COMPLETED_ORDERS) &&
            hasPage(PAGE_CANCELLED_ORDERS)
        )

        return getStr(if(containsOnlyHistoryOrders) {
            R.string.orders_container_order_history_title
        } else {
            R.string.orders_container_orders_title
        })
    }


    override fun getToolbar(): Toolbar? = mRootView.toolbar


    override fun getTabLayout(): TabLayout = mRootView.tabLayout


    override fun getViewPager(): ViewPager = mRootView.viewPager


    override fun getViewPagerAdapter(): OrdersContainerViewPagerAdapter {
        return OrdersContainerViewPagerAdapter(childFragmentManager)
    }


    private fun getActiveOrdersFragment(): OrdersFragment {
        var activeOrdersFragment = mAdapter.getFragment(getIndexForPage(PAGE_ACTIVE_ORDERS))

        if(activeOrdersFragment == null) {
            activeOrdersFragment = if(mSelectivityType == OrderSelectivityType.ANY_PAIR_ID) {
                OrdersFragment.newAllActiveOrdersInstance()
            } else {
                OrdersFragment.newSpecificActiveOrdersInstance(mCurrencyPairId)
            }
        } else {
            (activeOrdersFragment as OrdersFragment)
        }

        with(activeOrdersFragment) {
            toolbarProgressBarListener = mToolbarProgressBarListener
        }

        return activeOrdersFragment
    }


    private fun getCompletedOrdersFragment(): OrdersFragment {
        var completedOrdersFragment = mAdapter.getFragment(getIndexForPage(PAGE_COMPLETED_ORDERS))

        if(completedOrdersFragment == null) {
            completedOrdersFragment = if(mSelectivityType == OrderSelectivityType.ANY_PAIR_ID) {
                OrdersFragment.newAllCompletedOrdersInstance()
            } else {
                OrdersFragment.newSpecificCompletedOrdersInstance(mCurrencyPairId)
            }
        } else {
            (completedOrdersFragment as OrdersFragment)
        }

        return completedOrdersFragment
    }


    private fun getCancelledOrdersFragment(): OrdersFragment {
        var cancelledOrdersFragment = mAdapter.getFragment(getIndexForPage(PAGE_CANCELLED_ORDERS))

        if(cancelledOrdersFragment == null) {
            cancelledOrdersFragment = if(mSelectivityType == OrderSelectivityType.ANY_PAIR_ID) {
                OrdersFragment.newAllCancelledOrdersInstance()
            } else {
                OrdersFragment.newSpecificCancelledOrdersInstance(mCurrencyPairId)
            }
        } else {
            (cancelledOrdersFragment as OrdersFragment)
        }

        return cancelledOrdersFragment
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


    override fun onRestoreState(savedState: Bundle) {
        super.onRestoreState(savedState)

        (savedState ?: arguments)?.apply {
            mEnabledPages = getInt(KEY_ENABLED_PAGES, PAGE_ALL)
            mCurrencyPairId = getInt(KEY_CURRENCY_PAIR_ID, 0)
            mSelectivityType = getSerializableOrThrow(KEY_SELECTIVITY_TYPE)
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        with(savedState) {
            putInt(KEY_ENABLED_PAGES, mEnabledPages)
            putInt(KEY_CURRENCY_PAIR_ID, mCurrencyPairId)
            putSerializable(KEY_SELECTIVITY_TYPE, mSelectivityType)
        }
    }


}