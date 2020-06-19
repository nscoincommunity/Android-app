package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stocksexchange.android.database.model.DatabaseInbox.Companion.TABLE_NAME

/**
 * A Room database model for the [Inbox] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseInbox(
    @PrimaryKey @ColumnInfo(name = ID) var id: String,
    @ColumnInfo(name = TITLE) var title: String,
    @ColumnInfo(name = DESC) var desc: String,
    @ColumnInfo(name = DATE) var date: String,
    @ColumnInfo(name = CHANNEL) val channel: String?,
    @ColumnInfo(name = READ_AT) var readAt: String?,
    @ColumnInfo(name = COIN) val coin: String?,
    @ColumnInfo(name = COIN_FULL_NAME) val coinFullName: String?,
    @ColumnInfo(name = AMOUNT) val amount: String?,
    @ColumnInfo(name = ORDER_ID) val orderId: String?,
    @ColumnInfo(name = CURRENCY_PAIR) val currencyPair: String?,
    @ColumnInfo(name = TYPE) val type: String?,
    @ColumnInfo(name = ORDER_AMOUNT) val orderAmount: String?,
    @ColumnInfo(name = PRICE) val price: String?,
    @ColumnInfo(name = EXPECTED_AMOUNT) val expectedAmount: String?,
    @ColumnInfo(name = FEE) val fee: String?,
    @ColumnInfo(name = IN_ORDERS) val inOrders: String?,
    @ColumnInfo(name = IP) val ip: String?,
    @ColumnInfo(name = LOCATION) val location: String?,
    @ColumnInfo(name = BROWSER) val browser: String?,
    @ColumnInfo(name = BROWSER_VERSION) val browserVersion: String?,
    @ColumnInfo(name = DEVICE) val device: String?,
    @ColumnInfo(name = PLATFORM) val platform: String?
) {


    companion object {

        const val TABLE_NAME = "inbox"
        const val ID = "id"
        const val TITLE = "title"
        const val DESC = "desc"
        const val DATE = "date"
        const val CHANNEL = "channel"
        const val READ_AT = "readAt"
        const val COIN = "coin"
        const val COIN_FULL_NAME = "coin_full_name"
        const val AMOUNT = "amount"
        const val ORDER_ID = "order_id"
        const val CURRENCY_PAIR = "currency_pair"
        const val TYPE = "type"
        const val ORDER_AMOUNT = "order_amount"
        const val PRICE = "price"
        const val EXPECTED_AMOUNT = "expected_amount"
        const val FEE = "fee"
        const val IN_ORDERS = "in_orders"
        const val IP = "ip"
        const val LOCATION = "location"
        const val BROWSER = "browser"
        const val BROWSER_VERSION = "browser_version"
        const val DEVICE = "device"
        const val PLATFORM = "platform"

    }


    constructor() : this(
        id = "-1",
        title = "",
        desc = "",
        date = "",
        channel = "",
        readAt = null,
        coin = null,
        coinFullName = null,
        amount = null,
        orderId = null,
        currencyPair = null,
        type = null,
        orderAmount = null,
        price = null,
        expectedAmount = null,
        fee = null,
        inOrders = null,
        ip = null,
        location = null,
        browser = null,
        browserVersion = null,
        device = null,
        platform = null
    )


}