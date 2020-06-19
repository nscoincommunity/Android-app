package com.stocksexchange.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PinCode(val code: String) : Parcelable {


    companion object {

        fun getEmptyPinCode(): PinCode = PinCode("")

    }


    fun hasCode(): Boolean = code.isNotBlank()


}