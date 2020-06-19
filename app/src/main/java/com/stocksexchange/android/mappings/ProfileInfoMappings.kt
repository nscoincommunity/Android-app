package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.android.database.model.DatabaseProfileInfo

fun ProfileInfo.mapToDatabaseProfileInfo(): DatabaseProfileInfo {
    return DatabaseProfileInfo(
        id = id,
        email = email,
        userName = userName,
        areWithdrawalsAllowed = areWithdrawalsAllowed,
        verifications = verifications,
        tradingFeeLevels = tradingFeeLevels,
        referralProgram = referralProgram,
        settings = settings
    )
}


fun DatabaseProfileInfo.mapToProfileInfo(): ProfileInfo {
    return ProfileInfo(
        id = id,
        email = email,
        userName = userName,
        areWithdrawalsAllowed = areWithdrawalsAllowed,
        verifications = verifications,
        tradingFeeLevels = tradingFeeLevels,
        referralProgram = referralProgram,
        settings = settings
    )
}