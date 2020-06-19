package com.stocksexchange.android.data.stores.profileinfos

import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.android.database.tables.ProfileInfosTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class ProfileInfosDatabaseDataStore(
    private val profileInfosTable: ProfileInfosTable
) : ProfileInfosDataStore {


    override suspend fun save(profileInfo: ProfileInfo) {
        executeBackgroundOperation {
            profileInfosTable.save(profileInfo)
        }
    }


    override suspend fun deleteAll() {
        executeBackgroundOperation {
            profileInfosTable.deleteAll()
        }
    }


    override suspend fun get(email: String): Result<ProfileInfo> {
        return performBackgroundOperation {
            profileInfosTable.get(email)
        }
    }


}