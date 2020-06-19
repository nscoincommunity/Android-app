package com.stocksexchange.android.data.stores.orders

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.database.tables.OrdersTable
import com.stocksexchange.api.model.rest.parameters.OrderCreationParameters
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class OrdersDatabaseDataStore(
    private val ordersTable: OrdersTable
) : OrdersDataStore {


    override suspend fun save(order: Order) {
        executeBackgroundOperation {
            ordersTable.save(order)
        }
    }


    override suspend fun save(orders: List<Order>) {
        executeBackgroundOperation {
            ordersTable.save(orders)
        }
    }


    override suspend fun create(params: OrderCreationParameters): Result<Order> {
        throw UnsupportedOperationException()
    }


    override suspend fun cancel(order: Order): Result<OrdersCancellationResponse> {
        throw UnsupportedOperationException()
    }


    override suspend fun delete(order: Order) {
        executeBackgroundOperation {
            ordersTable.delete(order)
        }
    }


    override suspend fun delete(status: OrderStatus) {
        executeBackgroundOperation {
            ordersTable.delete(status)
        }
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            ordersTable.deleteAll()
        }
    }


    override suspend fun search(params: OrderParameters, currencyPairIds: List<Int>): Result<List<Order>> {
        return performBackgroundOperation {
            ordersTable.search(params, currencyPairIds)
        }
    }


    override suspend fun getOrder(id: Long): Result<Order> {
        return performBackgroundOperation {
            ordersTable.getOrder(id)
        }
    }


    override suspend fun getActiveOrders(params: OrderParameters): Result<List<Order>> {
        return getOrders(params)
    }


    override suspend fun getHistoryOrders(params: OrderParameters): Result<List<Order>> {
        return getOrders(params)
    }


    private suspend fun getOrders(params: OrderParameters): Result<List<Order>> {
        return performBackgroundOperation {
            ordersTable.getOrders(params)
        }
    }


}