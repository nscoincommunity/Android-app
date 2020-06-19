package com.stocksexchange.android.theming.model

import java.io.Serializable

data class ButtonTheme(
    val contentColor: Int,
    val releasedStateBackgroundColor: Int,
    val pressedStateBackgroundColor: Int
) : Serializable {


    companion object {

        val STUB = ButtonTheme(
            contentColor = -1,
            releasedStateBackgroundColor = -1,
            pressedStateBackgroundColor = -1
        )

    }


}