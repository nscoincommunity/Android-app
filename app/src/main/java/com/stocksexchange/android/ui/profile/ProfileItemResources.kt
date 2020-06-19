package com.stocksexchange.android.ui.profile

import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.utils.providers.StringProvider

class ProfileItemResources private constructor(
    val theme: Theme,
    val stringProvider: StringProvider
) : ItemResources {


    companion object {

        fun newInstance(settings: Settings,
                        stringProvider: StringProvider): ProfileItemResources {
            return ProfileItemResources(
                theme = settings.theme,
                stringProvider = stringProvider
            )
        }

    }


}