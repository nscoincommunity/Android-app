package com.stocksexchange.android.data.stores.orders

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.api.model.rest.parameters.OrderCreationParameters
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class OrdersServerDataStore(
    private val stexRestApi: StexRestApi
) : OrdersDataStore {


    override suspend fun save(order: Order) {
        throw UnsupportedOperationException()
    }


    override suspend fun save(orders: List<Order>) {
        throw UnsupportedOperationException()
    }


    override suspend fun create(params: OrderCreationParameters): Result<Order> {
        return performBackgroundOperation {
            stexRestApi.createOrder(params)
        }
    }


    override suspend fun cancel(order: Order): Result<OrdersCancellationResponse> {
        return performBackgroundOperation {
            stexRestApi.cancelActiveOrder(order)
        }
    }


    override suspend fun delete(order: Order) {
        throw UnsupportedOperationException()
    }


    override suspend fun delete(status: OrderStatus) {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun search(params: OrderParameters, currencyPairIds: List<Int>): Result<List<Order>> {
        throw UnsupportedOperationException()
    }


    override suspend fun getOrder(id: Long): Result<Order> {
        throw UnsupportedOperationException()
    }


    override suspend fun getActiveOrders(params: OrderParameters): Result<List<Order>> {
        return performBackgroundOperation {
            stexRestApi.getActiveOrders(params)
        }
    }


    override suspend fun getHistoryOrders(params: OrderParameters): Result<List<Order>> {
        return performBackgroundOperation {
            stexRestApi.getHistoryOrders(params)
        }
    }


}