package com.stocksexchange.android.theming.model

import java.io.Serializable

data class DialogTheme(
    val backgroundColor: Int,
    val titleColor: Int,
    val textColor: Int,
    val widgetColor: Int,
    val buttonColor: Int
) : Serializable {


    companion object {

        val STUB = DialogTheme(
            backgroundColor = -1,
            titleColor = -1,
            textColor = -1,
            widgetColor = -1,
            buttonColor = -1
        )

    }


}