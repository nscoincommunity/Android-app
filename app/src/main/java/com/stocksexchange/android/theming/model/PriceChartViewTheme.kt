package com.stocksexchange.android.theming.model

import java.io.Serializable

data class PriceChartViewTheme(
    val backgroundColor: Int,
    val progressBarColor: Int,
    val infoViewColor: Int,
    val infoFieldsDefaultTextColor: Int,
    val axisGridColor: Int,
    val highlighterColor: Int,
    val positiveColor: Int,
    val negativeColor: Int,
    val neutralColor: Int,
    val volumeBarsColor: Int,
    val candleStickShadowColor: Int,
    val tabBackgroundColor: Int,
    val tabNormalTextColor: Int,
    val tabSelectedTextColor: Int
) : Serializable {


    companion object {

        val STUB = PriceChartViewTheme(
            backgroundColor = -1,
            progressBarColor = -1,
            infoViewColor = -1,
            infoFieldsDefaultTextColor = -1,
            axisGridColor = -1,
            highlighterColor = -1,
            positiveColor = -1,
            negativeColor = -1,
            neutralColor = -1,
            volumeBarsColor = -1,
            candleStickShadowColor = -1,
            tabBackgroundColor = -1,
            tabNormalTextColor = -1,
            tabSelectedTextColor = -1
        )

    }


}