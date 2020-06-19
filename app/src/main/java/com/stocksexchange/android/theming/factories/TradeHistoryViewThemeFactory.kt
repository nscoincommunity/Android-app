package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.TradeHistoryViewTheme
import com.stocksexchange.core.providers.ColorProvider

class TradeHistoryViewThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<TradeHistoryViewTheme>(colorProvider) {


    override fun getDeepTealTheme(): TradeHistoryViewTheme {
        return TradeHistoryViewTheme(
            backgroundColor = getColor(R.color.deepTealTradeHistoryViewBackgroundColor),
            headerTitleTextColor = getColor(R.color.deepTealTradeHistoryViewHeaderTitleTextColor),
            headerSeparatorColor = getColor(R.color.deepTealTradeHistoryViewHeaderSeparatorColor),
            buyHighlightBackgroundColor = getColor(R.color.deepTealTradeHistoryViewBuyHighlightBackgroundColor),
            sellHighlightBackgroundColor = getColor(R.color.deepTealTradeHistoryViewSellHighlightBackgroundColor),
            buyPriceHighlightColor = getColor(R.color.deepTealTradeHistoryViewBuyPriceHighlightColor),
            sellPriceHighlightColor = getColor(R.color.deepTealTradeHistoryViewSellPriceHighlightColor),
            buyPriceColor = getColor(R.color.deepTealTradeHistoryViewBuyPriceColor),
            sellPriceColor = getColor(R.color.deepTealTradeHistoryViewSellPriceColor),
            amountColor = getColor(R.color.deepTealTradeHistoryViewAmountColor),
            tradeTimeColor = getColor(R.color.deepTealTradeHistoryViewTradeTimeColor),
            progressBarColor = getColor(R.color.deepTealTradeHistoryViewProgressBarColor),
            infoViewColor = getColor(R.color.deepTealTradeHistoryViewInfoViewColor)
        )
    }


}