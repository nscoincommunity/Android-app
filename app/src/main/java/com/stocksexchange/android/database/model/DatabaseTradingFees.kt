package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stocksexchange.api.model.rest.TradingFees
import com.stocksexchange.android.database.model.DatabaseTradingFees.Companion.TABLE_NAME

/**
 * A Room database model for the [TradingFees] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseTradingFees(
    @PrimaryKey @ColumnInfo(name = CURRENCY_PAIR_ID) var currencyPairId: Int,
    @ColumnInfo(name = BUY_FEE) var buyFee: Double,
    @ColumnInfo(name = SELL_FEE) var sellFee: Double
) {

    companion object {

        const val TABLE_NAME = "trading_fees"

        const val CURRENCY_PAIR_ID = "currency_pair_id"
        const val BUY_FEE = "buy_fee"
        const val SELL_FEE = "sell_fee"

    }


    constructor(): this(
        currencyPairId = -1,
        buyFee = -1.0,
        sellFee = -1.0
    )

}