package com.stocksexchange.android.theming.model

import java.io.Serializable

data class DepthChartViewTheme(
    val backgroundColor: Int,
    val progressBarColor: Int,
    val infoViewColor: Int,
    val infoFieldsDefaultTextColor: Int,
    val axisGridColor: Int,
    val highlighterColor: Int,
    val positiveColor: Int,
    val negativeColor: Int,
    val neutralColor: Int,
    val tabBackgroundColor: Int,
    val tabNormalTextColor: Int,
    val tabSelectedTextColor: Int
) : Serializable {


    companion object {

        val STUB = DepthChartViewTheme(
            backgroundColor = -1,
            progressBarColor = -1,
            infoViewColor = -1,
            infoFieldsDefaultTextColor = -1,
            axisGridColor = -1,
            highlighterColor = -1,
            positiveColor = -1,
            negativeColor = -1,
            neutralColor = -1,
            tabBackgroundColor = -1,
            tabNormalTextColor = -1,
            tabSelectedTextColor = -1
        )
        
    }


}