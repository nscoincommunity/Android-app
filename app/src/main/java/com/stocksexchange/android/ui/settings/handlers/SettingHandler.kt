package com.stocksexchange.android.ui.settings.handlers

import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.ui.settings.handlers.model.SettingEvent

interface SettingHandler {

    fun onHandleEvent(event: SettingEvent)

    fun candleHandleEvent(event: SettingEvent): Boolean

    fun getSettingId(): SettingId

}