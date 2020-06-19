package com.stocksexchange.android.model

import android.os.Bundle
import android.os.Parcelable
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChartHighlight(
    val x: Float,
    val dataSetIndex: Int,
    var key: String? = null,
    var bundle: Bundle? = null
) : Parcelable {


    fun updateKeyAndBundle(key: String, bundle: Bundle) {
        this.key = key
        this.bundle = bundle
    }


    fun toHighlight(): Highlight {
        return Highlight(x, dataSetIndex, -1)
    }


    fun hasKey(): Boolean {
        return (key != null)
    }


    fun hasBundle(): Boolean {
        return (bundle != null)
    }


}