package com.stocksexchange.android.ui.currencymarketpreview.views.tradehistory

import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.formatters.TimeFormatter

class TradeHistoryResources(
    val priceMaxCharsLength: Int,
    val amountMaxCharsLength: Int,
    val stubText: String,
    val numberFormatter: NumberFormatter,
    val timeFormatter: TimeFormatter,
    val colors: List<Int>,
    val strings: List<String>
) : ItemResources {


    companion object {

        const val COLOR_HEADER_TITLE_TEXT = 0
        const val COLOR_HEADER_SEPARATOR = 1
        const val COLOR_BUY_TRADE_BACKGROUND_HIGHLIGHT = 2
        const val COLOR_SELL_TRADE_BACKGROUND_HIGHLIGHT = 3
        const val COLOR_BUY_TRADE_PRICE_HIGHLIGHT = 4
        const val COLOR_SELL_TRADE_PRICE_HIGHLIGHT = 5
        const val COLOR_BUY_TRADE_PRICE = 6
        const val COLOR_SELL_TRADE_PRICE = 7
        const val COLOR_TRADE_AMOUNT = 8
        const val COLOR_TRADE_TIME = 9
        const val COLOR_CANCEL_BUTTON_TEXT = 10
        const val COLOR_CANCELLATION_PROGRESS_BAR = 11

        const val STRING_ACTION_CANCEL = 0


        fun newInstance(priceMaxCharsLength: Int, amountMaxCharsLength: Int,
                        stubText: String, numberFormatter: NumberFormatter,
                        timeFormatter: TimeFormatter, colors: List<Int>,
                        strings: List<String>): TradeHistoryResources {
            return TradeHistoryResources(
                priceMaxCharsLength,
                amountMaxCharsLength,
                stubText,
                numberFormatter,
                timeFormatter,
                colors,
                strings
            )
        }


        fun getDefaultResources(numberFormatter: NumberFormatter,
                                timeFormatter: TimeFormatter): TradeHistoryResources {
            return TradeHistoryResources(
                priceMaxCharsLength = -1,
                amountMaxCharsLength = -1,
                stubText = "",
                numberFormatter = numberFormatter,
                timeFormatter = timeFormatter,
                colors = emptyList(),
                strings = emptyList()
            )
        }

    }


}