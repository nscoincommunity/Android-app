package com.stocksexchange.android.ui.currencymarkets.search

import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface CurrencyMarketsSearchContract {


    interface View : BaseView {

        fun sortAdapterItems(comparator: CurrencyMarketComparator)

    }


    interface ActionListener {

        fun onSortPanelTitleClicked(comparator: CurrencyMarketComparator)

    }


}