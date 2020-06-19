package com.stocksexchange.android.ui.settings

import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.model.Settings

class SettingResources(
    val settings: Settings
) : ItemResources {


    companion object {

        fun newInstance(settings: Settings): SettingResources {
            return SettingResources(settings)
        }

    }


}