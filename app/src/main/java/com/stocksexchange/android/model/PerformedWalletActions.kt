package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.api.model.rest.Wallet
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PerformedWalletActions(
    val idCreatedWalletsMap: MutableMap<Int, Wallet> = mutableMapOf(),
    val balanceChangedWalletsMap: MutableMap<Int, Wallet> = mutableMapOf()
) : Parcelable {


    fun addIdCreatedWallet(wallet: Wallet): PerformedWalletActions {
        idCreatedWalletsMap[wallet.currencyId] = wallet
        return this
    }


    fun removeIdCreatedWallet(wallet: Wallet): PerformedWalletActions {
        idCreatedWalletsMap.remove(wallet.currencyId)
        return this
    }


    fun removeAllIdCreatedWallets() {
        idCreatedWalletsMap.clear()
    }


    fun hasIdCreatedWallets(): Boolean {
        return idCreatedWalletsMap.isNotEmpty()
    }


    fun addBalanceChangedWallet(wallet: Wallet): PerformedWalletActions {
        balanceChangedWalletsMap[wallet.currencyId] = wallet
        return this
    }


    fun removeBalanceChangedWallet(wallet: Wallet): PerformedWalletActions {
        balanceChangedWalletsMap.remove(wallet.currencyId)
        return this
    }


    fun removeAllBalanceChangedWallets() {
        balanceChangedWalletsMap.clear()
    }


    fun hasBalanceChangedWallets(): Boolean {
        return balanceChangedWalletsMap.isNotEmpty()
    }


    fun merge(actions: PerformedWalletActions): PerformedWalletActions {
        balanceChangedWalletsMap.putAll(actions.idCreatedWalletsMap)
        balanceChangedWalletsMap.putAll(actions.balanceChangedWalletsMap)

        return this
    }


    fun clear() {
        if(isEmpty()) {
            return
        }

        if(hasIdCreatedWallets()) {
            removeAllIdCreatedWallets()
        }

        if(hasBalanceChangedWallets()) {
            removeAllBalanceChangedWallets()
        }
    }


    fun isEmpty(): Boolean {
        return (!hasIdCreatedWallets() &&
                !hasBalanceChangedWallets())
    }


}