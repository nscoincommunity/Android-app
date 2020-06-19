package com.stocksexchange.android.ui.transactions.fragment

import android.content.Context
import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.R
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.formatters.TimeFormatter
import com.stocksexchange.core.utils.extensions.getCompatColor

class TransactionResources(
    val strings: List<String>,
    val colors: List<Int>,
    val stringProvider: StringProvider,
    val timeFormatter: TimeFormatter,
    val numberFormatter: NumberFormatter,
    val theme: Theme
) : ItemResources {


    companion object {

        const val STRING_AMOUNT = 0
        const val STRING_FEE = 1
        const val STRING_CONFIRMATIONS = 2
        const val STRING_ADDRESS = 3
        const val STRING_TRANSACTION_ID = 4
        const val STRING_DATE = 5
        const val STRING_PREPOSITION_OF = 6
        const val STRING_INTERNAL_TRANSFER = 7
        const val STRING_RESEND_CONFIRMATION_EMAIL = 8
        const val STRING_CANCEL = 9

        const val COLOR_BLUE_ACCENT = 0


        fun newInstance(context: Context, stringProvider: StringProvider,
                        timeFormatter: TimeFormatter, numberFormatter: NumberFormatter,
                        settings: Settings): TransactionResources {
            val strings = stringProvider.getStringList(
                R.string.amount,
                R.string.fee,
                R.string.confirmations,
                R.string.address,
                R.string.transaction_id,
                R.string.date,
                R.string.preposition_of,
                R.string.internal_transfer,
                R.string.action_resend_confirmation_email,
                R.string.action_cancel
            )
            val colors = listOf(
                context.getCompatColor(R.color.colorBlueAccent)
            )

            return TransactionResources(
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