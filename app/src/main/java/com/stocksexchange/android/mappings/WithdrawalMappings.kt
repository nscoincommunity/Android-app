package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.Withdrawal
import com.stocksexchange.android.database.model.DatabaseWithdrawal

fun Withdrawal.mapToDatabaseWithdrawal(): DatabaseWithdrawal {
    return DatabaseWithdrawal(
        id = id,
        currencyId = currencyId,
        currencySymbol = currencySymbol,
        amount = amount,
        fee = fee,
        feeCurrencyId = feeCurrencyId,
        feeCurrencySymbol = feeCurrencySymbol,
        statusId = statusId,
        status = status,
        statusColor = statusColor,
        creationTimestamp = creationTimestamp,
        updateTimestamp = updateTimestamp,
        transactionExplorerId = transactionExplorerId,
        addressData = addressData
    )
}


fun List<Withdrawal>.mapToDatabaseWithdrawalList(): List<DatabaseWithdrawal> {
    return map { it.mapToDatabaseWithdrawal() }
}


fun DatabaseWithdrawal.mapToWithdrawal(): Withdrawal {
    return Withdrawal(
        id = id,
        currencyId = currencyId,
        currencySymbol = currencySymbol,
        amount = amount,
        fee = fee,
        feeCurrencyId = feeCurrencyId,
        feeCurrencySymbol = feeCurrencySymbol,
        statusId = statusId,
        status = status,
        statusColor = statusColor,
        creationTimestamp = creationTimestamp,
        updateTimestamp = updateTimestamp,
        transactionExplorerId = transactionExplorerId,
        addressData = addressData
    )
}


fun List<DatabaseWithdrawal>.mapToWithdrawalList(): List<Withdrawal> {
    return map { it.mapToWithdrawal() }
}