package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import com.stocksexchange.api.model.rest.WalletFilter
import com.stocksexchange.api.model.rest.SortOrder
import com.stocksexchange.api.model.rest.WalletBalanceType
import com.stocksexchange.api.model.rest.WalletMode
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WalletParameters(
    val mode: WalletMode,
    val emptyWalletsFilter: WalletFilter,
    val searchQuery: String,
    val sortOrder: SortOrder,
    val sortColumn: WalletBalanceType
) : Parcelable {


    companion object {


        fun getStandardParameters(): WalletParameters {
            return getDefaultParameters(WalletMode.STANDARD)
        }


        fun getSearchParameters(): WalletParameters {
            return getDefaultParameters(WalletMode.SEARCH)
        }


        fun getDefaultParameters(mode: WalletMode = WalletMode.STANDARD): WalletParameters {
            return WalletParameters(
                mode = mode,
                emptyWalletsFilter = WalletFilter.ANY_WALLET,
                searchQuery = "",
                sortOrder = SortOrder.DESC,
                sortColumn = WalletBalanceType.CURRENT
            )
        }


    }


    val lowercasedSearchQuery: String
        get() = searchQuery.toLowerCase()


}