package com.stocksexchange.android.data.repositories.favoritecurrencypairs

import com.stocksexchange.android.data.base.FavoriteCurrencyPairsData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface FavoriteCurrencyPairsRepository : FavoriteCurrencyPairsData<
    RepositoryResult<Int>,
    RepositoryResult<List<Int>>
>, Repository