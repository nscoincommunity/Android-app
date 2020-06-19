package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.CandleStick
import com.stocksexchange.android.database.model.DatabaseCandleStick
import com.stocksexchange.api.model.rest.parameters.PriceChartDataParameters

fun CandleStick.mapToDatabaseCandleStick(
    parameters: PriceChartDataParameters
): DatabaseCandleStick {
    return DatabaseCandleStick(
        currencyPairId = parameters.currencyPairId,
        intervalName = parameters.interval.intervalName,
        openPrice = openPrice,
        highPrice = highPrice,
        lowPrice = lowPrice,
        closePrice = closePrice,
        volume = volume,
        timestamp = timestamp
    )
}


fun List<CandleStick>.mapToDatabaseCandleStickList(
    parameters: PriceChartDataParameters
): List<DatabaseCandleStick> {
    return map { it.mapToDatabaseCandleStick(parameters) }
}


fun DatabaseCandleStick.mapToCandleStick(): CandleStick {
    return CandleStick(
        openPrice = openPrice,
        highPrice = highPrice,
        lowPrice = lowPrice,
        closePrice = closePrice,
        volume = volume,
        timestamp = timestamp
    )
}


fun List<DatabaseCandleStick>.mapToCandleStickList(): List<CandleStick> {
    return map { it.mapToCandleStick() }
}