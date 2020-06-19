package com.stocksexchange.android.mappings

import com.stocksexchange.android.database.model.DatabaseCurrency
import com.stocksexchange.api.model.rest.Currency

fun Currency.mapToDatabaseCurrency(): DatabaseCurrency {
    return DatabaseCurrency(
        id = id,
        symbol = symbol,
        name = name,
        isActive = isActive,
        isDelisted = isDelisted,
        precision = precision,
        minimumWithdrawalAmount = minimumWithdrawalAmount,
        minimumDepositAmount = minimumDepositAmount,
        depositFeeCurrencyId = depositFeeCurrencyId,
        depositFeeCurrencySymbol = depositFeeCurrencySymbol,
        depositFee = depositFee,
        depositFeeInPercentage = depositFeeInPercentage,
        withdrawalFeeCurrencyId = withdrawalFeeCurrencyId,
        withdrawalFeeCurrencySymbol = withdrawalFeeCurrencySymbol,
        withdrawalFee = withdrawalFee,
        withdrawalFeeInPercentage = withdrawalFeeInPercentage,
        blockExplorerUrl = blockExplorerUrl,
        protocols = protocols,
        withdrawalLimit = withdrawalLimit
    )
}


fun List<Currency>.mapToDatabaseCurrencyList(): List<DatabaseCurrency> {
    return map { it.mapToDatabaseCurrency() }
}


fun List<Currency>.mapToIdCurrencyMap(): Map<Int, Currency> {
    return mutableMapOf<Int, Currency>().apply {
        for(item in this@mapToIdCurrencyMap) {
            put(item.id, item)
        }
    }
}


fun DatabaseCurrency.mapToCurrency(): Currency {
    return Currency(
        id = id,
        symbol = symbol,
        name = name,
        isActive = isActive,
        isDelisted = isDelisted,
        precision = precision,
        minimumWithdrawalAmount = minimumWithdrawalAmount,
        minimumDepositAmount = minimumDepositAmount,
        depositFeeCurrencyId = depositFeeCurrencyId,
        depositFeeCurrencySymbol = depositFeeCurrencySymbol,
        depositFee = depositFee,
        depositFeeInPercentage = depositFeeInPercentage,
        withdrawalFeeCurrencyId = withdrawalFeeCurrencyId,
        withdrawalFeeCurrencySymbol = withdrawalFeeCurrencySymbol,
        withdrawalFee = withdrawalFee,
        withdrawalFeeInPercentage = withdrawalFeeInPercentage,
        _blockExplorerUrl = blockExplorerUrl,
        _protocols = protocols,
        withdrawalLimit = withdrawalLimit
    )
}


fun List<DatabaseCurrency>.mapToCurrencyList(): List<Currency> {
    return map { it.mapToCurrency() }
}