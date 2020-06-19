package com.stocksexchange.android.model

import androidx.annotation.StringRes
import com.stocksexchange.android.R

enum class DecimalSeparator(
    val separator: Char,
    @StringRes val titleId: Int
) {

    PERIOD(
        separator = '.',
        titleId = R.string.decimal_separator_period
    ),
    COMMA(
        separator = ',',
        titleId = R.string.decimal_separator_comma
    );

}