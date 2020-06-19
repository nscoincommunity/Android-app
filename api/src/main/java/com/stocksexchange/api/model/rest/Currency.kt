package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Currency(
    @SerializedName("id") val id: Int,
    @SerializedName("code") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("active") val isActive: Boolean,
    @SerializedName("delisted") val isDelisted: Boolean,
    @SerializedName("precision") val precision: Int,
    @SerializedName("minimum_withdrawal_amount") val minimumWithdrawalAmount: Double,
    @SerializedName("minimum_deposit_amount") val minimumDepositAmount: Double,
    @SerializedName("deposit_fee_currency_id") val depositFeeCurrencyId: Int,
    @SerializedName("deposit_fee_currency_code") val depositFeeCurrencySymbol: String,
    @SerializedName("deposit_fee_const") val depositFee: Double,
    @SerializedName("deposit_fee_percent") val depositFeeInPercentage: Double,
    @SerializedName("withdrawal_fee_currency_id") val withdrawalFeeCurrencyId: Int,
    @SerializedName("withdrawal_fee_currency_code") val withdrawalFeeCurrencySymbol: String,
    @SerializedName("withdrawal_fee_const") val withdrawalFee: Double,
    @SerializedName("withdrawal_fee_percent") val withdrawalFeeInPercentage: Double,
    @SerializedName("block_explorer_url") private val _blockExplorerUrl: String? = null,
    @SerializedName("protocol_specific_settings") private val _protocols: List<Protocol>? = null,
    @SerializedName("withdrawal_limit") val withdrawalLimit: Double
) : Parcelable {


    companion object {

        val STUB_CURRENCY = Currency(
            id = -1,
            symbol = "",
            name = "",
            isActive = false,
            isDelisted = false,
            precision = 0,
            minimumWithdrawalAmount = 0.0,
            minimumDepositAmount = 0.0,
            depositFeeCurrencyId = -1,
            depositFeeCurrencySymbol = "",
            depositFee = 0.0,
            depositFeeInPercentage = 0.0,
            withdrawalFeeCurrencyId = -1,
            withdrawalFeeCurrencySymbol = "",
            withdrawalFee = 0.0,
            withdrawalFeeInPercentage = 0.0,
            _blockExplorerUrl = null,
            withdrawalLimit = 0.0
        )

    }


    val isStub: Boolean
        get() = (this == STUB_CURRENCY)


    val hasProtocols: Boolean
        get() = protocols.isNotEmpty()


    val blockExplorerUrl: String
        get() = (_blockExplorerUrl ?: "")


    val protocols: List<Protocol>
        get() = (_protocols ?: listOf())




    fun getProtocol(protocolId: Int): Protocol? {
        if((protocolId <= 0) || !hasProtocols) {
            return null
        }

        return protocols.firstOrNull {
            it.id == protocolId
        }
    }


    fun getBlockExplorerUrl(protocolId: Int): String {
        return (getProtocol(protocolId)?.blockExplorerUrl ?: blockExplorerUrl)
    }


}