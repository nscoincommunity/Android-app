package com.stocksexchange.android.data.stores.orders

import com.stocksexchange.api.model.rest.OrdersCancellationResponse
import com.stocksexchange.api.model.rest.Order
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import com.stocksexchange.android.data.base.OrdersData
import com.stocksexchange.core.model.Result

interface OrdersDataStore : OrdersData<
    Result<Order>,
    Result<OrdersCancellationResponse>,
    Result<Order>,
    Result<List<Order>>
> {

    suspend fun search(params: OrderParameters, currencyPairIds: List<Int>): Result<List<Order>>

}