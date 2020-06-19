package com.stocksexchange.android.model

import com.stocksexchange.android.theming.model.Theme
import java.io.Serializable

data class ThemeData(
    val theme: Theme,
    var isSelected: Boolean = false
) : Serializable