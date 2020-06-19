package com.stocksexchange.android.mappings

import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.android.database.model.DatabaseWallet
import com.stocksexchange.android.ui.wallets.fragment.WalletItem

fun Wallet.mapToDatabaseWallet(): DatabaseWallet {
    return DatabaseWallet(
        id = _id,
        currencyId = currencyId,
        currencyTypeId = currencyTypeId,
        isDelisted = isDelisted,
        isDisabled = isDisabled,
        isDepositingDisabled = isDepositingDisabled,
        currencyName = currencyName,
        currencySymbol = currencySymbol,
        currentBalance = currentBalance,
        frozenBalance = frozenBalance,
        bonusBalance = bonusBalance,
        totalBalance = totalBalance,
        depositAddressData = depositAddressData,
        protocols = protocols,
        multiDepositAddresses = multiDepositAddresses,
        additionalWithdrawalParameterName = additionalWithdrawalParameterName,
        withdrawalLimit = withdrawalLimit
    )
}


fun Wallet.mapToWalletItem(): WalletItem {
    return WalletItem(this)
}


fun List<Wallet>.mapToDatabaseWalletList(): List<DatabaseWallet> {
    return map { it.mapToDatabaseWallet() }
}


fun List<Wallet>.mapToWalletItemList(): List<WalletItem> {
    return map { it.mapToWalletItem() }
}


fun List<Wallet>.mapToCurrencyIdWalletMap(): Map<Int, Wallet> {
    return mutableMapOf<Int, Wallet>().apply {
        for(wallet in this@mapToCurrencyIdWalletMap) {
            put(wallet.currencyId, wallet)
        }
    }
}


fun DatabaseWallet.mapToWallet(): Wallet {
    return Wallet(
        _id = id,
        currencyId = currencyId,
        currencyTypeId = currencyTypeId,
        isDelisted = isDelisted,
        isDisabled = isDisabled,
        isDepositingDisabled = isDepositingDisabled,
        currencyName = currencyName,
        currencySymbol = currencySymbol,
        currentBalance = currentBalance,
        frozenBalance = frozenBalance,
        bonusBalance = bonusBalance,
        totalBalance = totalBalance,
        depositAddressData = depositAddressData,
        _protocols = protocols,
        _multiDepositAddresses = multiDepositAddresses,
        _additionalWithdrawalParameterName = additionalWithdrawalParameterName,
        withdrawalLimit = withdrawalLimit
    )
}


fun List<DatabaseWallet>.mapToWalletList(): List<Wallet> {
    return map { it.mapToWallet() }
}