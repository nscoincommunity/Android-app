package com.stocksexchange.android.data.repositories.utilities

import com.stocksexchange.api.model.rest.PingResponse
import com.stocksexchange.android.data.base.UtilitiesData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface UtilitiesRepository : UtilitiesData<RepositoryResult<PingResponse>>, Repository