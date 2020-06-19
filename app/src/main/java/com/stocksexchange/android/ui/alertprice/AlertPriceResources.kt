package com.stocksexchange.android.ui.alertprice

import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.model.Settings
import com.stocksexchange.core.formatters.NumberFormatter

class AlertPriceResources(
    val settings: Settings,
    val numberFormatter: NumberFormatter
) : ItemResources {


    companion object {

        fun newInstance(settings: Settings, numberFormatter: NumberFormatter): AlertPriceResources {
            return AlertPriceResources(
                settings,
                numberFormatter
            )
        }

    }


}