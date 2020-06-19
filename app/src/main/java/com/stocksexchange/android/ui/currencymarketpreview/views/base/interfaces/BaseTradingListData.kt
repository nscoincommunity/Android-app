package com.stocksexchange.android.ui.currencymarketpreview.views.base.interfaces

/**
 * An interface to implement for list data item
 * classes that are highlightable.
 */
interface BaseTradingListData {


    companion object {

        const val NO_TIMESTAMP = -1L

    }


    val highlightEndTimestamp: Long




    fun shouldBeHighlighted(): Boolean {
        return ((highlightEndTimestamp != NO_TIMESTAMP) && (calculateHighlightDuration() > 0L))
    }


    fun calculateHighlightDuration(): Long {
        return (highlightEndTimestamp - System.currentTimeMillis())
    }


}