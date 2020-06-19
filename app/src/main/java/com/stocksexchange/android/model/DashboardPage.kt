package com.stocksexchange.android.model

import androidx.annotation.NavigationRes
import com.stocksexchange.android.R

enum class DashboardPage(
    @NavigationRes val navigationGraphId: Int
) {


    BALANCE(
        navigationGraphId = R.navigation.navigation_dashboard_balance
    ),
    ORDERS(
        navigationGraphId = R.navigation.navigation_dashboard_orders
    ),
    TRADE(
        navigationGraphId = R.navigation.navigation_dashboard_trade
    ),
    NEWS(
        navigationGraphId = R.navigation.navigation_dashboard_news
    ),
    PROFILE(
        navigationGraphId = R.navigation.navigation_dashboard_profile
    )


}