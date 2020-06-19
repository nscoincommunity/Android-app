package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.DepthChartViewTheme
import com.stocksexchange.core.providers.ColorProvider

class DepthChartViewThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<DepthChartViewTheme>(colorProvider) {


    override fun getDeepTealTheme(): DepthChartViewTheme {
        return DepthChartViewTheme(
            backgroundColor = getColor(R.color.deepTealDepthChartViewBackgroundColor),
            progressBarColor = getColor(R.color.deepTealDepthChartViewProgressbarColor),
            infoViewColor = getColor(R.color.deepTealDepthChartViewInfoViewColor),
            infoFieldsDefaultTextColor = getColor(R.color.deepTealDepthChartViewInfoFieldsDefaultTextColor),
            axisGridColor = getColor(R.color.deepTealDepthChartViewAxisGridColor),
            highlighterColor = getColor(R.color.deepTealDepthChartViewHighlighterColor),
            positiveColor = getColor(R.color.deepTealDepthChartViewPositiveColor),
            negativeColor = getColor(R.color.deepTealDepthChartViewNegativeColor),
            neutralColor = getColor(R.color.deepTealDepthChartViewNeutralColor),
            tabBackgroundColor = getColor(R.color.deepTealDepthChartViewTabBackgroundColor),
            tabNormalTextColor = getColor(R.color.deepTealDepthChartViewTabNormalTextColor),
            tabSelectedTextColor = getColor(R.color.deepTealDepthChartViewTabSelectedTextColor)
        )
    }


}