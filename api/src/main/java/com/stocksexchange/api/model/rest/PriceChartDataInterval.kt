package com.stocksexchange.api.model.rest

import com.stocksexchange.api.R

enum class PriceChartDataInterval(
    val intervalName: String,
    val timeDeduction: Long,
    val stringId: Int
) {


    ONE_DAY(
        intervalName = "1D",
        timeDeduction = 60L * 24L * 60L * 60L * 1000L,  // 60 days
        stringId = R.string.chart_interval_one_day
    ),
    TWELVE_HOURS(
        intervalName = "720",
        timeDeduction = 30L * 24L * 60L * 60L * 1000L,  // 720 hours (30 days)
        stringId = R.string.chart_interval_twelve_hours
    ),
    FOUR_HOURS(
        intervalName = "240",
        timeDeduction = 10L * 24L * 60L * 60L * 1000L,   // 240 hours (10 days)
        stringId = R.string.chart_interval_four_hours
    ),
    SIXTY_MINUTES(
        intervalName = "60",
        timeDeduction = 60L * 60L * 60L * 1000L,   // 60 hours
        stringId = R.string.chart_interval_sixty_minutes
    ),
    THIRTY_MINUTES(
        intervalName = "30",
        timeDeduction = 30L * 60L * 60L * 1000L,    // 30 hours
        stringId = R.string.chart_interval_thirty_minutes
    ),
    FIVE_MINUTES(
        intervalName = "5",
        timeDeduction = 5L * 60L * 60L * 1000L,     // 5 hours
        stringId = R.string.chart_interval_five_minutes
    ),
    ONE_MINUTE(
        intervalName = "1",
        timeDeduction = 1L * 60L * 60L * 1000L,     // 1 hour
        stringId = R.string.chart_interval_one_minute
    );


    companion object {


        fun newInstance(intervalName: String): PriceChartDataInterval {
            return when(intervalName) {
                ONE_DAY.intervalName -> ONE_DAY
                TWELVE_HOURS.intervalName -> TWELVE_HOURS
                FOUR_HOURS.intervalName -> FOUR_HOURS
                SIXTY_MINUTES.intervalName -> SIXTY_MINUTES
                THIRTY_MINUTES.intervalName -> THIRTY_MINUTES
                FIVE_MINUTES.intervalName -> FIVE_MINUTES
                ONE_MINUTE.intervalName -> ONE_MINUTE

                else -> throw IllegalStateException()
            }
        }


    }


}