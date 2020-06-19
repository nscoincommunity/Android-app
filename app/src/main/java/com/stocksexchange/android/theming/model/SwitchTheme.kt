package com.stocksexchange.android.theming.model

import java.io.Serializable

data class SwitchTheme(
    val pointerActivatedColor: Int,
    val pointerDeactivatedColor: Int,
    val backgroundActivatedColor: Int,
    val backgroundDeactivatedColor: Int
) : Serializable {


    companion object {

        val STUB = SwitchTheme(
            pointerActivatedColor = -1,
            pointerDeactivatedColor = -1,
            backgroundActivatedColor = -1,
            backgroundDeactivatedColor = -1
        )

    }


}