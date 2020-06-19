package com.stocksexchange.android.model.comparators

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.SortOrder
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyMarketComparator(
    val id: Int,
    val column: Column,
    val order: SortOrder
) : ParcelableComparator<CurrencyMarket> {


    companion object {

        val NAME_ASCENDING_COMPARATOR = CurrencyMarketComparator(1, Column.NAME, SortOrder.ASC)
        val NAME_DESCENDING_COMPARATOR = CurrencyMarketComparator(2, Column.NAME, SortOrder.DESC)
        val VOLUME_ASCENDING_COMPARATOR = CurrencyMarketComparator(3, Column.VOLUME, SortOrder.ASC)
        val VOLUME_DESCENDING_COMPARATOR = CurrencyMarketComparator(4, Column.VOLUME, SortOrder.DESC)
        val LAST_PRICE_ASCENDING_COMPARATOR = CurrencyMarketComparator(5, Column.LAST_PRICE, SortOrder.ASC)
        val LAST_PRICE_DESCENDING_COMPARATOR = CurrencyMarketComparator(6, Column.LAST_PRICE, SortOrder.DESC)
        val DAILY_PRICE_CHANGE_ASCENDING_COMPARATOR = CurrencyMarketComparator(7, Column.DAILY_PRICE_CHANGE, SortOrder.ASC)
        val DAILY_PRICE_CHANGE_DESCENDING_COMPARATOR = CurrencyMarketComparator(8, Column.DAILY_PRICE_CHANGE, SortOrder.DESC)

        val DEFAULT = VOLUME_DESCENDING_COMPARATOR

    }


    enum class Column {

        NAME,
        VOLUME,
        LAST_PRICE,
        DAILY_PRICE_CHANGE

    }




    operator fun not(): CurrencyMarketComparator {
        return when(order) {

            SortOrder.ASC -> when(column) {
                Column.NAME -> NAME_DESCENDING_COMPARATOR
                Column.VOLUME -> VOLUME_DESCENDING_COMPARATOR
                Column.LAST_PRICE -> LAST_PRICE_DESCENDING_COMPARATOR
                Column.DAILY_PRICE_CHANGE -> DAILY_PRICE_CHANGE_DESCENDING_COMPARATOR
            }

            SortOrder.DESC -> when(column) {
                Column.NAME -> NAME_ASCENDING_COMPARATOR
                Column.VOLUME -> VOLUME_ASCENDING_COMPARATOR
                Column.LAST_PRICE -> LAST_PRICE_ASCENDING_COMPARATOR
                Column.DAILY_PRICE_CHANGE -> DAILY_PRICE_CHANGE_ASCENDING_COMPARATOR
            }

        }
    }


    override fun compare(o1: CurrencyMarket, o2: CurrencyMarket): Int {
        return when(order) {

            SortOrder.ASC -> when(column) {
                Column.NAME -> o1.pairName.compareTo(o2.pairName)
                Column.VOLUME -> o1.dailyVolumeInQuoteCurrency.compareTo(o2.dailyVolumeInQuoteCurrency)
                Column.LAST_PRICE -> o1.lastPrice.compareTo(o2.lastPrice)
                Column.DAILY_PRICE_CHANGE -> o1.dailyPriceChangeInPercentage.compareTo(o2.dailyPriceChangeInPercentage)
            }

            SortOrder.DESC -> when(column) {
                Column.NAME -> o2.pairName.compareTo(o1.pairName)
                Column.VOLUME -> o2.dailyVolumeInQuoteCurrency.compareTo(o1.dailyVolumeInQuoteCurrency)
                Column.LAST_PRICE -> o2.lastPrice.compareTo(o1.lastPrice)
                Column.DAILY_PRICE_CHANGE -> o2.dailyPriceChangeInPercentage.compareTo(o1.dailyPriceChangeInPercentage)
            }

        }
    }


}