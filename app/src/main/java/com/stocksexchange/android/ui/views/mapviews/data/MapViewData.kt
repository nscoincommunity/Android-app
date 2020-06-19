package com.stocksexchange.android.ui.views.mapviews.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MapViewData(
    val title: CharSequence,
    val subtitle: CharSequence
) : Parcelable