package com.stocksexchange.android.ui.alertprice

import com.stocksexchange.api.model.rest.AlertPrice

class AlertPairItem(
    val currencyPairId: Int,
    val currencyPairName: String,
    var lessAlertPrice: AlertPrice?,
    var moreAlertPrice: AlertPrice?
) {


    val isMoreLessEmpty: Boolean
        get() = ((lessAlertPrice == null) && (moreAlertPrice == null))


    val isOneMoreLessItemEmpty: Boolean
        get() = ((lessAlertPrice == null) || (moreAlertPrice == null))


    fun deleteAlertPriceInPairItemById(deleteId: Int): AlertPairItem {
        moreAlertPrice?.let {
            if (it.id == deleteId) {
                moreAlertPrice = null
            }
        }

        lessAlertPrice?.let {
            if (it.id == deleteId) {
                lessAlertPrice = null
            }
        }

        return this
    }


}