package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stocksexchange.api.utils.Constants
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReferralProgram(
    @SerializedName("referral_code") val code: String,
    @SerializedName("members") val memberCount: Int,
    @SerializedName("invited") val isInvited: Boolean
) : Parcelable {


    companion object {

        val STUB = ReferralProgram(
            code = "",
            memberCount = 0,
            isInvited = false
        )

    }


    val invitationLink: String
        get() = (Constants.STEX_REFERRAL_BASE_URL + code)


}