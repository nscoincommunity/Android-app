package com.stocksexchange.android.database.tables

import com.stocksexchange.android.database.daos.SettingsDao
import com.stocksexchange.android.database.model.DatabaseSettings
import com.stocksexchange.android.mappings.mapToDatabaseSettings
import com.stocksexchange.android.mappings.mapToSettings
import com.stocksexchange.android.database.tables.base.BaseTable
import com.stocksexchange.core.model.Result
import com.stocksexchange.android.model.Settings

class SettingsTable(
    private val settingsDao: SettingsDao
) : BaseTable() {


    @Synchronized
    fun save(settings: Settings) {
        settingsDao.insert(settings.mapToDatabaseSettings())
    }


    @Synchronized
    fun get(): Result<Settings> {
        return settingsDao.get(Settings.SETTINGS_ID).toResult(
            DatabaseSettings::mapToSettings
        )
    }


}