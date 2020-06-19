package com.stocksexchange.android.utils.handlers

import android.widget.TextView
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.FiatCurrency
import com.stocksexchange.api.model.rest.FiatCurrencySymbolPosition
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.utils.extensions.getWithDefault
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible

class FiatCurrencyPriceHandler(private val numberFormatter: NumberFormatter) {


    /**
     * Handles the last price in fiat currency of the currency market item.
     *
     * @param fiatCurrency The currently selected fiat currency
     * @param currencyMarket The currency market
     * @param textView The view to bind the formatted last price to
     */
    fun handleMarketItemFiatCurrencyLastPrice(
        fiatCurrency: FiatCurrency,
        currencyMarket: CurrencyMarket,
        textView: TextView
    ) {
        val fiatCurrencyLastPrice = getMarketPreviewLastPriceInFiatCurrency(
            fiatCurrency = fiatCurrency,
            currencyMarket = currencyMarket
        )

        if(fiatCurrencyLastPrice == null) {
            textView.makeGone()
        } else {
            textView.text = fiatCurrencyLastPrice
            textView.makeVisible()
        }
    }


    /**
     * Retrieves a last price converted to the fiat currency for
     * the market preview.
     *
     * @param fiatCurrency The fiat currency to convert the last price to
     * @param currencyMarket The currency market
     *
     * @return The last price converted to the fiat currency
     */
    fun getMarketPreviewLastPriceInFiatCurrency(
        fiatCurrency: FiatCurrency,
        currencyMarket: CurrencyMarket
    ): String? {
        return getPriceInFiatCurrency(
            price = currencyMarket.lastPrice,
            fiatCurrency = fiatCurrency,
            fiatCurrencyRates = currencyMarket.fiatCurrencyRates
        ) {
            numberFormatter.formatFixedPriceInFiatCurrency(
                value = it,
                maxLengthSubtrahend = if(fiatCurrency.symbol.length == 2) 1 else 0
                // Adding a subtrahend for a symbol that is two characters long
                // to align the width of fiat price to that of the last price
            )
        }
    }


    /**
     * Retrieves a price converted to the fiat currency for the trade form.
     *
     * @param price The price to convert
     * @param fiatCurrency The fiat currency to convert the price to
     * @param fiatCurrencyRates The fiat currency rates
     *
     * @return The price converted to the fiat currency
     */
    fun getTradeFormLastPriceInFiatCurrency(
        price: Double,
        fiatCurrency: FiatCurrency,
        fiatCurrencyRates: Map<String, Double>
    ): String? {
        return getPriceInFiatCurrency(
            price = price,
            fiatCurrency = fiatCurrency,
            fiatCurrencyRates = fiatCurrencyRates
        ) {
            numberFormatter.formatPriceInFiatCurrency(it)
        }
    }


    private fun getPriceInFiatCurrency(
        price: Double,
        fiatCurrency: FiatCurrency,
        fiatCurrencyRates: Map<String, Double>,
        formatPrice: ((Double) -> String)
    ): String? {
        val fiatCurrencyRate = fiatCurrencyRates.getWithDefault(fiatCurrency.name, 0.0)

        return if(fiatCurrencyRate == 0.0) {
            null
        } else {
            val priceInFiatCurrency = (price * fiatCurrencyRate)
            val priceInFiatCurrencyStr = formatPrice(priceInFiatCurrency)

            addFiatCurrencySymbol(fiatCurrency, priceInFiatCurrencyStr)
        }
    }


    private fun addFiatCurrencySymbol(fiatCurrency: FiatCurrency, priceString: String): String {
        val stringBuilder = StringBuilder()

        return when(fiatCurrency.symbolPosition) {

            FiatCurrencySymbolPosition.START -> {
                stringBuilder.append(fiatCurrency.symbol)

                if(fiatCurrency.shouldAddExtraSpace) {
                    stringBuilder.append(" ")
                }

                stringBuilder.append(priceString)
            }

            FiatCurrencySymbolPosition.END -> {
                stringBuilder.append(priceString)

                if(fiatCurrency.shouldAddExtraSpace) {
                    stringBuilder.append(" ")
                }

                stringBuilder.append(fiatCurrency.symbol)
            }

        }.toString()
    }


}