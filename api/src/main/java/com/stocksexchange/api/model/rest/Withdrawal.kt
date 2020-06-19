package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stocksexchange.api.utils.helpers.getTransactionExplorerUrl
import com.stocksexchange.api.utils.helpers.isTransactionInternal
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Withdrawal(
    @SerializedName("id") val id: Long,
    @SerializedName("currency_id") val currencyId: Int,
    @SerializedName("currency_code") val currencySymbol: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("fee") val fee: Double,
    @SerializedName("fee_currency_id") val feeCurrencyId: Int,
    @SerializedName("fee_currency_code") val feeCurrencySymbol: String,
    @SerializedName("withdrawal_status_id") val statusId: Int,
    @SerializedName("status") val status: String,
    @SerializedName("status_color") val statusColor: String,
    @SerializedName("created_ts") val creationTimestamp: Long,
    @SerializedName("updated_ts") val updateTimestamp: Long,
    @SerializedName("txid") val transactionExplorerId: String? = null,
    @SerializedName("withdrawal_address") val addressData: TransactionAddressData
) : Parcelable {


    companion object {

        private const val STATUS_ID_CANCELLED_BY_USER = 2
        private const val STATUS_TEXT_CANCELLED_BY_USER = "Cancelled by User"
        private const val STATUS_COLOR_CANCELLED_BY_USER = "#BC3D51"

        private const val STATUS_ID_NOT_CONFIRMED = 1

        val STUB_WITHDRAWAl = Withdrawal(
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
            creationTimestamp = -1L,
            updateTimestamp = -1L,
            transactionExplorerId = null,
            addressData = TransactionAddressData.STUB_TRANSACTION_ADDRESS_DATA
        )

    }


    val hasTransactionExplorerId: Boolean
        get() = (transactionExplorerId != null)


    val isStub: Boolean
        get() = (this == STUB_WITHDRAWAl)


    val isInternal: Boolean
        get() = isTransactionInternal(transactionExplorerId)


    val isNotConfirmed: Boolean
        get() = (statusId == STATUS_ID_NOT_CONFIRMED)


    val protocolId: Int
        get() = addressData.protocolId


    val timestampInMillis: Long
        get() = (creationTimestamp * 1000L)




    fun toCancelledByUser(): Withdrawal {
        return copy(
            statusId = STATUS_ID_CANCELLED_BY_USER,
            status = STATUS_TEXT_CANCELLED_BY_USER,
            statusColor = STATUS_COLOR_CANCELLED_BY_USER
        )
    }


    fun getExplorerUrl(blockExplorerUrl: String): String {
        return if(hasTransactionExplorerId) {
            getTransactionExplorerUrl(blockExplorerUrl, transactionExplorerId!!)
        } else {
            ""
        }
    }


}