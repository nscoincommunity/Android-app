package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.ButtonTheme
import com.stocksexchange.core.providers.ColorProvider

class ButtonThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<ButtonTheme>(colorProvider) {


    override fun getDeepTealTheme(): ButtonTheme {
        return ButtonTheme(
            contentColor = getColor(R.color.deepTealButtonContentColor),
            releasedStateBackgroundColor = getColor(R.color.deepTealButtonReleasedStateBackgroundColor),
            pressedStateBackgroundColor = getColor(R.color.deepTealButtonPressedStateBackgroundColor)
        )
    }


}