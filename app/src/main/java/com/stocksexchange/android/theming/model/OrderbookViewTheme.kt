package com.stocksexchange.android.theming.model

import java.io.Serializable

data class OrderbookViewTheme(
    val backgroundColor: Int,
    val headerTitleTextColor: Int,
    val headerMoreButtonColor: Int,
    val headerSeparatorColor: Int,
    val buyPriceColor: Int,
    val buyPriceHighlightColor: Int,
    val buyOrderBackgroundColor: Int,
    val buyOrderHighlightBackgroundColor: Int,
    val sellPriceColor: Int,
    val sellPriceHighlightColor: Int,
    val sellOrderBackgroundColor: Int,
    val sellOrderHighlightBackgroundColor: Int,
    val amountColor: Int,
    val progressBarColor: Int,
    val infoViewColor: Int
) : Serializable {


    companion object {

        val STUB = OrderbookViewTheme(
            backgroundColor = -1,
            headerTitleTextColor = -1,
            headerMoreButtonColor = -1,
            headerSeparatorColor = -1,
            buyPriceColor = -1,
            buyPriceHighlightColor = -1,
            buyOrderBackgroundColor = -1,
            buyOrderHighlightBackgroundColor = -1,
            sellPriceColor = -1,
            sellPriceHighlightColor = -1,
            sellOrderBackgroundColor = -1,
            sellOrderHighlightBackgroundColor = -1,
            amountColor = -1,
            progressBarColor = -1,
            infoViewColor = -1
        )

    }


}