package com.stocksexchange.android.model

import androidx.annotation.StringRes
import com.stocksexchange.android.R

enum class AuthenticationSessionDuration(
    val timeInMillis: Long,
    @StringRes val titleId: Int
) {

    // For debugging purposes only
    FIVE_SECONDS(
        timeInMillis = 5L * 1000L,
        titleId = R.string.time_period_five_seconds
    ),

    FIVE_MINUTES(
        timeInMillis = 5L * 60L * 1000L,
        titleId = R.string.time_period_five_minutes
    ),
    TEN_MINUTES(
        timeInMillis = 10L * 60L * 1000L,
        titleId = R.string.time_period_ten_minutes
    ),
    FIFTEEN_MINUTES(
        timeInMillis = 15L * 60L * 1000L,
        titleId = R.string.time_period_fifteen_minutes
    ),
    THIRTY_MINUTES(
        timeInMillis = 30L * 60L * 1000L,
        titleId = R.string.time_period_thirty_minutes
    ),
    FORTY_FIVE_MINUTES(
        timeInMillis = 45L * 60L * 1000L,
        titleId = R.string.time_period_forty_five_minutes
    ),
    ONE_HOUR(
        timeInMillis = 1L * 60L * 60L * 1000L,
        titleId = R.string.time_period_one_hour
    ),
    TWO_HOURS(
        timeInMillis = 2L * 60L * 60L * 1000L,
        titleId = R.string.time_period_two_hours
    ),
    THREE_HOURS(
        timeInMillis = 3L * 60L * 60L * 1000L,
        titleId = R.string.time_period_three_hours
    ),
    SIX_HOURS(
        timeInMillis = 6L * 60L * 60L * 1000L,
        titleId = R.string.time_period_six_hours
    ),
    NINE_HOURS(
        timeInMillis = 9L * 60L * 60L * 1000L,
        titleId = R.string.time_period_nine_hours
    ),
    TWELVE_HOURS(
        timeInMillis = 12L * 60L * 60L * 1000L,
        titleId = R.string.time_period_twelve_hours
    )

}