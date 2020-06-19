package com.stocksexchange.android.data.stores.currencypairs

import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.android.data.base.CurrencyPairsData
import com.stocksexchange.core.model.Result

interface CurrencyPairsDataStore : CurrencyPairsData<
    Result<CurrencyPair>,
    Result<List<CurrencyPair>>
>