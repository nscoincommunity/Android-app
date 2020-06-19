package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.DialogTheme
import com.stocksexchange.core.providers.ColorProvider

class DialogThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<DialogTheme>(colorProvider) {


    override fun getDeepTealTheme(): DialogTheme {
        return DialogTheme(
            backgroundColor = getColor(R.color.deepTealDialogBackgroundColor),
            titleColor = getColor(R.color.deepTealDialogTitleColor),
            textColor = getColor(R.color.deepTealDialogTextColor),
            widgetColor = getColor(R.color.deepTealDialogWidgetColor),
            buttonColor = getColor(R.color.deepTealDialogButtonColor)
        )
    }


}