package com.stocksexchange.android.ui.wallets.search

import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.fragments.BaseSearchFragment
import com.stocksexchange.android.ui.views.toolbars.SearchToolbar
import com.stocksexchange.android.ui.wallets.fragment.WalletsFragment
import com.stocksexchange.android.ui.wallets.fragment.newSearchInstance
import kotlinx.android.synthetic.main.wallets_search_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class WalletsSearchFragment : BaseSearchFragment<WalletsFragment, WalletsSearchPresenter>(),
    WalletsSearchContract.View {


    override val mPresenter: WalletsSearchPresenter by inject { parametersOf(this) }




    override fun performSearch(query: String) {
        mFragment.onPerformSearch(query)
    }


    override fun reloadSearchQuery() {
        val searchQuery = getSearchQuery()
        mFragment.onPerformSearch("")
        mFragment.onPerformSearch(searchQuery)
    }


    override fun cancelSearch() {
        mFragment.onCancelSearch()
    }


    override fun getInputHint(): String {
        return getStr(R.string.wallets_search_fragment_input_hint_text)
    }


    override fun getSearchToolbar(): SearchToolbar = mRootView.searchToolbar


    override fun getActivityFragment(): WalletsFragment {
        return WalletsFragment.newSearchInstance()
    }


    override fun getContentLayoutResourceId(): Int = R.layout.wallets_search_fragment_layout


}