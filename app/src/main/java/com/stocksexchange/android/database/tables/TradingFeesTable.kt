package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.TradingFees
import com.stocksexchange.android.database.daos.TradingFeesDao
import com.stocksexchange.android.database.model.DatabaseTradingFees
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.mappings.mapToDatabaseTradingFees
import com.stocksexchange.android.mappings.mapToTradingFees
import com.stocksexchange.api.model.rest.parameters.TradingFeesParameters
import com.stocksexchange.core.model.Result

class TradingFeesTable(
    private val tradingFeesDao: TradingFeesDao
) : BaseTable() {


    @Synchronized
    fun save(params: TradingFeesParameters, tradingFees: TradingFees) {
        tradingFeesDao.insert(tradingFees.mapToDatabaseTradingFees(params))
    }


    @Synchronized
    fun get(params: TradingFeesParameters): Result<TradingFees> {
        return tradingFeesDao.get(params.currencyPairId).toResult(
            DatabaseTradingFees::mapToTradingFees
        )
    }


}