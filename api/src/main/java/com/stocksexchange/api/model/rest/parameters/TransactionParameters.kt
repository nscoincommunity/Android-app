package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import com.stocksexchange.api.model.rest.SortOrder
import com.stocksexchange.api.model.rest.TransactionMode
import com.stocksexchange.api.model.rest.TransactionType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionParameters(
    val mode: TransactionMode,
    val type: TransactionType,
    val sortOrder: SortOrder,
    val searchQuery: String,
    val limit: Int,
    val offset: Int
) : Parcelable {


    companion object {


        fun getStandardParameters(type: TransactionType): TransactionParameters {
            return getDefaultParameters(
                mode = TransactionMode.STANDARD,
                type = type
            )
        }


        fun getSearchParameters(type: TransactionType): TransactionParameters {
            return getDefaultParameters(
                mode = TransactionMode.SEARCH,
                type = type
            )
        }


        fun getDefaultParameters(
            mode: TransactionMode = TransactionMode.STANDARD,
            type: TransactionType = TransactionType.DEPOSITS
        ): TransactionParameters {
            return TransactionParameters(
                mode = mode,
                type = type,
                searchQuery = "",
                sortOrder = SortOrder.DESC,
                limit = 100,
                offset = 0
            )
        }


    }


    val lowercasedSearchQuery: String
        get() = searchQuery.toLowerCase()


}