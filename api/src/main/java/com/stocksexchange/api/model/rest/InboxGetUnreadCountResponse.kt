package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InboxGetUnreadCountResponse(
    @SerializedName("counts") val counts: Int
) : Parcelable
