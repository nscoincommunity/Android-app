package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stocksexchange.api.model.rest.Deposit
import com.stocksexchange.android.database.model.DatabaseDeposit.Companion.TABLE_NAME

/**
 * A Room database model for the [Deposit] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseDeposit(
    @PrimaryKey @ColumnInfo(name = ID) var id: Long,
    @ColumnInfo(name = CURRENCY_ID) var currencyId: Int,
    @ColumnInfo(name = CURRENCY_SYMBOL) var currencySymbol: String,
    @ColumnInfo(name = AMOUNT) var amount: Double,
    @ColumnInfo(name = FEE) var fee: Double,
    @ColumnInfo(name = FEE_CURRENCY_ID) var feeCurrencyId: Int,
    @ColumnInfo(name = FEE_CURRENCY_SYMBOL) var feeCurrencySymbol: String,
    @ColumnInfo(name = STATUS_ID) var statusId: Int,
    @ColumnInfo(name = STATUS) var status: String,
    @ColumnInfo(name = STATUS_COLOR) var statusColor: String,
    @ColumnInfo(name = PROTOCOL_ID) var protocolId: Int,
    @ColumnInfo(name = TIMESTAMP) var timestamp: Long,
    @ColumnInfo(name = TRANSACTION_EXPLORER_ID) var transactionExplorerId: String?,
    @ColumnInfo(name = CONFIRMATIONS_STR) var confirmationsStr: String
) {

    companion object {

        const val TABLE_NAME = "deposits"

        const val ID = "id"
        const val CURRENCY_ID = "currency_id"
        const val CURRENCY_SYMBOL = "currency_symbol"
        const val AMOUNT = "amount"
        const val FEE = "fee"
        const val FEE_CURRENCY_ID = "fee_currency_id"
        const val FEE_CURRENCY_SYMBOL = "fee_currency_symbol"
        const val STATUS_ID = "status_id"
        const val STATUS = "status"
        const val STATUS_COLOR = "status_color"
        const val PROTOCOL_ID = "protocol_id"
        const val TIMESTAMP = "timestamp"
        const val TRANSACTION_EXPLORER_ID = "transaction_explorer_id"
        const val CONFIRMATIONS_STR = "confirmations_str"

    }


    constructor(): this(
        id = -1L,
        currencyId = -1,
        currencySymbol = "",
        amount = -1.0,
        fee = -1.0,
        feeCurrencyId = -1,
        feeCurrencySymbol = "",
        statusId = -1,
        status = "",
        statusColor = "",
        protocolId = -1,
        timestamp = -1L,
        transactionExplorerId = null,
        confirmationsStr = ""
    )

}