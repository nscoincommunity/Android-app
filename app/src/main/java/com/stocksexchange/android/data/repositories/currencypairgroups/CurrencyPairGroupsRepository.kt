package com.stocksexchange.android.data.repositories.currencypairgroups

import com.stocksexchange.api.model.rest.CurrencyPairGroup
import com.stocksexchange.android.data.base.CurrencyPairGroupsData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface CurrencyPairGroupsRepository : CurrencyPairGroupsData<
    RepositoryResult<List<CurrencyPairGroup>>
>, Repository