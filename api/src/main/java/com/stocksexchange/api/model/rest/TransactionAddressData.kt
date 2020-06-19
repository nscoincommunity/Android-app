package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stocksexchange.api.utils.helpers.stripAdditionalParameterName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionAddressData(
    @SerializedName("address_name") private val _addressParameterName: String? = null,
    @SerializedName("address") private val _addressParameterValue: String? = null,
    @SerializedName("additional_address_parameter_name") private val _additionalParameterName: String? = null,
    @SerializedName("additional_address_parameter") private val _additionalParameterValue: String? = null,
    @SerializedName("notification") private val _notification: String? = null,
    @SerializedName("protocol_id") private val _protocolId: Int? = null,
    @SerializedName("protocol_name") private val _protocolName: String? = null,
    @SerializedName("supports_new_address_creation") private val _supportsNewAddressCreation: Boolean? = null
) : Parcelable {


    companion object {

        val STUB_TRANSACTION_ADDRESS_DATA = TransactionAddressData()

    }


    val hasAddressParameter: Boolean
        get() = (
            addressParameterName.isNotBlank() &&
            addressParameterValue.isNotBlank()
        )


    val hasAdditionalParameter: Boolean
        get() = (
            additionalParameterName.isNotBlank() &&
            additionalParameterValue.isNotBlank()
        )


    val hasAtLeastOneParameter: Boolean
        get() = (hasAddressParameter || hasAdditionalParameter)


    val hasAtLeastTwoParameters: Boolean
        get() = (hasAddressParameter && hasAdditionalParameter)


    val hasNotification: Boolean
        get() = notification.isNotEmpty()


    val hasProtocolId: Boolean
        get() = (_protocolId != null)


    val hasProtocolName: Boolean
        get() = (_protocolName != null)


    val supportsNewAddressCreation: Boolean
        get() = (_supportsNewAddressCreation ?: false)


    val protocolId: Int
        get() = (_protocolId ?: -1)


    val addressParameterName: String
        get() = (_addressParameterName ?: "")


    val addressParameterValue: String
        get() = (_addressParameterValue ?: "")


    val additionalParameterName: String
        get() = (_additionalParameterName ?: "")


    val strippedAdditionalParameterName: String
        get() = stripAdditionalParameterName(additionalParameterName)


    val additionalParameterValue: String
        get() = (_additionalParameterValue ?: "")


    val notification: String
        get() = (_notification ?: "")


    val protocolName: String
        get() = (_protocolName ?: "")


    val notificationType: TransactionNotificationType
        get() = TransactionNotificationType.newInstance(notification)


}