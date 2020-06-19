package com.stocksexchange.android.data.stores.profileinfos

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class ProfileInfosServerDataStore(
    private val stexRestApi: StexRestApi
) : ProfileInfosDataStore {


    override suspend fun save(profileInfo: ProfileInfo) {
        throw UnsupportedOperationException()
    }


    override suspend fun deleteAll() {
        throw UnsupportedOperationException()
    }


    override suspend fun get(email: String): Result<ProfileInfo> {
        return performBackgroundOperation {
            stexRestApi.getProfileInfo()
        }
    }


}