package com.stocksexchange.android.data.stores.tradingfees

import com.stocksexchange.api.model.rest.TradingFees
import com.stocksexchange.android.data.base.TradingFeesData
import com.stocksexchange.core.model.Result

interface TradingFeesDataStore : TradingFeesData<
    Result<TradingFees>
>