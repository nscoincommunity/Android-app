package com.stocksexchange.android.ui.help

import com.stocksexchange.android.model.HelpItemModel
import com.stocksexchange.android.ui.base.mvp.views.BaseView
import com.stocksexchange.core.model.NetworkInfo

interface HelpContract {


    interface View : BaseView {

        fun showProgressBar()

        fun hideProgressBar()

        fun setItems(items: List<HelpItemModel>)

        fun isDataSetEmpty(): Boolean

        fun getNetworkInfo(): NetworkInfo

    }


    interface ActionListener {

        fun onItemClicked(item: HelpItemModel)

    }


}