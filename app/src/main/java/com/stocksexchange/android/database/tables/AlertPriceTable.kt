package com.stocksexchange.android.database.tables

import com.stocksexchange.android.database.daos.AlertPriceDao
import com.stocksexchange.android.database.model.DatabaseAlertPrice
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.mappings.mapToDatabaseAlertPriceList
import com.stocksexchange.android.mappings.mapToAlertPriceList
import com.stocksexchange.android.mappings.mapToDatabaseAlertPrice
import com.stocksexchange.api.model.rest.AlertPrice
import com.stocksexchange.api.model.rest.AlertPriceDeleteResponse
import com.stocksexchange.api.model.rest.parameters.AlertPriceParameters
import com.stocksexchange.core.model.Result

class AlertPriceTable(
    private val alertPriceDao: AlertPriceDao
) : BaseTable() {


    @Synchronized
    fun save(priceAlert: List<AlertPrice>) {
        alertPriceDao.insert(priceAlert.mapToDatabaseAlertPriceList())
    }


    @Synchronized
    fun save(priceAlert: AlertPrice): Result<AlertPrice> {
        alertPriceDao.insert(priceAlert.mapToDatabaseAlertPrice())

        return Result.Success(priceAlert)
    }


    @Synchronized
    fun deleteAll() {
        alertPriceDao.deleteAll()
    }


    @Synchronized
    fun get(params: AlertPriceParameters): Result<List<AlertPrice>> {
        return alertPriceDao.get().toResult(
            List<DatabaseAlertPrice>::mapToAlertPriceList
        )
    }


    @Synchronized
    fun getByPairId(id: Int): Result<List<AlertPrice>> {
        return alertPriceDao.getAlertPriceByPairId(id).toResult(
            List<DatabaseAlertPrice>::mapToAlertPriceList
        )
    }


    @Synchronized
    fun delete(id: Int): Result<AlertPriceDeleteResponse> {
        alertPriceDao.delete(id)

        return Result.Success(AlertPriceDeleteResponse("ok"))
    }


}