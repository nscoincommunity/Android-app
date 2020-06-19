package com.stocksexchange.android.ui.orders.search

import android.os.Bundle
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.OrderLifecycleType
import com.stocksexchange.android.ui.base.fragments.BaseSearchFragment
import com.stocksexchange.android.ui.orders.fragment.OrdersFragment
import com.stocksexchange.android.ui.orders.fragment.newSearchInstance
import com.stocksexchange.android.ui.views.toolbars.SearchToolbar
import com.stocksexchange.core.utils.extensions.extract
import com.stocksexchange.core.utils.listeners.ProgressBarListener
import kotlinx.android.synthetic.main.orders_search_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class OrdersSearchFragment : BaseSearchFragment<OrdersFragment, OrdersSearchPresenter>(),
    OrdersSearchContract.View {


    companion object {}


    override val mPresenter: OrdersSearchPresenter by inject { parametersOf(this) }




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.orderLifecycleType = it.orderLifecycleType
        }
    }


    override fun performSearch(query: String) {
        mFragment.onPerformSearch(query)
    }


    override fun cancelSearch() {
        mFragment.onCancelSearch()
    }


    override fun getInputHint(): String {
        return getStr(when(mPresenter.orderLifecycleType) {
            OrderLifecycleType.ACTIVE -> R.string.orders_search_fragment_active_orders_hint
            OrderLifecycleType.COMPLETED -> R.string.orders_search_fragment_completed_orders_hint
            OrderLifecycleType.CANCELLED -> R.string.orders_search_fragment_cancelled_orders_hint
        })
    }


    override fun getSearchToolbar(): SearchToolbar = mRootView.searchToolbar


    override fun getActivityFragment(): OrdersFragment {
        return OrdersFragment.newSearchInstance(mPresenter.orderLifecycleType).apply {
            toolbarProgressBarListener = mToolbarProgressBarListener
        }
    }


    override fun getContentLayoutResourceId(): Int = R.layout.orders_search_fragment_layout


    private val mToolbarProgressBarListener = object : ProgressBarListener {

        override fun showProgressBar() = with(mRootView.searchToolbar) {
            hideClearInputButton(false)
            showProgressBar()
        }

        override fun hideProgressBar() = with(mRootView.searchToolbar) {
            hideProgressBar()
            showClearInputButton(false)
        }

        override fun setInboxCountMessage(count: Int) {

        }

    }


}