package com.stocksexchange.android.model

import androidx.annotation.StringRes
import com.stocksexchange.android.R

enum class GroupingSeparator(
    val separator: Char,
    @StringRes val titleId: Int
) {

    PERIOD(
        separator = '.',
        titleId = R.string.grouping_separator_period
    ),
    COMMA(
        separator = ',',
        titleId = R.string.grouping_separator_comma
    ),
    SPACE(
        separator = ' ',
        titleId = R.string.grouping_separator_space
    )

}