package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.PriceChartViewTheme
import com.stocksexchange.core.providers.ColorProvider

class PriceChartViewThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<PriceChartViewTheme>(colorProvider) {


    override fun getDeepTealTheme(): PriceChartViewTheme {
        return PriceChartViewTheme(
            backgroundColor = getColor(R.color.deepTealPriceChartViewBackgroundColor),
            progressBarColor = getColor(R.color.deepTealPriceChartViewProgressBarColor),
            infoViewColor = getColor(R.color.deepTealPriceChartViewInfoViewColor),
            infoFieldsDefaultTextColor = getColor(R.color.deepTealPriceChartViewInfoFieldsDefaultTextColor),
            axisGridColor = getColor(R.color.deepTealPriceChartViewAxisGridColor),
            highlighterColor = getColor(R.color.deepTealPriceChartViewHighlighterColor),
            positiveColor = getColor(R.color.deepTealPriceChartViewPositiveColor),
            negativeColor = getColor(R.color.deepTealPriceChartViewNegativeColor),
            neutralColor = getColor(R.color.deepTealPriceChartViewNeutralColor),
            volumeBarsColor = getColor(R.color.deepTealPriceChartViewVolumeBarsColor),
            candleStickShadowColor = getColor(R.color.deepTealPriceChartViewCandleStickShadowColor),
            tabBackgroundColor = getColor(R.color.deepTealPriceChartViewTabBackgroundColor),
            tabNormalTextColor = getColor(R.color.deepTealPriceChartViewTabNormalTextColor),
            tabSelectedTextColor = getColor(R.color.deepTealPriceChartViewTabSelectedTextColor)
        )
    }


}