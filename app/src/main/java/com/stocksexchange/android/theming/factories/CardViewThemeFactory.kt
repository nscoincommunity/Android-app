package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.CardViewTheme
import com.stocksexchange.core.providers.ColorProvider

class CardViewThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<CardViewTheme>(colorProvider) {


    override fun getDeepTealTheme(): CardViewTheme {
        return CardViewTheme(
            backgroundColor = getColor(R.color.deepTealCardViewBackgroundColor),
            primaryTextColor = getColor(R.color.deepTealCardViewPrimaryTextColor),
            primaryDarkTextColor = getColor(R.color.deepTealCardViewPrimaryDarkTextColor),
            buttonEnabledBackgroundColor = getColor(R.color.deepTealCardViewButtonEnabledBackgroundColor),
            buttonDisabledBackgroundColor = getColor(R.color.deepTealCardViewButtonDisabledBackgroundColor),
            buttonEnabledTextColor = getColor(R.color.deepTealCardViewButtonEnabledTextColor),
            buttonDisabledTextColor = getColor(R.color.deepTealCardViewButtonDisabledTextColor)
        )
    }


}