package com.stocksexchange.android.model

import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NavigationDeepLinkData(
    val destinationId: Int,
    val destinationArgs: Bundle? = null
) : Parcelable {


    companion object {

        val STUB = NavigationDeepLinkData(
            destinationId = 0
        )

    }




    val isStub: Boolean
        get() = (this == STUB)


}