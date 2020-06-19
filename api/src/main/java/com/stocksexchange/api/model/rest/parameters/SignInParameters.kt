package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import com.stocksexchange.core.utils.extensions.sha1
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignInParameters(
    val email: String,
    val password: String,
    val key: String,
    val code: String
) : Parcelable {


    companion object {

        const val KEY_STRING_LENGTH = 20


        fun getDefaultParameters(): SignInParameters {
            return SignInParameters(
                email = "",
                password = "",
                key = "",
                code = ""
            )
        }


    }


    /**
     * A field that returns a SHA-1 hash of the key.
     */
    val hashedKey: String
        get() = key.sha1()


}