package com.stocksexchange.android.model

import android.os.Parcelable
import com.stocksexchange.api.model.rest.Currency
import com.stocksexchange.api.model.rest.Wallet
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionData(
    val wallet: Wallet = Wallet.STUB_WALLET,
    val currency: Currency = Currency.STUB_CURRENCY
) : Parcelable {


    val isEmpty: Boolean
        get() = (wallet.isStub || currency.isStub)




    fun getWithdrawalFeeCurrencyId(protocolId: Int): Int {
        return if(wallet.hasProtocol(protocolId)) {
            (wallet.getProtocol(protocolId)?.withdrawalFeeCurrencyId ?: 0)
        } else {
            currency.withdrawalFeeCurrencyId
        }
    }


    fun getWithdrawalFee(protocolId: Int): Double {
        return if(wallet.hasProtocol(protocolId)) {
            (wallet.getProtocol(protocolId)?.withdrawalFee ?: 0.0)
        } else {
            currency.withdrawalFee
        }
    }


    fun getWithdrawalLimit(protocolId: Int): Double {
        return if(wallet.hasProtocol(protocolId)) {
            (wallet.getProtocol(protocolId)?.withdrawalLimit ?: 0.0)
        } else {
            currency.withdrawalLimit
        }
    }


    fun getWithdrawalCurrencySymbol(protocolId: Int): String {
        return if(wallet.hasProtocol(protocolId)) {
            (wallet.getProtocol(protocolId)?.withdrawalFeeCurrencySymbol ?: "")
        } else {
            currency.withdrawalFeeCurrencySymbol
        }
    }


}