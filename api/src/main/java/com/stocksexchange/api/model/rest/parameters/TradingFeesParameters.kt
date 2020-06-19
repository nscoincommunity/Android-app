package com.stocksexchange.api.model.rest.parameters

import com.stocksexchange.core.utils.interfaces.HasUniqueKey


data class TradingFeesParameters(
    val currencyPairId: Int
) : HasUniqueKey {


    override val uniqueKey: String
        get() = currencyPairId.toString()


}