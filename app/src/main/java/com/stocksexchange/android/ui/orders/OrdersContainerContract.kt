package com.stocksexchange.android.ui.orders

import com.stocksexchange.android.ui.base.viewpager.ViewPagerView

interface OrdersContainerContract {


    interface View : ViewPagerView {

        fun showAppBar(animate: Boolean)

        fun navigateToOrdersSearchScreen()

        fun getPageCount(): Int

    }


    interface ActionListener {

        fun onToolbarRightButtonClicked()

    }


}