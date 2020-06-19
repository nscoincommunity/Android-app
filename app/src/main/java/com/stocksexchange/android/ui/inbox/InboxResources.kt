package com.stocksexchange.android.ui.inbox

import android.content.Context
import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.R
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.formatters.TimeFormatter
import com.stocksexchange.core.utils.extensions.dpToPx
import com.stocksexchange.core.utils.extensions.getCompatColor

class InboxResources(
    val dimensions: List<Int>,
    val strings: List<String>,
    val colors: List<Int>,
    val stringProvider: StringProvider,
    val timeFormatter: TimeFormatter,
    val numberFormatter: NumberFormatter,
    val theme: Theme
) : ItemResources {

    //todo: will need add all parameters
    companion object {

        const val DIMENSION_ACTION_BUTTON_HORIZONTAL_MARGIN = 0

        const val STRING_COIN = 0
        const val STRING_COIN_FULL_NAME = 1
        const val STRING_AMOUNT = 2
        const val STRING_ORDER_ID = 3
        const val STRING_CURRENCY_PAIR = 4
        const val STRING_TYPE = 5
        const val STRING_ORDER_AMOUNT = 6
        const val STRING_PRICE = 7
        const val STRING_EXPECTED_AMOUNT = 8
        const val STRING_DESCRIPTION = 9
        const val STRING_DATE = 10


        fun newInstance(context: Context, stringProvider: StringProvider,
                        timeFormatter: TimeFormatter, numberFormatter: NumberFormatter,
                        settings: Settings): InboxResources {
            val dimensions = listOf(
                context.dpToPx(R.dimen.inbox_item_action_button_horizontal_margin)
            )
            val strings = stringProvider.getStringList(
                R.string.inbox_coin,
                R.string.inbox_coin_full_name,
                R.string.inbox_amount,
                R.string.inbox_order_id,
                R.string.inbox_currency_pair,
                R.string.inbox_type,
                R.string.inbox_order_amount,
                R.string.inbox_price,
                R.string.inbox_expected_amount,
                R.string.inbox_description,
                R.string.inbox_date
            )
            val colors = listOf(
                context.getCompatColor(R.color.colorBlueAccent)
            )

            return InboxResources(
                dimensions,
                strings,
                colors,
                stringProvider,
                timeFormatter,
                numberFormatter,
                settings.theme
            )
        }

    }


}