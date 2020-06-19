package com.stocksexchange.android.model

import java.io.Serializable

data class Setting(
    var id: SettingId,
    var isEnabled: Boolean = true,
    var isCheckable: Boolean = false,
    var isChecked: Boolean = false,
    var title: String = "",
    var description: String = "",
    var customDescriptionColor: Boolean = false,
    var tag: Any? = null
) : Serializable {


    val hasDescription: Boolean
        get() = description.isNotBlank()


}