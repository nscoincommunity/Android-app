package com.stocksexchange.android.data.base

import com.stocksexchange.android.model.Settings

interface SettingsData<
    SettingsFetchingResult
> {

    suspend fun save(settings: Settings)

    suspend fun get(): SettingsFetchingResult

}