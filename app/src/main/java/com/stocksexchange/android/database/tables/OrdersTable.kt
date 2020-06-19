package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.Order
import com.stocksexchange.api.model.rest.OrderStatus
import com.stocksexchange.android.database.daos.OrderDao
import com.stocksexchange.android.database.model.DatabaseOrder
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.database.utils.QueryBuilder
import com.stocksexchange.android.mappings.mapToDatabaseOrder
import com.stocksexchange.android.mappings.mapToDatabaseOrderList
import com.stocksexchange.android.mappings.mapToOrder
import com.stocksexchange.android.mappings.mapToOrderList
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import com.stocksexchange.core.model.Result

class OrdersTable(
    private val orderDao: OrderDao
) : BaseTable() {


    @Synchronized
    fun save(order: Order) {
        orderDao.insert(order.mapToDatabaseOrder())
    }


    @Synchronized
    fun save(orders: List<Order>) {
        orderDao.insert(orders.mapToDatabaseOrderList())
    }


    @Synchronized
    fun delete(order: Order) {
        orderDao.delete(order.mapToDatabaseOrder())
    }


    @Synchronized
    fun delete(status: OrderStatus) {
        orderDao.delete(QueryBuilder.Orders.getDeleteQuery(status))
    }


    @Synchronized
    fun deleteAll() {
        orderDao.deleteAll()
    }


    @Synchronized
    fun search(params: OrderParameters, currencyPairIds: List<Int>): Result<List<Order>> {
        val query = QueryBuilder.Orders.getSearchQuery(
            status = params.status,
            currencyPairIds = currencyPairIds,
            sortOrder = params.sortOrder.name,
            limit = params.limit,
            offset = params.offset
        )

        return orderDao.search(query).toResult(
            List<DatabaseOrder>::mapToOrderList
        )
    }


    @Synchronized
    fun getOrder(id: Long): Result<Order> {
        return orderDao.get(id).toResult(DatabaseOrder::mapToOrder)
    }


    @Synchronized
    fun getOrders(params: OrderParameters): Result<List<Order>> {
        val query = QueryBuilder.Orders.getQuery(
            status = params.status,
            currencyPairId = params.currencyPairIdStrOrEmptyStr,
            sortOrder = params.sortOrder.name,
            limit = params.limit,
            offset = params.offset
        )

        return orderDao.get(query).toResult(
            List<DatabaseOrder>::mapToOrderList
        )
    }


}