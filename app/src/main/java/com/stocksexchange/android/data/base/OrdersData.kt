package com.stocksexchange.android.data.base

import com.stocksexchange.api.model.rest.Order
import com.stocksexchange.api.model.rest.parameters.OrderCreationParameters
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import com.stocksexchange.api.model.rest.OrderStatus

interface OrdersData<
    OrderCreationResult,
    OrderCancellationResult,
    OrderFetchingResult,
    OrdersFetchingResult
> {

    suspend fun save(order: Order)

    suspend fun save(orders: List<Order>)

    suspend fun create(params: OrderCreationParameters): OrderCreationResult

    suspend fun cancel(order: Order): OrderCancellationResult

    suspend fun delete(order: Order)

    suspend fun delete(status: OrderStatus)

    suspend fun deleteAll()

    suspend fun getOrder(id: Long): OrderFetchingResult

    suspend fun getActiveOrders(params: OrderParameters): OrdersFetchingResult

    suspend fun getHistoryOrders(params: OrderParameters): OrdersFetchingResult

}