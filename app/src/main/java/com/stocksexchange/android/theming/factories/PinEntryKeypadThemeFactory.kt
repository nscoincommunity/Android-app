package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.PinEntryKeypadTheme
import com.stocksexchange.core.providers.ColorProvider

class PinEntryKeypadThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<PinEntryKeypadTheme>(colorProvider) {


    override fun getDeepTealTheme(): PinEntryKeypadTheme {
        return PinEntryKeypadTheme(
            digitButtonTextColor = getColor(R.color.deepTealPinEntryKeypadDigitButtonTextColor),
            enabledButtonBackgroundColor = getColor(R.color.deepTealPinEntryKeypadEnabledButtonBackgroundColor),
            disabledButtonBackgroundColor = getColor(R.color.deepTealPinEntryKeypadDisabledButtonBackgroundColor),
            enabledFingerprintButtonForegroundColor = getColor(R.color.deepTealPinEntryKeypadEnabledFingerprintButtonForegroundColor),
            enabledDeleteButtonForegroundColor = getColor(R.color.deepTealPinEntryKeypadEnabledDeleteButtonForegroundColor),
            disabledActionButtonForegroundColor = getColor(R.color.deepTealPinEntryKeypadDisabledActionButtonForegroundColor)
        )
    }


}