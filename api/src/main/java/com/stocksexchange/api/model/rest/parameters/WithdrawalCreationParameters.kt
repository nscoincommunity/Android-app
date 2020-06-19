package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WithdrawalCreationParameters(
    val currencyId: Int,
    val protocolId: Int,
    val amount: String,
    val address: String,
    val additionalAddress: String
) : Parcelable