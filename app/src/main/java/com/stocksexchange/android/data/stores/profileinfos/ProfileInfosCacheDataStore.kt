package com.stocksexchange.android.data.stores.profileinfos

import com.stocksexchange.android.data.stores.base.BaseCacheDataStore
import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.core.exceptions.NotFoundException
import com.stocksexchange.core.model.Result

class ProfileInfosCacheDataStore : BaseCacheDataStore(), ProfileInfosDataStore {


    override suspend fun save(profileInfo: ProfileInfo) {
        cache.put(profileInfo.email, profileInfo)
    }


    override suspend fun deleteAll() {
        clear()
    }


    override suspend fun get(email: String): Result<ProfileInfo> {
        return if(cache.contains(email)) {
            Result.Success(cache.get(email) as ProfileInfo)
        } else {
            Result.Failure(NotFoundException())
        }
    }


}