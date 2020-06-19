package com.stocksexchange.android.utils

import android.os.Bundle
import com.stocksexchange.android.R
import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.balance.BalanceContainerFragment
import com.stocksexchange.android.ui.balance.newArgs
import com.stocksexchange.android.ui.base.fragments.NavigationFragment
import com.stocksexchange.android.ui.currencymarketpreview.CurrencyMarketPreviewFragment
import com.stocksexchange.android.ui.currencymarketpreview.newArgs
import com.stocksexchange.api.model.rest.CurrencyMarket

class DashboardArgsCreator {


    fun getWithdrawalsScreenOpeningArgs(
        wasWithdrawalJustConfirmed: Boolean,
        wasWithdrawalJustCancelled: Boolean
    ) : DashboardArgs {
        return DashboardArgs(
            selectedBottomMenuItem = DashboardBottomMenuItem.BALANCE,
            startDestinationsArgsMap = mapOf(DashboardPage.BALANCE to BalanceContainerFragment.newArgs(
                wasWithdrawalJustConfirmed = wasWithdrawalJustConfirmed,
                wasWithdrawalJustCancelled = wasWithdrawalJustCancelled,
                selectedTab = BalanceTab.WITHDRAWALS
            ))
        )
    }


    fun getCurrencyMarketPreviewScreenOpeningArgs(
        currencyMarket: CurrencyMarket
    ) : DashboardArgs {
        return DashboardArgs(
            selectedBottomMenuItem = DashboardBottomMenuItem.TRADE,
            startDestinationsArgsMap = mapOf(
                DashboardPage.TRADE to NavigationFragment.newDeepLinkArgs(
                    deepLinkData = NavigationDeepLinkData(
                        destinationId = R.id.currencyMarketPreviewDest,
                        destinationArgs = CurrencyMarketPreviewFragment.newArgs(currencyMarket)
                    )
                )
            )
        )
    }


    fun getTradeScreenOpeningArgs(tradeArguments: Bundle): DashboardArgs {
        return DashboardArgs(
            selectedBottomMenuItem = DashboardBottomMenuItem.TRADE,
            startDestinationsArgsMap = mapOf(
                DashboardPage.TRADE to NavigationFragment.newDeepLinkArgs(
                    deepLinkData = NavigationDeepLinkData(
                        destinationId = R.id.tradeDest,
                        destinationArgs = tradeArguments
                    )
                )
            )
        )
    }


    fun getInboxScreenOpeningArgs(): DashboardArgs {
        return DashboardArgs(
            selectedBottomMenuItem = DashboardBottomMenuItem.TRADE,
            startDestinationsArgsMap = mapOf(
                DashboardPage.TRADE to NavigationFragment.newDeepLinkArgs(
                    deepLinkData = NavigationDeepLinkData(
                        destinationId = R.id.inboxDest
                    )
                )
            )
        )
    }


    fun getOpenAppArgs(): DashboardArgs {
        return DashboardArgs(
            selectedBottomMenuItem = DashboardBottomMenuItem.TRADE,
            startDestinationsArgsMap = mapOf()
        )
    }

}