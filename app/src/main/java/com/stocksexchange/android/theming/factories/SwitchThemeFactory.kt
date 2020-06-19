package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.SwitchTheme
import com.stocksexchange.core.providers.ColorProvider

class SwitchThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<SwitchTheme>(colorProvider) {


    override fun getDeepTealTheme(): SwitchTheme {
        return SwitchTheme(
            pointerActivatedColor = getColor(R.color.deepTealSwitchPointerActivatedColor),
            pointerDeactivatedColor = getColor(R.color.deepTealSwitchPointerDeactivatedColor),
            backgroundActivatedColor = getColor(R.color.deepTealSwitchBackgroundActivatedColor),
            backgroundDeactivatedColor = getColor(R.color.deepTealSwitchBackgroundDeactivatedColor)
        )
    }


}