package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.SwitchOptionsViewTheme
import com.stocksexchange.core.providers.ColorProvider

class SwitchOptionsViewThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<SwitchOptionsViewTheme>(colorProvider) {


    override fun getDeepTealTheme(): SwitchOptionsViewTheme {
        return SwitchOptionsViewTheme(
            titleTextColor = getColor(R.color.deepTealSwitchOptionsViewTitleTextColor),
            switchColor = getColor(R.color.deepTealSwitchOptionsViewSwitchColor)
        )
    }


}