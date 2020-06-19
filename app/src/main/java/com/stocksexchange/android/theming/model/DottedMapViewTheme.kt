package com.stocksexchange.android.theming.model

import java.io.Serializable

data class DottedMapViewTheme(
    val titleColor: Int,
    val textColor: Int
) : Serializable {


    companion object {

        val STUB = DottedMapViewTheme(
            titleColor = -1,
            textColor = -1
        )

    }


}