package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WithdrawalConfirmationParameters(
    val withdrawalId: Long,
    val confirmationToken: String
) : Parcelable