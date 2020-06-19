package com.stocksexchange.android.theming.model

import java.io.Serializable

data class PopupMenuTheme(
    val backgroundColor: Int,
    val titleColor: Int
) : Serializable {


    companion object {

        val STUB = PopupMenuTheme(
            backgroundColor = -1,
            titleColor = -1
        )

    }


}