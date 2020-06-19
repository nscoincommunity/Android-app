package com.stocksexchange.android.data.repositories.deposits

import com.stocksexchange.api.model.rest.Deposit
import com.stocksexchange.android.data.base.DepositsData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface DepositsRepository : DepositsData<
    RepositoryResult<List<Deposit>>
>, Repository {

    suspend fun refresh()

}