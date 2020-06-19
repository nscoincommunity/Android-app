package com.stocksexchange.android.ui.wallets.fragment

import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.R
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.formatters.NumberFormatter

class WalletsResources(
    val stringProvider: StringProvider,
    val numberFormatter: NumberFormatter,
    val settings: Settings,
    val strings: List<String>
) : ItemResources {


    companion object {

        const val STRING_COIN_STATUS = 0
        const val STRING_AVAILABLE_BALANCE = 1
        const val STRING_BALANCE_IN_ORDERS = 2
        const val STRING_BONUS_BALANCE = 3
        const val STRING_TOTAL_BALANCE = 4
        const val STRING_ACTION_DEPOSIT = 5
        const val STRING_ACTION_WITHDRAW = 6


        fun newInstance(stringProvider: StringProvider,
                        numberFormatter: NumberFormatter,
                        settings: Settings): WalletsResources {
            val strings = stringProvider.getStringList(
                R.string.coin_status,
                R.string.available_balance,
                R.string.balance_in_orders,
                R.string.bonus_balance,
                R.string.total_balance,
                R.string.action_deposit,
                R.string.action_withdraw
            )

            return WalletsResources(
                stringProvider = stringProvider,
                numberFormatter = numberFormatter,
                settings = settings,
                strings = strings
            )
        }

    }


}