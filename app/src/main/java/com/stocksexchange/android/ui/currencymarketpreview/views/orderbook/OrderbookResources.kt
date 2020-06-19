package com.stocksexchange.android.ui.currencymarketpreview.views.orderbook

import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.core.formatters.NumberFormatter

class OrderbookResources(
    val shouldHideHeaderMoreButton: Boolean,
    val priceMaxCharsLength: Int,
    val amountMaxCharsLength: Int,
    val stubText: String,
    val numberFormatter: NumberFormatter,
    val colors: List<Int>
) : ItemResources {


    companion object {

        const val COLOR_HEADER_TITLE_TEXT = 0
        const val COLOR_HEADER_MORE_BUTTON = 1
        const val COLOR_HEADER_SEPARATOR = 2
        const val COLOR_SELL_ORDER_BACKGROUND_HIGHLIGHT = 3
        const val COLOR_BUY_ORDER_BACKGROUND_HIGHLIGHT = 4
        const val COLOR_SELL_ORDER_PRICE_HIGHLIGHT = 5
        const val COLOR_BUY_ORDER_PRICE_HIGHLIGHT = 6
        const val COLOR_SELL_ORDER_PRICE = 7
        const val COLOR_BUY_ORDER_PRICE = 8
        const val COLOR_ORDER_AMOUNT = 9
        const val COLOR_SELL_ORDER_BACKGROUND = 10
        const val COLOR_BUY_ORDER_BACKGROUND = 11


        fun newInstance(shouldHideHeaderMoreButton: Boolean,
                        priceMaxCharsLength: Int, amountMaxCharsLength: Int,
                        stubText: String, numberFormatter: NumberFormatter,
                        colors: List<Int>): OrderbookResources {
            return OrderbookResources(
                shouldHideHeaderMoreButton,
                priceMaxCharsLength,
                amountMaxCharsLength,
                stubText,
                numberFormatter,
                colors
            )
        }


        fun getDefaultResources(numberFormatter: NumberFormatter): OrderbookResources {
            return OrderbookResources(
                shouldHideHeaderMoreButton = false,
                priceMaxCharsLength = -1,
                amountMaxCharsLength = -1,
                stubText = "",
                numberFormatter = numberFormatter,
                colors = emptyList()
            )
        }

    }


}