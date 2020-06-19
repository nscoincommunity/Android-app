package com.stocksexchange.android.model

import com.stocksexchange.android.R

enum class CoinStatus(
    val stringId: Int,
    val colorId: Int
) {

    ENABLED(
        stringId = R.string.coin_status_enabled,
        colorId = R.color.colorGreenAccent
    ),
    DISABLED(
        stringId = R.string.coin_status_disabled,
        colorId = R.color.colorBlueAccent
    ),
    DELISTED(
        stringId = R.string.coin_status_delisted,
        colorId = R.color.colorRedAccent
    )

}