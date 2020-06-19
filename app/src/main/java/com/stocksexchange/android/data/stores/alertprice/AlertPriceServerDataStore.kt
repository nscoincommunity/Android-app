package com.stocksexchange.android.data.stores.alertprice

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.api.model.rest.parameters.AlertPriceParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class AlertPriceServerDataStore(
    private val stexRestApi: StexRestApi
) : AlertPriceDataStore {


    override suspend fun create(alertPriceParameters: AlertPriceParameters): Result<AlertPrice> {
        return performBackgroundOperation {
            stexRestApi.createAlertPriceItem(alertPriceParameters)
        }
    }


    override suspend fun delete(id: Int): Result<AlertPriceDeleteResponse> {
        return performBackgroundOperation {
            stexRestApi.deleteAlertPriceItem(id)
        }
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun get(params: AlertPriceParameters): Result<List<AlertPrice>> {
        return performBackgroundOperation {
            stexRestApi.getAlertPrices()
        }
    }


    override suspend fun getAlertPriceListByPairId(id: Int): Result<List<AlertPrice>> {
        return performBackgroundOperation {
            stexRestApi.getAlertPricesByPairId(id)
        }
    }


    override suspend fun save(alertPrices: List<AlertPrice>) {
        throw UnsupportedOperationException()
    }


}