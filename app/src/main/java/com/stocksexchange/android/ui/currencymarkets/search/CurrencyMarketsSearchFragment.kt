package com.stocksexchange.android.ui.currencymarkets.search

import com.stocksexchange.android.R
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketsFragment
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.fragments.BaseSearchFragment
import com.stocksexchange.android.ui.currencymarkets.fragment.newSearchInstance
import com.stocksexchange.android.ui.views.toolbars.SearchToolbar
import com.stocksexchange.core.utils.extensions.attemptToSort
import kotlinx.android.synthetic.main.currency_markets_search_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class CurrencyMarketsSearchFragment : BaseSearchFragment<CurrencyMarketsFragment, CurrencyMarketsSearchPresenter>(),
    CurrencyMarketsSearchContract.View {


    companion object {}


    override val mPresenter: CurrencyMarketsSearchPresenter by inject { parametersOf(this) }




    override fun init() {
        super.init()

        initCurrencyMarketsSortPanel()
    }


    private fun initCurrencyMarketsSortPanel() = with(mRootView.currencyMarketsSortPanel) {
        onSortPanelTitleClickListener = {
            mPresenter.onSortPanelTitleClicked(it.comparator)
        }

        initComparator()

        ThemingUtil.CurrencyMarketsSearch.sortPanel(this, getAppTheme())
    }


    override fun sortAdapterItems(comparator: CurrencyMarketComparator) {
        mFragment.attemptToSort(comparator)
    }


    override fun performSearch(query: String) {
        mFragment.onPerformSearch(query)
    }


    override fun cancelSearch() {
        mFragment.onCancelSearch()
    }


    override fun getInputHint(): String {
        return getStr(R.string.action_search_markets)
    }


    override fun getSearchToolbar(): SearchToolbar = mRootView.searchToolbar


    override fun getActivityFragment(): CurrencyMarketsFragment {
        return CurrencyMarketsFragment.newSearchInstance()
    }


    override fun getContentLayoutResourceId() = R.layout.currency_markets_search_fragment_layout


}