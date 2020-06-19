package com.stocksexchange.android.data.stores.deposits

import com.stocksexchange.api.model.rest.Deposit
import com.stocksexchange.android.data.base.DepositsData
import com.stocksexchange.core.model.Result

interface DepositsDataStore : DepositsData<
    Result<List<Deposit>>
>