package com.stocksexchange.android.data.stores.currencypairgroups

import com.stocksexchange.api.model.rest.CurrencyPairGroup
import com.stocksexchange.android.data.base.CurrencyPairGroupsData
import com.stocksexchange.core.model.Result

interface CurrencyPairGroupsDataStore : CurrencyPairGroupsData<
    Result<List<CurrencyPairGroup>>
>