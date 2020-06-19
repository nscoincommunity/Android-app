package com.stocksexchange.android.data.stores.alertprice

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.database.tables.AlertPriceTable
import com.stocksexchange.api.model.rest.parameters.AlertPriceParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class AlertPriceDatabaseDataStore(
    private val alertPriceTable: AlertPriceTable
) : AlertPriceDataStore {


    override suspend fun create(alertPriceParameters: AlertPriceParameters): Result<AlertPrice>  {
        throw UnsupportedOperationException()
    }


    override suspend fun delete(id: Int): Result<AlertPriceDeleteResponse> {
        return performBackgroundOperation {
            alertPriceTable.delete(id)
        }
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            alertPriceTable.deleteAll()
        }
    }


    override suspend fun get(params: AlertPriceParameters): Result<List<AlertPrice>> {
        return performBackgroundOperation {
            alertPriceTable.get(params)
        }
    }


    override suspend fun getAlertPriceListByPairId(id: Int): Result<List<AlertPrice>> {
        return performBackgroundOperation {
            alertPriceTable.getByPairId(id)
        }
    }


    override suspend fun save(alertPrices: List<AlertPrice>) {
        executeBackgroundOperation {
            alertPriceTable.save(alertPrices)
        }
    }


}