package com.stocksexchange.android.utils.extensions

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.FiatCurrency
import com.stocksexchange.android.ui.views.CurrencyMarketPriceInfoView
import com.stocksexchange.android.utils.handlers.FiatCurrencyPriceHandler
import com.stocksexchange.core.formatters.NumberFormatter

fun CurrencyMarketPriceInfoView.setData(
    currencyMarket: CurrencyMarket,
    formatter: NumberFormatter,
    currentFiatCurrency: FiatCurrency,
    fiatCurrencyPriceHandler: FiatCurrencyPriceHandler
) {
    priceText = formatter.formatFixedPrice(currencyMarket.lastPrice)
    fiatPriceText = fiatCurrencyPriceHandler.getMarketPreviewLastPriceInFiatCurrency(
        fiatCurrency = currentFiatCurrency,
        currencyMarket = currencyMarket
    ) ?: formatter.formatLastPriceChange(currencyMarket.dailyPriceChange)
    dailyPriceChangeText = formatter.formatDailyPriceChange(currencyMarket.dailyPriceChangeInPercentage)
}