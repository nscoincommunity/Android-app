package com.stocksexchange.android.model

import java.io.Serializable

data class SettingAccount(
    val settingId: SettingId,
    var userEmail: String = "",
    var userName: String = ""
) : Serializable