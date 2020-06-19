package com.stocksexchange.android.data.stores.tradehistory

import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.android.data.base.TradeHistoryData
import com.stocksexchange.core.model.Result

interface TradeHistoryDataStore : TradeHistoryData<
    Result<List<Trade>>
>