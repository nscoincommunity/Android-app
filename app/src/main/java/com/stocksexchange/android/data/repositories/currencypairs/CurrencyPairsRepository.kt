package com.stocksexchange.android.data.repositories.currencypairs

import com.stocksexchange.api.model.rest.CurrencyPair
import com.stocksexchange.android.data.base.CurrencyPairsData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface CurrencyPairsRepository : CurrencyPairsData<
    RepositoryResult<CurrencyPair>,
    RepositoryResult<List<CurrencyPair>>
>, Repository {

    suspend fun refresh()

}