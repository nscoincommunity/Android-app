package com.stocksexchange.android.data.repositories.profileinfos

import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.android.data.stores.profileinfos.ProfileInfosDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.model.Result
import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.SimpleFreshDataHandler
import com.stocksexchange.core.providers.ConnectionProvider

class ProfileInfosRepositoryImpl(
    private val serverDataStore: ProfileInfosDataStore,
    private val databaseDataStore: ProfileInfosDataStore,
    private val cacheDataStore: ProfileInfosDataStore,
    private val freshDataHandler: SimpleFreshDataHandler,
    private val connectionProvider: ConnectionProvider
) : ProfileInfosRepository {


    @Synchronized
    override suspend fun refresh() {
        freshDataHandler.refresh()
        cacheDataStore.deleteAll()
    }


    @Synchronized
    override suspend fun save(profileInfo: ProfileInfo) {
        databaseDataStore.save(profileInfo)
        cacheDataStore.save(profileInfo)
    }


    @Synchronized
    override suspend fun deleteAll() {
        databaseDataStore.deleteAll()
        cacheDataStore.deleteAll()
    }


    @Synchronized
    override suspend fun clear() {
        deleteAll()

        freshDataHandler.reset()
    }


    @Synchronized
    override suspend fun get(email: String): RepositoryResult<ProfileInfo> {
        cacheDataStore.get(email).also {
            if(it is Result.Success) {
                return@get RepositoryResult(cacheResult = it)
            }
        }

        val result = RepositoryResult<ProfileInfo>()

        if(freshDataHandler.shouldLoadFreshData(connectionProvider)) {
            result.serverResult = serverDataStore.get(email)

            if(result.isServerResultSuccessful()) {
                save(result.getSuccessfulResultValue())
            }
        }

        if(result.isServerResultErroneous(true)) {
            result.databaseResult = databaseDataStore.get(email)

            if(result.isDatabaseResultSuccessful()) {
                cacheDataStore.save(result.getSuccessfulResultValue())
            }
        }

        return result
    }


}