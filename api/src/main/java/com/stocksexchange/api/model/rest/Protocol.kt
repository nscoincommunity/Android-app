package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Protocol(
    @SerializedName("protocol_id") val id: Int,
    @SerializedName("protocol_name") val name: String,
    @SerializedName("active") val isActive: Boolean,
    @SerializedName("withdrawal_fee_currency_id") val withdrawalFeeCurrencyId: Int,
    @SerializedName("withdrawal_fee_currency_code") private val _withdrawalFeeCurrencySymbol: String? = null,
    @SerializedName("withdrawal_fee_const") val withdrawalFee: Double,
    @SerializedName("withdrawal_fee_percent") val withdrawalFeeInPercentage: Double,
    @SerializedName("block_explorer_url") private val _blockExplorerUrl: String? = null,
    @SerializedName("withdrawal_limit") val withdrawalLimit: Double,
    @SerializedName("disable_deposits") val _disableDeposits: Boolean? = false,
    @SerializedName("disable_withdrawals") val _disableWithdrawals: Boolean? = false
) : Parcelable {


    companion object {

        val STUB_PROTOCOL = Protocol(
            id = -1,
            name = "",
            isActive = false,
            withdrawalFeeCurrencyId = -1,
            _withdrawalFeeCurrencySymbol = "",
            withdrawalFee = 0.0,
            withdrawalFeeInPercentage = 0.0,
            _blockExplorerUrl = "",
            withdrawalLimit = 0.0,
            _disableDeposits = false,
            _disableWithdrawals = false
        )

    }




    val isStub: Boolean
        get() = (this == STUB_PROTOCOL)


    val hasWithdrawalFeeCurrencySymbol: Boolean
        get() = withdrawalFeeCurrencySymbol.isNotBlank()


    val hasBlockExplorerUrl: Boolean
        get() = blockExplorerUrl.isNotEmpty()


    val withdrawalFeeCurrencySymbol: String
        get() = (_withdrawalFeeCurrencySymbol ?: "")


    val blockExplorerUrl: String
        get() = (_blockExplorerUrl ?: "")


    val disableDeposits : Boolean
        get() = _disableDeposits ?: false


    val disableWithdrawals : Boolean
        get() = _disableWithdrawals ?: false


}