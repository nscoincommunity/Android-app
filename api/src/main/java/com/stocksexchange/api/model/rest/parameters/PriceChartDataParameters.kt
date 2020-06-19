package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import com.stocksexchange.api.model.rest.PriceChartDataInterval
import com.stocksexchange.core.utils.interfaces.HasUniqueKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PriceChartDataParameters(
    val currencyPairId: Int,
    val interval: PriceChartDataInterval,
    val startTimestamp: Long,   // in seconds
    val endTimestamp: Long,     // in seconds
    val count: Int
) : Parcelable, HasUniqueKey {


    companion object {


        private const val DEFAULT_COUNT = 60

        private const val TWENTY_FOUR_HOURS_TIME_IN_MILLIS = (24L * 60L * 60L * 1000L)


        fun calculateStartTimestamp(interval: PriceChartDataInterval): Long {
            return ((System.currentTimeMillis() - interval.timeDeduction) / 1000L)
        }


        fun calculateEndTimestamp(): Long {
            // Adding 24 hours into to the future as compensation for lost time
            // between calling this method and the actual request made
            return ((System.currentTimeMillis() + TWENTY_FOUR_HOURS_TIME_IN_MILLIS) / 1000L)
        }


        fun getDefaultParameters(
            currencyPairId: Int = -1,
            interval: PriceChartDataInterval = PriceChartDataInterval.ONE_DAY
        ): PriceChartDataParameters {
            val startTimestamp = calculateStartTimestamp(interval)
            val endTimestamp = calculateEndTimestamp()

            return PriceChartDataParameters(
                currencyPairId = currencyPairId,
                interval = interval,
                startTimestamp = startTimestamp,
                endTimestamp = endTimestamp,
                count = DEFAULT_COUNT
            )
        }


    }


    override val uniqueKey: String
        get() = "${currencyPairId}_$interval"


}