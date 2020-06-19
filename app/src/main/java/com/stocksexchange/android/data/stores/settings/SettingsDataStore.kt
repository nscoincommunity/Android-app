package com.stocksexchange.android.data.stores.settings

import com.stocksexchange.android.data.base.SettingsData
import com.stocksexchange.core.model.Result
import com.stocksexchange.android.model.Settings

interface SettingsDataStore : SettingsData<
    Result<Settings>
> {

    suspend fun clear()

}