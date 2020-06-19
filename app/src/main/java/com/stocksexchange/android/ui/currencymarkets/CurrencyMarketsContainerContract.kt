package com.stocksexchange.android.ui.currencymarkets

import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.ui.base.viewpager.ViewPagerView

interface CurrencyMarketsContainerContract {


    interface View : ViewPagerView {

        fun showAppBar(animate: Boolean)

        fun sortAdapterItems(comparator: CurrencyMarketComparator)

        fun updateInboxButtonItemCount()

        fun navigateToSearchScreen()

        fun navigateToPriceAlertsScreen()

        fun navigateToInboxScreen()

        fun getTabCount(): Int

    }


    interface ActionListener {

        fun onToolbarRightButtonClicked()

        fun onToolbarAlertPriceButtonClicked()

        fun onToolbarInboxButtonClicked()

        fun onSortPanelTitleClicked(comparator: CurrencyMarketComparator)

    }


}