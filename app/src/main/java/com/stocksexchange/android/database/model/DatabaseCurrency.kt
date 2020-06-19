package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stocksexchange.api.model.rest.Currency
import com.stocksexchange.api.model.rest.Protocol
import com.stocksexchange.android.database.model.DatabaseCurrency.Companion.TABLE_NAME

/**
 * A Room database model for the [Currency] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseCurrency(
    @PrimaryKey @ColumnInfo(name = ID) var id: Int,
    @ColumnInfo(name = SYMBOL) var symbol: String,
    @ColumnInfo(name = NAME) var name: String,
    @ColumnInfo(name = IS_ACTIVE) var isActive: Boolean,
    @ColumnInfo(name = IS_DELISTED) var isDelisted: Boolean,
    @ColumnInfo(name = PRECISION) var precision: Int,
    @ColumnInfo(name = MINIMUM_WITHDRAWAL_AMOUNT) var minimumWithdrawalAmount: Double,
    @ColumnInfo(name = MINIMUM_DEPOSIT_AMOUNT) var minimumDepositAmount: Double,
    @ColumnInfo(name = DEPOSIT_FEE_CURRENCY_ID) var depositFeeCurrencyId: Int,
    @ColumnInfo(name = DEPOSIT_FEE_CURRENCY_SYMBOL) var depositFeeCurrencySymbol: String,
    @ColumnInfo(name = DEPOSIT_FEE) var depositFee: Double,
    @ColumnInfo(name = DEPOSIT_FEE_IN_PERCENTAGE) var depositFeeInPercentage: Double,
    @ColumnInfo(name = WITHDRAWAL_FEE_CURRENCY_ID) var withdrawalFeeCurrencyId: Int,
    @ColumnInfo(name = WITHDRAWAL_FEE_CURRENCY_SYMBOL) var withdrawalFeeCurrencySymbol: String,
    @ColumnInfo(name = WITHDRAWAL_FEE) var withdrawalFee: Double,
    @ColumnInfo(name = WITHDRAWAL_FEE_IN_PERCENTAGE) var withdrawalFeeInPercentage: Double,
    @ColumnInfo(name = BLOCK_EXPLORER_URL) var blockExplorerUrl: String,
    @ColumnInfo(name = PROTOCOLS) var protocols: List<Protocol>,
    @ColumnInfo(name = WITHDRAWAL_LIMIT) var withdrawalLimit: Double
) {

    companion object {

        const val TABLE_NAME = "currencies"

        const val ID = "id"
        const val SYMBOL = "symbol"
        const val NAME = "name"
        const val IS_ACTIVE = "is_active"
        const val IS_DELISTED = "is_delisted"
        const val PRECISION = "precision"
        const val MINIMUM_WITHDRAWAL_AMOUNT = "minimum_withdrawal_amount"
        const val MINIMUM_DEPOSIT_AMOUNT = "minimum_deposit_amount"
        const val DEPOSIT_FEE_CURRENCY_ID = "deposit_fee_currency_id"
        const val DEPOSIT_FEE_CURRENCY_SYMBOL = "deposit_fee_currency_symbol"
        const val DEPOSIT_FEE = "deposit_fee"
        const val DEPOSIT_FEE_IN_PERCENTAGE = "deposit_fee_in_percentage"
        const val WITHDRAWAL_FEE_CURRENCY_ID = "withdrawal_fee_currency_id"
        const val WITHDRAWAL_FEE_CURRENCY_SYMBOL = "withdrawal_fee_currency_symbol"
        const val WITHDRAWAL_FEE = "withdrawal_fee"
        const val WITHDRAWAL_FEE_IN_PERCENTAGE = "withdrawal_fee_in_percentage"
        const val BLOCK_EXPLORER_URL = "block_explorer_url"
        const val PROTOCOLS = "protocols"
        const val WITHDRAWAL_LIMIT = "withdrawal_limit"

    }


    constructor(): this(
        id = -1,
        symbol = "",
        name = "",
        isActive = true,
        isDelisted = false,
        precision = 0,
        minimumWithdrawalAmount = -1.0,
        minimumDepositAmount = -1.0,
        depositFeeCurrencyId = -1,
        depositFeeCurrencySymbol = "",
        depositFee = -1.0,
        depositFeeInPercentage = -1.0,
        withdrawalFeeCurrencyId = -1,
        withdrawalFeeCurrencySymbol = "",
        withdrawalFee = -1.0,
        withdrawalFeeInPercentage = -1.0,
        blockExplorerUrl = "",
        protocols = listOf(),
        withdrawalLimit = 0.0
    )

}