package com.stocksexchange.android.theming.model

import java.io.Serializable

data class SortPanelTheme(
    val backgroundColor: Int,
    val selectedTitleColor: Int,
    val unselectedTitleColor: Int
) : Serializable {


    companion object {

        val STUB = SortPanelTheme(
            backgroundColor = -1,
            selectedTitleColor = -1,
            unselectedTitleColor = -1
        )

    }


}