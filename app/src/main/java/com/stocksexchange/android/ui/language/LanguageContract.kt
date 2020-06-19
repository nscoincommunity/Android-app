package com.stocksexchange.android.ui.language

import com.stocksexchange.android.model.LanguageItemModel
import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface LanguageContract {


    interface View : BaseView {

        fun setToolbarRightButtonVisible(isVisible: Boolean)

        fun setItems(items: MutableList<LanguageItemModel>)

        fun isDataSetEmpty(): Boolean

    }


    interface ActionListener {

        fun onToolbarRightButtonClicked()

        fun onLanguageItemClicked(itemModel: LanguageItemModel)

    }


}