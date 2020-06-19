package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.PopupMenuTheme
import com.stocksexchange.core.providers.ColorProvider

class PopupMenuThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<PopupMenuTheme>(colorProvider) {


    override fun getDeepTealTheme(): PopupMenuTheme {
        return PopupMenuTheme(
            backgroundColor = getColor(R.color.deepTealPopupMenuBackgroundColor),
            titleColor = getColor(R.color.deepTealPopupMenuTitleColor)
        )
    }


}