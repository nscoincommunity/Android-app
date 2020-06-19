package com.stocksexchange.android.database.tables

import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.android.database.daos.ProfileInfoDao
import com.stocksexchange.android.database.model.DatabaseProfileInfo
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.android.mappings.mapToDatabaseProfileInfo
import com.stocksexchange.android.mappings.mapToProfileInfo
import com.stocksexchange.core.model.Result

class ProfileInfosTable(
    private val profileInfoDao: ProfileInfoDao
) : BaseTable() {


    @Synchronized
    fun save(profileInfo: ProfileInfo) {
        profileInfoDao.insert(profileInfo.mapToDatabaseProfileInfo())
    }


    @Synchronized
    fun deleteAll() {
        profileInfoDao.deleteAll()
    }


    @Synchronized
    fun get(email: String): Result<ProfileInfo> {
        return profileInfoDao.get(email).toResult(
            DatabaseProfileInfo::mapToProfileInfo
        )
    }


}