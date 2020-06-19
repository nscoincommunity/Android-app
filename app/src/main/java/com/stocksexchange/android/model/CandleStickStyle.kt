package com.stocksexchange.android.model

import androidx.annotation.StringRes
import com.stocksexchange.android.R

enum class CandleStickStyle(
    @StringRes val titleId: Int
) {

    SOLID(
        titleId = R.string.candle_stick_style_solid
    ),
    HOLLOW(
        titleId = R.string.candle_stick_style_hollow
    )

}