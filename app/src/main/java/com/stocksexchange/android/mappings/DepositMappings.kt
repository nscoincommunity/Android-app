package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.Deposit
import com.stocksexchange.android.database.model.DatabaseDeposit

fun Deposit.mapToDatabaseDeposit(): DatabaseDeposit {
    return DatabaseDeposit(
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
        protocolId = protocolId,
        timestamp = timestamp,
        transactionExplorerId = transactionExplorerId,
        confirmationsStr = confirmationsStr
    )
}


fun List<Deposit>.mapToDatabaseDepositList(): List<DatabaseDeposit> {
    return map { it.mapToDatabaseDeposit() }
}


fun DatabaseDeposit.mapToDeposit(): Deposit {
    return Deposit(
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
        protocolId = protocolId,
        timestamp = timestamp,
        transactionExplorerId = transactionExplorerId,
        confirmationsStr = confirmationsStr
    )
}


fun List<DatabaseDeposit>.mapToDepositList(): List<Deposit> {
    return map { it.mapToDeposit() }
}