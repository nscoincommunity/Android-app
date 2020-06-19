package com.stocksexchange.android.model

import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DashboardArgs(
    val selectedBottomMenuItem: DashboardBottomMenuItem = DashboardBottomMenuItem.TRADE,
    val startDestinationsArgsMap: Map<DashboardPage, Bundle> = mapOf()
) : Parcelable