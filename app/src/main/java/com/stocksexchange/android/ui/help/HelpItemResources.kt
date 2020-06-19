package com.stocksexchange.android.ui.help

import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.providers.StringProvider

data class HelpItemResources(
    val settings: Settings,
    val stringProvider: StringProvider
) : ItemResources {


    companion object {

        fun newInstance(settings: Settings,
                        stringProvider: StringProvider): HelpItemResources {
            return HelpItemResources(
                settings = settings,
                stringProvider = stringProvider
            )
        }

    }


}