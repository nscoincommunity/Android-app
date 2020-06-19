package com.stocksexchange.android.theming.model

import java.io.Serializable

data class CardViewTheme(
    val backgroundColor: Int,
    val primaryTextColor: Int,
    val primaryDarkTextColor: Int,
    val buttonEnabledBackgroundColor: Int,
    val buttonDisabledBackgroundColor: Int,
    val buttonEnabledTextColor: Int,
    val buttonDisabledTextColor: Int
) : Serializable {


    companion object {

        val STUB = CardViewTheme(
            backgroundColor = -1,
            primaryTextColor = -1,
            primaryDarkTextColor = -1,
            buttonEnabledBackgroundColor = -1,
            buttonDisabledBackgroundColor = -1,
            buttonEnabledTextColor = -1,
            buttonDisabledTextColor = -1
        )

    }


}