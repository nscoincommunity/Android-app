package com.stocksexchange.android.data.stores.utilities

import com.stocksexchange.api.model.rest.PingResponse
import com.stocksexchange.android.data.base.UtilitiesData
import com.stocksexchange.core.model.Result

interface UtilitiesDataStore : UtilitiesData<Result<PingResponse>>