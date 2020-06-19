package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.GeneralTheme
import com.stocksexchange.core.providers.ColorProvider

class GeneralThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<GeneralTheme>(colorProvider) {


    override fun getDeepTealTheme(): GeneralTheme {
        return GeneralTheme(
            primaryColor = getColor(R.color.deepTealPrimaryColor),
            primaryLightColor = getColor(R.color.deepTealPrimaryLightColor),
            primaryDarkColor = getColor(R.color.deepTealPrimaryDarkColor),
            primaryTextColor = getColor(R.color.deepTealPrimaryTextColor),
            primaryDarkTextColor = getColor(R.color.deepTealPrimaryDarkTextColor),
            secondaryTextColor = getColor(R.color.deepTealSecondaryTextColor),
            secondaryDarkTextColor = getColor(R.color.deepTealSecondaryDarkTextColor),
            accentColor = getColor(R.color.deepTealAccentColor),
            darkAccentColor = getColor(R.color.deepTealDarkAccentColor),
            contentContainerColor = getColor(R.color.deepTealContentContainerColor),
            contentContainerLightColor = getColor(R.color.deepTealContentContainerLightColor),
            contentContainerTextColor = getColor(R.color.deepTealContentContainerTextColor),
            tabIndicatorColor = getColor(R.color.deepTealTabIndicatorColor),
            infoViewColor = getColor(R.color.deepTealInfoViewColor),
            progressBarColor = getColor(R.color.deepTealProgressBarColor),
            gradientStartColor = getColor(R.color.deepTealGradientStartColor),
            gradientEndColor = getColor(R.color.deepTealGradientEndColor),
            linkReleasedStateBackgroundColor = getColor(R.color.deepTealLinkReleasedStateBackgroundColor),
            linkPressedStateBackgroundColor = getColor(R.color.deepTealLinkPressedStateBackgroundColor),
            emailCardViewColor = getColor(R.color.deepTealEmailCardViewColor),
            emailFooterButtonColor = getColor(R.color.deepTealEmailFooterButtonColor),
            settingsBackgroundColor = getColor(R.color.deepTealSettingsBackgroundColor),
            tradeUserBalanceViewBackgroundColor = getColor(R.color.deepTealTradeUserBalanceViewBackgroundColor),
            tradeFormViewBackgroundColor = getColor(R.color.deepTealTradeFormViewBackgroundColor),
            marketPreviewHeadlineBackgroundColor = getColor(R.color.deepTealMarketPreviewHeadlineBackgroundColor),
            dashboardBottomNavBackgroundColor = getColor(R.color.deepTealDashboardBottomNavBackgroundColor)
        )
    }


}