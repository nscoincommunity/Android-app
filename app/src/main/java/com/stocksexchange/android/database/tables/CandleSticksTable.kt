package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.CandleStick
import com.stocksexchange.android.database.daos.CandleStickDao
import com.stocksexchange.android.database.model.DatabaseCandleStick
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.mappings.mapToCandleStickList
import com.stocksexchange.android.mappings.mapToDatabaseCandleStick
import com.stocksexchange.android.mappings.mapToDatabaseCandleStickList
import com.stocksexchange.api.model.rest.parameters.PriceChartDataParameters
import com.stocksexchange.core.model.Result

class CandleSticksTable(
    private val candleStickDao: CandleStickDao
) : BaseTable() {


    @Synchronized
    fun save(params: PriceChartDataParameters, candleStick: CandleStick) {
        candleStickDao.insert(candleStick.mapToDatabaseCandleStick(params))
    }


    @Synchronized
    fun save(params: PriceChartDataParameters, candleSticks: List<CandleStick>) {
        candleStickDao.insert(candleSticks.mapToDatabaseCandleStickList(params))
    }


    @Synchronized
    fun delete(params: PriceChartDataParameters) {
        candleStickDao.delete(
            currencyPairId = params.currencyPairId,
            intervalName = params.interval.intervalName
        )
    }


    @Synchronized
    fun get(params: PriceChartDataParameters): Result<List<CandleStick>> {
        return candleStickDao.get(
            currencyPairId = params.currencyPairId,
            intervalName = params.interval.intervalName,
            count = params.count
        ).toResult(List<DatabaseCandleStick>::mapToCandleStickList)
    }


}