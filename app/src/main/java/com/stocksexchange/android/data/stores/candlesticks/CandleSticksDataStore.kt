package com.stocksexchange.android.data.stores.candlesticks

import com.stocksexchange.api.model.rest.CandleStick
import com.stocksexchange.android.data.base.CandleSticksData
import com.stocksexchange.core.model.Result

interface CandleSticksDataStore : CandleSticksData<
    Result<List<CandleStick>>
>