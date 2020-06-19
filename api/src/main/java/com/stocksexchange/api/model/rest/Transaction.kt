package com.stocksexchange.api.model.rest

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A model class that represents a transaction (either a deposit
 * or a withdrawal).
 */
@Parcelize
data class Transaction(
    val type: TransactionType,
    val blockExplorerUrl: String,
    val deposit: Deposit = Deposit.STUB_DEPOSIT,
    val withdrawal: Withdrawal = Withdrawal.STUB_WITHDRAWAl
) : Parcelable {


    val hasBlockExplorerUrl: Boolean
        get() = blockExplorerUrl.isNotBlank()


    val hasTransactionExplorerId: Boolean
        get() = when(type) {
            TransactionType.DEPOSITS -> deposit.hasTransactionExplorerId
            TransactionType.WITHDRAWALS -> withdrawal.hasTransactionExplorerId
        }


    val isInternal: Boolean
        get() = when(type) {
            TransactionType.DEPOSITS -> deposit.isInternal
            TransactionType.WITHDRAWALS -> withdrawal.isInternal
        }


    val id: Long
        get() = when(type) {
            TransactionType.DEPOSITS -> deposit.id
            TransactionType.WITHDRAWALS -> withdrawal.id
        }


    val timestampInMillis: Long
        get() = when(type) {
            TransactionType.DEPOSITS -> deposit.timestampInMillis
            TransactionType.WITHDRAWALS -> withdrawal.timestampInMillis
        }


    val amount: Double
        get() = when(type) {
            TransactionType.DEPOSITS -> deposit.amount
            TransactionType.WITHDRAWALS -> withdrawal.amount
        }


    val fee: Double
        get() = when(type) {
            TransactionType.DEPOSITS -> deposit.fee
            TransactionType.WITHDRAWALS -> withdrawal.fee
        }


    val status: String
        get() = when(type) {
            TransactionType.DEPOSITS -> deposit.status
            TransactionType.WITHDRAWALS -> withdrawal.status
        }


    val statusColor: String
        get() = when(type) {
            TransactionType.DEPOSITS -> deposit.statusColor
            TransactionType.WITHDRAWALS -> withdrawal.statusColor
        }


    val currencySymbol: String
        get() = when(type) {
            TransactionType.DEPOSITS -> deposit.currencySymbol
            TransactionType.WITHDRAWALS -> withdrawal.currencySymbol
        }


    val feeCurrencySymbol: String
        get() = when(type) {
            TransactionType.DEPOSITS -> deposit.feeCurrencySymbol
            TransactionType.WITHDRAWALS -> withdrawal.feeCurrencySymbol
        }


    val transactionExplorerId: String
        get() = if(hasTransactionExplorerId) {
            when(type) {
                TransactionType.DEPOSITS -> deposit.transactionExplorerId!!
                TransactionType.WITHDRAWALS -> withdrawal.transactionExplorerId!!
            }
        } else {
            ""
        }


    val transactionExplorerUrl: String
        get() = when(type) {
            TransactionType.DEPOSITS -> deposit.getExplorerUrl(blockExplorerUrl)
            TransactionType.WITHDRAWALS -> withdrawal.getExplorerUrl(blockExplorerUrl)
        }


}