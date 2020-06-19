package com.stocksexchange.android.data.repositories.settings

import com.stocksexchange.android.data.base.SettingsData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.model.Settings

interface SettingsRepository : SettingsData<
    RepositoryResult<Settings>
>, Repository {

    suspend fun refresh()

}