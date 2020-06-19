package com.stocksexchange.android.ui.wallets.search

import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface WalletsSearchContract {


    interface View : BaseView {

        fun reloadSearchQuery()

    }


    interface ActionListener


}