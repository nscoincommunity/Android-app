package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stocksexchange.api.model.rest.Protocol
import com.stocksexchange.api.model.rest.TransactionAddressData
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.android.database.model.DatabaseWallet.Companion.TABLE_NAME

/**
 * A Room database model for the [Wallet] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseWallet(
    @ColumnInfo(name = ID) var id: Long?,
    @PrimaryKey @ColumnInfo(name = CURRENCY_ID) var currencyId: Int,
    @ColumnInfo(name = CURRENCY_TYPE_ID) var currencyTypeId: Int,
    @ColumnInfo(name = IS_DELISTED) var isDelisted: Boolean,
    @ColumnInfo(name = IS_DISABLED) var isDisabled: Boolean,
    @ColumnInfo(name = IS_DEPOSITING_DISABLED) var isDepositingDisabled: Boolean,
    @ColumnInfo(name = CURRENCY_NAME) var currencyName: String,
    @ColumnInfo(name = CURRENCY_SYMBOL) var currencySymbol: String,
    @ColumnInfo(name = CURRENT_BALANCE) var currentBalance: Double,
    @ColumnInfo(name = FROZEN_BALANCE) var frozenBalance: Double,
    @ColumnInfo(name = BONUS_BALANCE) var bonusBalance: Double,
    @ColumnInfo(name = TOTAL_BALANCE) var totalBalance: Double,
    @ColumnInfo(name = DEPOSIT_ADDRESS_DATA) var depositAddressData: TransactionAddressData?,
    @ColumnInfo(name = PROTOCOLS) var protocols: List<Protocol>,
    @ColumnInfo(name = MULTI_DEPOSIT_ADDRESSES) var multiDepositAddresses: List<TransactionAddressData>,
    @ColumnInfo(name = ADDITIONAL_WITHDRAWAL_PARAMETER_NAME) var additionalWithdrawalParameterName: String?,
    @ColumnInfo(name = WITHDRAWAL_LIMIT) var withdrawalLimit: Double
) {

    companion object {

        const val TABLE_NAME = "wallets"

        const val ID = "id"
        const val CURRENCY_ID = "currency_id"
        const val CURRENCY_TYPE_ID = "currency_type_id"
        const val IS_DELISTED = "is_delisted"
        const val IS_DISABLED = "is_disabled"
        const val IS_DEPOSITING_DISABLED = "is_depositing_disabled"
        const val CURRENCY_NAME = "currency_name"
        const val CURRENCY_SYMBOL = "currency_symbol"
        const val CURRENT_BALANCE = "current_balance"
        const val FROZEN_BALANCE = "frozen_balance"
        const val BONUS_BALANCE = "bonus_balance"
        const val TOTAL_BALANCE = "total_balance"
        const val DEPOSIT_ADDRESS_DATA = "deposit_address_data"
        const val PROTOCOLS = "protocols"
        const val MULTI_DEPOSIT_ADDRESSES = "multi_deposit_addresses"
        const val ADDITIONAL_WITHDRAWAL_PARAMETER_NAME = "additional_withdrawal_parameter_name"
        const val WITHDRAWAL_LIMIT = "withdrawal_limit"
    }


    constructor(): this(
        id = null,
        currencyId = -1,
        currencyTypeId = -1,
        isDelisted = false,
        isDisabled = false,
        isDepositingDisabled = false,
        currencyName = "",
        currencySymbol = "",
        currentBalance = -1.0,
        frozenBalance = -1.0,
        bonusBalance = -1.0,
        totalBalance = -1.0,
        depositAddressData = null,
        protocols = listOf(),
        multiDepositAddresses = listOf(),
        additionalWithdrawalParameterName = null,
        withdrawalLimit = 0.0
    )

}