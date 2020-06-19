package com.stocksexchange.android.data.stores.currencies

import com.stocksexchange.api.model.rest.Currency
import com.stocksexchange.android.data.base.CurrenciesData
import com.stocksexchange.core.model.Result

interface CurrenciesDataStore : CurrenciesData<
    Result<Currency>,
    Result<List<Currency>>
>