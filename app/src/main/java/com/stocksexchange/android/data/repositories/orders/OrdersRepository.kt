package com.stocksexchange.android.data.repositories.orders

import com.stocksexchange.api.model.rest.Order
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import com.stocksexchange.api.model.rest.OrdersCancellationResponse
import com.stocksexchange.android.data.base.OrdersData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface OrdersRepository : OrdersData<
    RepositoryResult<Order>,
    RepositoryResult<OrdersCancellationResponse>,
    RepositoryResult<Order>,
    RepositoryResult<List<Order>>
>, Repository {

    suspend fun refresh(params: OrderParameters)

    suspend fun search(params: OrderParameters): RepositoryResult<List<Order>>

}