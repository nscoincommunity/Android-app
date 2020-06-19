package com.stocksexchange.android.theming.model

import java.io.Serializable

data class GeneralTheme(
    val primaryColor: Int,
    val primaryLightColor: Int,
    val primaryDarkColor: Int,
    val primaryTextColor: Int,
    val primaryDarkTextColor: Int,
    val secondaryTextColor: Int,
    val secondaryDarkTextColor: Int,
    val accentColor: Int,
    val darkAccentColor: Int,
    val contentContainerColor: Int,
    val contentContainerLightColor: Int,
    val contentContainerTextColor: Int,
    val tabIndicatorColor: Int,
    val infoViewColor: Int,
    val progressBarColor: Int,
    val gradientStartColor: Int,
    val gradientEndColor: Int,
    val linkReleasedStateBackgroundColor: Int,
    val linkPressedStateBackgroundColor: Int,
    val emailCardViewColor: Int,
    val emailFooterButtonColor: Int,
    val settingsBackgroundColor: Int,
    val tradeUserBalanceViewBackgroundColor: Int,
    val tradeFormViewBackgroundColor: Int,
    val marketPreviewHeadlineBackgroundColor: Int,
    val dashboardBottomNavBackgroundColor: Int
) : Serializable {


    companion object {

        val STUB = GeneralTheme(
            primaryColor = -1,
            primaryLightColor = -1,
            primaryDarkColor = -1,
            primaryTextColor = -1,
            primaryDarkTextColor = -1,
            secondaryTextColor = -1,
            secondaryDarkTextColor = -1,
            accentColor = -1,
            darkAccentColor = -1,
            contentContainerColor = -1,
            contentContainerLightColor = -1,
            contentContainerTextColor = -1,
            tabIndicatorColor = -1,
            infoViewColor = -1,
            progressBarColor = -1,
            gradientStartColor = -1,
            gradientEndColor = -1,
            linkReleasedStateBackgroundColor = -1,
            linkPressedStateBackgroundColor = -1,
            emailCardViewColor = -1,
            emailFooterButtonColor = -1,
            settingsBackgroundColor = -1,
            tradeUserBalanceViewBackgroundColor = -1,
            tradeFormViewBackgroundColor = -1,
            marketPreviewHeadlineBackgroundColor = -1,
            dashboardBottomNavBackgroundColor = -1
        )

    }


}