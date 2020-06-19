package com.stocksexchange.android.data.repositories.settings

import com.stocksexchange.android.data.stores.settings.SettingsDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.model.Result
import com.stocksexchange.android.model.Settings

class SettingsRepositoryImpl(
    private val databaseDataStore: SettingsDataStore,
    private val cacheDataStore: SettingsDataStore
) : SettingsRepository {


    @Synchronized
    override suspend fun refresh() {
        cacheDataStore.clear()
    }


    @Synchronized
    override suspend fun save(settings: Settings) {
        databaseDataStore.save(settings)
        cacheDataStore.save(settings)
    }


    @Synchronized
    override suspend fun get(): RepositoryResult<Settings> {
        cacheDataStore.get().also {
            if(it is Result.Success) {
                return@get RepositoryResult(cacheResult = it)
            }
        }

        return RepositoryResult<Settings>().apply {
            databaseResult = databaseDataStore.get()

            if(isDatabaseResultSuccessful()) {
                cacheDataStore.save(getSuccessfulResultValue())
            }
        }
    }


}