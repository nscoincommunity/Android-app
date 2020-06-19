package com.stocksexchange.android.utils.listeners

import com.stocksexchange.android.model.Settings

interface OnSettingsChangeListener {

    fun onSettingsChanged(newSettings: Settings)

}


fun Any.handleSettingsChangeEvent(newSettings: Settings) {
    if(this is OnSettingsChangeListener) {
        this.onSettingsChanged(newSettings)
    }
}