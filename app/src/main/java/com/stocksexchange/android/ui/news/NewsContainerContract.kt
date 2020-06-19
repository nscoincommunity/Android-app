package com.stocksexchange.android.ui.news

import com.stocksexchange.android.ui.base.viewpager.ViewPagerView

interface NewsContainerContract {


    interface View : ViewPagerView {

        fun showAppBar(animate: Boolean)

    }


    interface ActionListener


}