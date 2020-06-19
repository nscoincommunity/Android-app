package com.stocksexchange.android.data.repositories.profileinfos

import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.android.data.base.ProfileInfosData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface ProfileInfosRepository : ProfileInfosData<
    RepositoryResult<ProfileInfo>
>, Repository {

    suspend fun refresh()

}