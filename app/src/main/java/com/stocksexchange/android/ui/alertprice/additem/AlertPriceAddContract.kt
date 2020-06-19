package com.stocksexchange.android.ui.alertprice.additem

import com.stocksexchange.android.ui.base.mvp.views.BaseView
import com.stocksexchange.api.model.rest.AlertPrice

interface AlertPriceAddContract {


    interface View : BaseView {

        fun deleteAlertPriceItem(id: Int)

        fun showMoreLessItems(alertPriceItems: List<AlertPrice>)

        fun updateLastPrice(lastPrice: Double)

        fun updateFavoriteButtonState(isFavorite: Boolean)

        fun showToolbarProgressBar()

        fun hideToolbarProgressBar()

        fun showKeyboard()

        fun hideKeyboard()

        fun setTextToInputField(price: Double)

        fun getLessAlertPriceItem(): AlertPrice?

        fun getMoreAlertPriceItem(): AlertPrice?

        fun getInputText(): String

        fun getPriceFormattedString(price: Double): String

    }


    interface ActionListener {

        fun onDeletePriceItem(id: Int)

        fun onShowMoreLessItems(id: Int)

        fun onClickCreateAlertPrice()

        fun onRightButtonClicked()

        fun onClickClearButton()

        fun onClickDeleteLessItem()

        fun onClickDeleteMoreItem()

    }


}