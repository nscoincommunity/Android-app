package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.OrderbookViewTheme
import com.stocksexchange.core.providers.ColorProvider

class OrderbookViewThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<OrderbookViewTheme>(colorProvider) {


    override fun getDeepTealTheme(): OrderbookViewTheme {
        return OrderbookViewTheme(
            backgroundColor = getColor(R.color.deepTealOrderbookViewBackgroundColor),
            headerTitleTextColor = getColor(R.color.deepTealOrderbookViewHeaderTitleTextColor),
            headerMoreButtonColor = getColor(R.color.deepTealOrderbookViewHeaderMoreButtonColor),
            headerSeparatorColor = getColor(R.color.deepTealOrderbookViewHeaderSeparatorColor),
            buyPriceColor = getColor(R.color.deepTealOrderbookViewBuyPriceColor),
            buyPriceHighlightColor = getColor(R.color.deepTealOrderbookViewBuyPriceHighlightColor),
            buyOrderBackgroundColor = getColor(R.color.deepTealOrderbookViewBuyOrderBackgroundColor),
            buyOrderHighlightBackgroundColor = getColor(R.color.deepTealOrderbookViewBuyOrderHighlightBackgroundColor),
            sellPriceColor = getColor(R.color.deepTealOrderbookViewSellPriceColor),
            sellPriceHighlightColor = getColor(R.color.deepTealOrderbookViewSellPriceHighlightColor),
            sellOrderBackgroundColor = getColor(R.color.deepTealOrderbookViewSellOrderBackgroundColor),
            sellOrderHighlightBackgroundColor = getColor(R.color.deepTealOrderbookViewSellOrderHighlightBackgroundColor),
            amountColor = getColor(R.color.deepTealOrderbookViewAmountColor),
            progressBarColor = getColor(R.color.deepTealOrderbookViewProgressBarColor),
            infoViewColor = getColor(R.color.deepTealOrderbookViewInfoViewColor)
        )
    }


}