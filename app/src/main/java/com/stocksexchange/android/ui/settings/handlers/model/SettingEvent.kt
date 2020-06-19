package com.stocksexchange.android.ui.settings.handlers.model

import com.stocksexchange.android.model.SettingId

data class SettingEvent(
    val id: SettingId,
    val action: SettingAction,
    val payload: Any? = null
)