package com.stocksexchange.android.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.stocksexchange.android.R

enum class Shortcut(
    val requiresUserPresence: Boolean,
    @StringRes val shortLabelId: Int,
    @StringRes val longLabelId: Int,
    @DrawableRes val iconResId: Int
)  {

    BALANCE(
        requiresUserPresence = true,
        shortLabelId = R.string.shortcut_balance_short_label,
        longLabelId = R.string.shortcut_balance_long_label,
        iconResId = R.mipmap.ic_shortcut_icon_balance
    ),
    ORDERS(
        requiresUserPresence = true,
        shortLabelId = R.string.shortcut_orders_short_label,
        longLabelId = R.string.shortcut_orders_long_label,
        iconResId = R.mipmap.ic_shortcut_icon_orders
    ),
    NEWS(
        requiresUserPresence = false,
        shortLabelId = R.string.shortcut_news_short_label,
        longLabelId = R.string.shortcut_news_long_label,
        iconResId = R.mipmap.ic_shortcut_icon_news
    )

}