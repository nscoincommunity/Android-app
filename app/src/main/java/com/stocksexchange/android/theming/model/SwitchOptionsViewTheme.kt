package com.stocksexchange.android.theming.model

import java.io.Serializable

data class SwitchOptionsViewTheme(
    val titleTextColor: Int,
    val switchColor: Int
) : Serializable {


    companion object {

        val STUB = SwitchOptionsViewTheme(
            titleTextColor = -1,
            switchColor = -1
        )

    }


}