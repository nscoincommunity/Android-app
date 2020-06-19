package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stocksexchange.api.utils.helpers.getTransactionExplorerUrl
import com.stocksexchange.api.utils.helpers.isTransactionInternal
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Deposit(
    @SerializedName("id") val id: Long,
    @SerializedName("currency_id") val currencyId: Int,
    @SerializedName("currency_code") val currencySymbol: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("fee") val fee: Double,
    @SerializedName("deposit_fee_currency_id") val feeCurrencyId: Int,
    @SerializedName("deposit_fee_currency_code") val feeCurrencySymbol: String,
    @SerializedName("deposit_status_id") val statusId: Int,
    @SerializedName("status") val status: String,
    @SerializedName("status_color") val statusColor: String,
    @SerializedName("protocol_id") val protocolId: Int,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("txid") val transactionExplorerId: String? = null,
    @SerializedName("confirmations") val confirmationsStr: String
) : Parcelable {


    companion object {

        val STUB_DEPOSIT = Deposit(
            id = -1L,
            currencyId = -1,
            currencySymbol = "",
            amount = 0.0,
            fee = 0.0,
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


    val hasProtocolId: Boolean
        get() = (protocolId > 0)


    val hasTransactionExplorerId: Boolean
        get() = (transactionExplorerId != null)


    val isStub: Boolean
        get() = (this == STUB_DEPOSIT)


    val isInternal: Boolean
        get() = isTransactionInternal(transactionExplorerId)


    val timestampInMillis: Long
        get() = (timestamp * 1000L)


    val confirmations: DepositConfirmations?
        get() = DepositConfirmations.newInstance(confirmationsStr)




    fun getExplorerUrl(blockExplorerUrl: String): String {
        return if(hasTransactionExplorerId) {
            getTransactionExplorerUrl(blockExplorerUrl, transactionExplorerId!!)
        } else {
            ""
        }
    }


}