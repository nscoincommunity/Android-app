package com.stocksexchange.android.data.stores.alertprice

import com.stocksexchange.android.data.base.AlertPriceData
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.core.model.Result

interface AlertPriceDataStore : AlertPriceData<
    Result<List<AlertPrice>>,
    Result<AlertPriceDeleteResponse>,
    Result<AlertPrice>
>