package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.api.model.rest.OrderbookOrder
import com.stocksexchange.android.database.model.DatabaseOrderbook.Companion.TABLE_NAME

/**
 * A Room database model for the [Orderbook] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseOrderbook(
    @PrimaryKey @ColumnInfo(name = CURRENCY_PAIR_ID) var currencyPairId: Int,
    @ColumnInfo(name = BUY_ORDERS) var buyOrders: List<OrderbookOrder>,
    @ColumnInfo(name = SELL_ORDERS) var sellOrders: List<OrderbookOrder>
) {

    companion object {

        const val TABLE_NAME = "orderbooks"

        const val CURRENCY_PAIR_ID = "currency_pair_id"
        const val BUY_ORDERS = "buy_orders"
        const val SELL_ORDERS = "sell_orders"

    }


    constructor(): this(
        currencyPairId = -1,
        buyOrders = listOf(),
        sellOrders = listOf()
    )

}