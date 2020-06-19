package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.android.database.model.DatabaseTrade.Companion.TABLE_NAME

/**
 * A Room database model for the [Trade] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseTrade(
    @PrimaryKey @ColumnInfo(name = ID) var id: Long,
    @ColumnInfo(name = CURRENCY_PAIR_ID) var currencyPairId: Int,
    @ColumnInfo(name = PRICE) var price: Double,
    @ColumnInfo(name = AMOUNT) var amount: Double,
    @ColumnInfo(name = TYPE_STR) var typeStr: String,
    @ColumnInfo(name = TIMESTAMP) var timestamp: Long
) {

    companion object {

        const val TABLE_NAME = "trades"

        const val ID = "id"
        const val CURRENCY_PAIR_ID = "currency_pair_id"
        const val PRICE = "price"
        const val AMOUNT = "amount"
        const val TYPE_STR = "type_str"
        const val TIMESTAMP = "timestamp"

    }


    constructor(): this(
        id = -1L,
        currencyPairId = -1,
        price = -1.0,
        amount = -1.0,
        typeStr = "",
        timestamp = -1L
    )

}