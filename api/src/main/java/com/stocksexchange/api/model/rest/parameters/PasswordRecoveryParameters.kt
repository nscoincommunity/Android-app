package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PasswordRecoveryParameters(
    val passwordResetToken: String,
    val email: String,
    val newPassword: String
) : Parcelable {


    companion object {

        fun getDefaultParameters(): PasswordRecoveryParameters {
            return PasswordRecoveryParameters(
                passwordResetToken = "",
                email = "",
                newPassword = ""
            )
        }

    }


}