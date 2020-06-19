package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignUpParameters(
    val email: String,
    val password: String,
    val referralCode: String
) : Parcelable {


    companion object {

        fun getDefaultParameters(): SignUpParameters {
            return SignUpParameters(
                email = "",
                password = "",
                referralCode = ""
            )
        }

    }


}