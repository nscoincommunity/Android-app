package com.stocksexchange.android.data.stores.settings

import com.stocksexchange.android.database.tables.SettingsTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.android.model.Settings
import com.stocksexchange.core.utils.helpers.performBackgroundOperation
import com.stocksexchange.core.utils.helpers.executeBackgroundOperation

class SettingsDatabaseDataStore(
    private val settingsTable: SettingsTable
) : SettingsDataStore {


    override suspend fun save(settings: Settings) {
        executeBackgroundOperation {
            settingsTable.save(settings)
        }
    }


    override suspend fun get(): Result<Settings> {
        return performBackgroundOperation {
            settingsTable.get()
        }
    }


    override suspend fun clear() {
        throw UnsupportedOperationException()
    }


}