package com.stocksexchange.android.data.stores.settings

import com.stocksexchange.android.data.stores.base.BaseCacheDataStore
import com.stocksexchange.android.model.Settings
import com.stocksexchange.core.exceptions.NotFoundException
import com.stocksexchange.core.model.Result

class SettingsCacheDataStore : BaseCacheDataStore(), SettingsDataStore {


    companion object {

        private const val KEY_SETTINGS = "settings"

    }




    override suspend fun save(settings: Settings) {
        cache.put(KEY_SETTINGS, settings)
    }


    override suspend fun get(): Result<Settings> {
        return if(cache.contains(KEY_SETTINGS)) {
            Result.Success(cache.get(KEY_SETTINGS) as Settings)
        } else {
            Result.Failure(NotFoundException())
        }
    }


}