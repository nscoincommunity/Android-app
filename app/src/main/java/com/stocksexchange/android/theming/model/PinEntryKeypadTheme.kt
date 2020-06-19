package com.stocksexchange.android.theming.model

import java.io.Serializable

data class PinEntryKeypadTheme(
    val digitButtonTextColor: Int,
    val enabledButtonBackgroundColor: Int,
    val disabledButtonBackgroundColor: Int,
    val enabledFingerprintButtonForegroundColor: Int,
    val enabledDeleteButtonForegroundColor: Int,
    val disabledActionButtonForegroundColor: Int
) : Serializable {


    companion object {

        val STUB = PinEntryKeypadTheme(
            digitButtonTextColor = -1,
            enabledButtonBackgroundColor = -1,
            disabledButtonBackgroundColor = -1,
            enabledFingerprintButtonForegroundColor = -1,
            enabledDeleteButtonForegroundColor = -1,
            disabledActionButtonForegroundColor = -1
        )

    }


}