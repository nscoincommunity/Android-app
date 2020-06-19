package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.SortPanelTheme
import com.stocksexchange.core.providers.ColorProvider

class SortPanelThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<SortPanelTheme>(colorProvider) {


    override fun getDeepTealTheme(): SortPanelTheme {
        return SortPanelTheme(
            backgroundColor = getColor(R.color.deepTealSortPanelBackgroundColor),
            selectedTitleColor = getColor(R.color.deepTealSortPanelSelectedTitleColor),
            unselectedTitleColor = getColor(R.color.deepTealSortPanelUnselectedTitleColor)
        )
    }

}