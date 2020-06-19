package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.DottedMapViewTheme
import com.stocksexchange.core.providers.ColorProvider

class DottedMapViewThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<DottedMapViewTheme>(colorProvider) {


    override fun getDeepTealTheme(): DottedMapViewTheme {
        return DottedMapViewTheme(
            titleColor = getColor(R.color.deepTealDottedMapViewTitleColor),
            textColor = getColor(R.color.deepTealDottedMapViewTextColor)
        )
    }


}