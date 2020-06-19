package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stocksexchange.android.database.model.DatabaseAlertPrice.Companion.TABLE_NAME

/**
 * A Room database model for the [AlertPrice] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseAlertPrice(
    @PrimaryKey @ColumnInfo(name = ID) var id: Int,
    @ColumnInfo(name = CURRENCY_PAIR_ID) var currencyPairId: Int,
    @ColumnInfo(name = CURRENCY_PAIR_NAME) var currencyPairName: String,
    @ColumnInfo(name = COMPARISON_TYPE) var comparisonType: String,
    @ColumnInfo(name = PRICE) val price: Double,
    @ColumnInfo(name = ACTIVE) val active: Boolean

) {


    companion object {

        const val TABLE_NAME = "alertprice"
        const val ID = "id"
        const val CURRENCY_PAIR_ID = "currency_pair_id"
        const val CURRENCY_PAIR_NAME = "currency_pair_name"
        const val COMPARISON_TYPE = "comparison_type"
        const val PRICE = "price"
        const val ACTIVE = "active"
    }


    constructor(): this(
        id = -1,
        currencyPairId = -1,
        currencyPairName = "",
        comparisonType = "",
        price = 0.0,
        active = false
    )


}