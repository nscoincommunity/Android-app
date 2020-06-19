package com.stocksexchange.android.data.repositories.orderbooks

import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters
import com.stocksexchange.android.data.base.OrderbooksData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface OrderbooksRepository : OrderbooksData<
    RepositoryResult<Orderbook>
>, Repository {

    suspend fun refresh(params: OrderbookParameters)

}