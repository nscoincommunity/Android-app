package com.stocksexchange.android.data.repositories.currencies

import com.stocksexchange.api.model.rest.Currency
import com.stocksexchange.android.data.base.CurrenciesData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface CurrenciesRepository : CurrenciesData<
    RepositoryResult<Currency>,
    RepositoryResult<List<Currency>>
>, Repository {

    suspend fun refresh()

}