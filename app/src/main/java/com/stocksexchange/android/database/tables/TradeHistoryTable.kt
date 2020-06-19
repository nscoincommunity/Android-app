package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.android.database.daos.TradeHistoryDao
import com.stocksexchange.android.database.model.DatabaseTrade
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.database.utils.QueryBuilder
import com.stocksexchange.android.mappings.mapToDatabaseTrade
import com.stocksexchange.android.mappings.mapToDatabaseTradeList
import com.stocksexchange.android.mappings.mapToTradeList
import com.stocksexchange.api.model.rest.parameters.TradeHistoryParameters
import com.stocksexchange.core.model.Result

class TradeHistoryTable(
    private val tradeHistoryDao: TradeHistoryDao
) : BaseTable() {


    @Synchronized
    fun save(params: TradeHistoryParameters,
             trade: Trade) {
        tradeHistoryDao.insert(trade.mapToDatabaseTrade(params))
    }


    @Synchronized
    fun save(params: TradeHistoryParameters,
             trades: List<Trade>) {
        tradeHistoryDao.insert(trades.mapToDatabaseTradeList(params))
    }


    @Synchronized
    fun delete(currencyPairId: Int) {
        tradeHistoryDao.delete(currencyPairId)
    }


    @Synchronized
    fun get(params: TradeHistoryParameters): Result<List<Trade>> {
        val query = QueryBuilder.TradeHistory.getQuery(
            currencyPairId = params.currencyPairId,
            sortOrder = params.sortOrder.name,
            count = params.count
        )

        return tradeHistoryDao.get(query).toResult(
            List<DatabaseTrade>::mapToTradeList
        )
    }


}