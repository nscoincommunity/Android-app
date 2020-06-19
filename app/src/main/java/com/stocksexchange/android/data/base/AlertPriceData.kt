package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.AlertPrice
import com.stocksexchange.api.model.rest.parameters.AlertPriceParameters

interface AlertPriceData<
    AlertPriceFetchingResult,
    AlertPriceDeleteResult,
    AlertPriceCreateResult
> {

    suspend fun create(alertPriceParameters: AlertPriceParameters): AlertPriceCreateResult

    suspend fun delete(id: Int): AlertPriceDeleteResult

    suspend fun deleteAll()

    suspend fun get(params: AlertPriceParameters): AlertPriceFetchingResult

    suspend fun getAlertPriceListByPairId(id: Int): AlertPriceFetchingResult

    suspend fun save(alertPrices: List<AlertPrice>)

}