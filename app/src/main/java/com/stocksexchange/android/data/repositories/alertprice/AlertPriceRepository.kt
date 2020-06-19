package com.stocksexchange.android.data.repositories.alertprice

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.data.base.AlertPriceData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.api.model.rest.parameters.AlertPriceParameters

interface AlertPriceRepository : AlertPriceData<
    RepositoryResult<List<AlertPrice>>,
    RepositoryResult<AlertPriceDeleteResponse>,
    RepositoryResult<AlertPrice>
>, Repository {

    suspend fun refresh(params: AlertPriceParameters)

}