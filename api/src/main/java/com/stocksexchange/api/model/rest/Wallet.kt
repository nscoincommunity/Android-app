package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stocksexchange.api.model.CoinStatus
import com.stocksexchange.api.utils.helpers.stripAdditionalParameterName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wallet(
    @SerializedName("id") val _id: Long? = null,
    @SerializedName("currency_id") val currencyId: Int,
    @SerializedName("currency_type_id") val currencyTypeId: Int,
    @SerializedName("delisted") val isDelisted: Boolean,
    @SerializedName("disabled") val isDisabled: Boolean,
    @SerializedName("disable_deposits") val isDepositingDisabled: Boolean,
    @SerializedName("currency_name") val currencyName: String,
    @SerializedName("currency_code") val currencySymbol: String,
    @SerializedName("balance") val currentBalance: Double,
    @SerializedName("frozen_balance") val frozenBalance: Double,
    @SerializedName("bonus_balance") val bonusBalance: Double,
    @SerializedName("total_balance") val totalBalance: Double,
    @SerializedName("deposit_address") val depositAddressData: TransactionAddressData? = null,
    @SerializedName("protocol_specific_settings") val _protocols: List<Protocol>? = null,
    @SerializedName("multi_deposit_addresses") val _multiDepositAddresses: List<TransactionAddressData>? = null,
    @SerializedName("withdrawal_additional_field_name") private val _additionalWithdrawalParameterName: String? = null,
    @SerializedName("withdrawal_limit") val withdrawalLimit: Double
) : Parcelable {


    companion object {

        private const val WITHDRAWAL_PARAMETER_OPTIONALITY_TOKEN = "optional"

        val STUB_WALLET = Wallet(
            _id = null,
            currencyId = -1,
            currencyTypeId = -1,
            isDelisted = false,
            isDisabled = false,
            isDepositingDisabled = false,
            currencyName = "",
            currencySymbol = "",
            currentBalance = 0.0,
            frozenBalance = 0.0,
            bonusBalance = 0.0,
            totalBalance = 0.0,
            depositAddressData = null,
            _additionalWithdrawalParameterName = null,
            withdrawalLimit = 0.0
        )

    }


    val hasId: Boolean
        get() = (_id != null)


    val hasDepositAddressData: Boolean
        get() = (depositAddressData != null)


    val hasProtocols: Boolean
        get() = protocols.isNotEmpty()


    val hasMultipleProtocols: Boolean
        get() = (protocols.size > 1)


    val hasMultiDepositAddresses: Boolean
        get() = multiDepositAddresses.isNotEmpty()


    val hasAdditionalWithdrawalParameter: Boolean
        get() = additionalWithdrawalParameterName.isNotBlank()


    val isStub: Boolean
        get() = (this == STUB_WALLET)


    val isCurrentBalanceEmpty: Boolean
        get() = (currentBalance == 0.0)


    val isEmpty: Boolean
        get() = (totalBalance == 0.0)


    val isDepositingEnabled: Boolean
        get() = (!isDelisted && !isDepositingDisabled)


    val isAdditionalWithdrawalParameterOptional: Boolean
        get() = additionalWithdrawalParameterName
                .toLowerCase()
                .contains(WITHDRAWAL_PARAMETER_OPTIONALITY_TOKEN)


    val id: Long
        get() = (_id ?: -1L)


    val additionalWithdrawalParameterName: String
        get() = (_additionalWithdrawalParameterName ?: "")


    val strippedAdditionalWithdrawalParameterName: String
        get() = stripAdditionalParameterName(additionalWithdrawalParameterName)


    val status: CoinStatus
        get() = when {
            isDisabled -> CoinStatus.DISABLED
            isDelisted -> CoinStatus.DELISTED

            else -> CoinStatus.ENABLED
        }


    val protocols: List<Protocol>
        get() {
            val protocols = (_protocols ?: listOf())

            return mutableListOf<Protocol>().apply {
                for(protocol in protocols) {
                    if(protocol.isActive) {
                        add(protocol)
                    }
                }
            }
        }


    val multiDepositAddresses: List<TransactionAddressData>
        get() = (_multiDepositAddresses ?: listOf())


    fun getProtocols(transactionType: TransactionType): List<Protocol> {
        val protocols = (_protocols ?: listOf())

        return mutableListOf<Protocol>().apply {
            for(protocol in protocols) {
                val disableItem = if (transactionType == TransactionType.DEPOSITS) {
                    protocol.disableDeposits
                } else{
                    protocol.disableWithdrawals
                }

                if(protocol.isActive && !disableItem) {
                    add(protocol)
                }
            }
        }
    }

    fun hasProtocol(protocolId: Int): Boolean {
        if((protocolId == -1) || !hasProtocols) {
            return false
        }

        return (getProtocol(protocolId) != null)
    }


    fun getProtocol(protocolId: Int): Protocol? {
        if((protocolId == -1) || !hasProtocols) {
            return null
        }

        return protocols.firstOrNull {
            it.id == protocolId
        }
    }


    fun hasDepositAddressData(protocolId: Int = -1): Boolean {
        if((protocolId == -1) || !hasMultiDepositAddresses) {
            return hasDepositAddressData
        }

        return (getDepositAddressData(protocolId) != null)
    }


    fun getDepositAddressData(protocolId: Int = -1): TransactionAddressData? {
        if((protocolId == -1) || !hasMultiDepositAddresses) {
            return depositAddressData
        }

        return multiDepositAddresses.firstOrNull {
            it.protocolId == protocolId
        }
    }


}