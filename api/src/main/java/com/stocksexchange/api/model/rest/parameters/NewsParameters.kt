package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsParameters(
    val limit: Int
) : Parcelable {


    companion object {

        fun getDefaultParameters(): NewsParameters {
            return NewsParameters(
                limit = 40
            )
        }

    }


}