package com.stocksexchange.android.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.stocksexchange.android.R

enum class DashboardBottomMenuItem(
    val id: Int,
    @StringRes val stringId: Int,
    @DrawableRes val iconId: Int,
    val requiresUser: Boolean
) {

    BALANCE(
        id = 1,
        stringId = R.string.balance,
        iconId = R.drawable.ic_bottom_nav_balance,
        requiresUser = true
    ),
    ORDERS(
        id = 2,
        stringId = R.string.orders,
        iconId = R.drawable.ic_bottom_nav_orders,
        requiresUser = true
    ),
    TRADE(
        id = 3,
        stringId = R.string.trade,
        iconId = R.drawable.ic_bottom_nav_trade,
        requiresUser = false
    ),
    NEWS(
        id = 4,
        stringId = R.string.news,
        iconId = R.drawable.ic_bottom_nav_news,
        requiresUser = false
    ),
    PROFILE(
        id = 5,
        stringId = R.string.profile,
        iconId = R.drawable.ic_bottom_nav_profile,
        requiresUser = false
    )

}