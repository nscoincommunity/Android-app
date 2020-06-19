package com.stocksexchange.android.theming.model

import java.io.Serializable

data class TradeHistoryViewTheme(
    val backgroundColor: Int,
    val headerTitleTextColor: Int,
    val headerSeparatorColor: Int,
    val buyHighlightBackgroundColor: Int,
    val sellHighlightBackgroundColor: Int,
    val buyPriceHighlightColor: Int,
    val sellPriceHighlightColor: Int,
    val buyPriceColor: Int,
    val sellPriceColor: Int,
    val amountColor: Int,
    val tradeTimeColor: Int,
    val progressBarColor: Int,
    val infoViewColor: Int
) : Serializable {


    companion object {

        val STUB = TradeHistoryViewTheme(
            backgroundColor = -1,
            headerTitleTextColor = -1,
            headerSeparatorColor = -1,
            buyHighlightBackgroundColor = -1,
            sellHighlightBackgroundColor = -1,
            buyPriceHighlightColor = -1,
            sellPriceHighlightColor = -1,
            buyPriceColor = -1,
            sellPriceColor = -1,
            amountColor = -1,
            tradeTimeColor = -1,
            progressBarColor = -1,
            infoViewColor = -1
        )

    }


}