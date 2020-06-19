package com.stocksexchange.android.data.stores.profileinfos

import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.android.data.base.ProfileInfosData
import com.stocksexchange.core.model.Result

interface ProfileInfosDataStore : ProfileInfosData<
    Result<ProfileInfo>
>