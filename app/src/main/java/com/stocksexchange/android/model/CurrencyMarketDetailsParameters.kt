package com.stocksexchange.android.model

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.parameters.TradingFeesParameters

@Suppress("DataClassPrivateConstructor")
data class CurrencyMarketDetailsParameters private constructor(
    val isUserSignedIn: Boolean,
    val currencyMarket: CurrencyMarket,
    val tradingFeesParameters: TradingFeesParameters
) {


    companion object {

        fun newInstance(
            isUserSignedIn: Boolean,
            currencyMarket: CurrencyMarket
        ): CurrencyMarketDetailsParameters {
            return CurrencyMarketDetailsParameters(
                isUserSignedIn = isUserSignedIn,
                currencyMarket = currencyMarket,
                tradingFeesParameters = TradingFeesParameters(
                    currencyPairId = currencyMarket.pairId
                )
            )
        }

    }


}