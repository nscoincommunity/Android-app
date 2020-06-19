package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.stocksexchange.api.model.rest.CandleStick
import com.stocksexchange.android.database.model.DatabaseCandleStick.Companion.CURRENCY_PAIR_ID
import com.stocksexchange.android.database.model.DatabaseCandleStick.Companion.INTERVAL_NAME
import com.stocksexchange.android.database.model.DatabaseCandleStick.Companion.TABLE_NAME
import com.stocksexchange.android.database.model.DatabaseCandleStick.Companion.TIMESTAMP

/**
 * A Room database model for the [CandleStick] class.
 */
@Entity(tableName = TABLE_NAME, primaryKeys = [CURRENCY_PAIR_ID, INTERVAL_NAME, TIMESTAMP])
data class DatabaseCandleStick(
    @ColumnInfo(name = CURRENCY_PAIR_ID) var currencyPairId: Int,
    @ColumnInfo(name = INTERVAL_NAME) var intervalName: String,
    @ColumnInfo(name = OPEN_PRICE) var openPrice: Double,
    @ColumnInfo(name = HIGH_PRICE) var highPrice: Double,
    @ColumnInfo(name = LOW_PRICE) var lowPrice: Double,
    @ColumnInfo(name = CLOSE_PRICE) var closePrice: Double,
    @ColumnInfo(name = VOLUME) var volume: Double,
    @ColumnInfo(name = TIMESTAMP) var timestamp: Long
) {

    companion object {

        const val TABLE_NAME = "candle_sticks"

        const val CURRENCY_PAIR_ID = "currency_pair_id"
        const val INTERVAL_NAME = "interval_name"
        const val OPEN_PRICE = "open_price"
        const val HIGH_PRICE = "high_price"
        const val LOW_PRICE = "low_price"
        const val CLOSE_PRICE = "close_price"
        const val VOLUME = "volume"
        const val TIMESTAMP = "timestamp"

    }


    constructor(): this(
        currencyPairId = -1,
        intervalName = "",
        openPrice = -1.0,
        highPrice = -1.0,
        lowPrice = -1.0,
        closePrice = -1.0,
        volume = -1.0,
        timestamp = -1L
    )

}