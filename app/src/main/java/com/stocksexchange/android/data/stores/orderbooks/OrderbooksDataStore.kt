package com.stocksexchange.android.data.stores.orderbooks

import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.android.data.base.OrderbooksData
import com.stocksexchange.core.model.Result

interface OrderbooksDataStore : OrderbooksData<
    Result<Orderbook>
>