package com.stocksexchange.android.ui.currencymarkets.fragment

import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.R
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.handlers.FiatCurrencyPriceHandler
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.formatters.NumberFormatter

data class CurrencyMarketResources(
    val volumeTemplate: String,
    val numberFormatter: NumberFormatter
) : ItemResources {


    companion object {

        fun newInstance(stringProvider: StringProvider,
                        numberFormatter: NumberFormatter): CurrencyMarketResources {
            val volumeTemplate = stringProvider.getString(R.string.currency_market_item_volume_template)

            return CurrencyMarketResources(
                volumeTemplate,
                numberFormatter
            )
        }

    }


    var baseCurrencySymbolCharacterLimit: Int = 0

    lateinit var fiatCurrencyPriceHandler: FiatCurrencyPriceHandler

    lateinit var settings: Settings


}