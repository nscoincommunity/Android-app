package com.stocksexchange.android.ui.orders.fragment

import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.OrderLifecycleType
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.api.model.rest.OrderSelectivityType
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.formatters.TimeFormatter

class OrderResources(
    val strings: List<String>,
    val numberFormatter: NumberFormatter,
    val timeFormatter: TimeFormatter,
    val settings: Settings
) : ItemResources {


    companion object {

        const val STRING_ID = 0
        const val STRING_AMOUNT = 1
        const val STRING_INITIAL_AMOUNT = 2
        const val STRING_FILLED_AMOUNT = 3
        const val STRING_STOP_PRICE = 4
        const val STRING_PRICE = 5
        const val STRING_DATE = 6
        const val STRING_ACTION_CANCEL = 7
        const val STRING_RESULT_EXPENSE = 8
        const val STRING_RESULT_INCOME = 9
        const val STRING_RESULT_SPENT = 10
        const val STRING_RESULT_RECEIVED = 11
        const val STRING_ERROR = 12


        fun newInstance(stringProvider: StringProvider, numberFormatter: NumberFormatter,
                        timeFormatter: TimeFormatter, settings: Settings): OrderResources {
            val strings = stringProvider.getStringList(
                R.string.id,
                R.string.amount,
                R.string.initial_amount,
                R.string.filled_amount,
                R.string.stop_price,
                R.string.price,
                R.string.date,
                R.string.action_cancel,
                R.string.expense,
                R.string.income,
                R.string.action_spent,
                R.string.action_received,
                R.string.error
            )

            return OrderResources(
                strings,
                numberFormatter,
                timeFormatter,
                settings
            )
        }

    }


    var orderLifecycleType: OrderLifecycleType = OrderLifecycleType.ACTIVE

    var orderSelectivityType: OrderSelectivityType = OrderSelectivityType.ANY_PAIR_ID


}