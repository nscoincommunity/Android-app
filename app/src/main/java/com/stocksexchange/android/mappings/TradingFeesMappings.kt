package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.TradingFees
import com.stocksexchange.android.database.model.DatabaseTradingFees
import com.stocksexchange.api.model.rest.parameters.TradingFeesParameters

fun TradingFees.mapToDatabaseTradingFees(
    parameters: TradingFeesParameters
): DatabaseTradingFees {
    return DatabaseTradingFees(
        currencyPairId = parameters.currencyPairId,
        buyFee = buyFee,
        sellFee = sellFee
    )
}


fun DatabaseTradingFees.mapToTradingFees(): TradingFees {
    return TradingFees(
        buyFee = buyFee,
        sellFee = sellFee
    )
}