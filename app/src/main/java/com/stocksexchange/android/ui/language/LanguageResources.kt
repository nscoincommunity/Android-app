package com.stocksexchange.android.ui.language

import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.providers.StringProvider

class LanguageResources(
    val settings: Settings,
    val stringProvider: StringProvider
) : ItemResources {


    companion object {

        fun newInstance(
            settings: Settings,
            stringProvider: StringProvider
        ): LanguageResources {
            return LanguageResources(
                settings = settings,
                stringProvider = stringProvider
            )
        }

    }


}