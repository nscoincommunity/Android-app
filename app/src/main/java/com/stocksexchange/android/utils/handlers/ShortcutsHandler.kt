package com.stocksexchange.android.utils.handlers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.graphics.drawable.Icon
import com.stocksexchange.android.model.DashboardArgs
import com.stocksexchange.android.model.DashboardBottomMenuItem
import com.stocksexchange.android.model.Shortcut
import com.stocksexchange.android.ui.dashboard.DashboardActivity
import com.stocksexchange.android.ui.dashboard.newInstance
import com.stocksexchange.android.ui.splash.SplashActivity
import com.stocksexchange.android.ui.splash.newInstance
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.Constants
import com.stocksexchange.core.utils.extensions.getShortcutManager

class ShortcutsHandler(
    private val context: Context,
    private val stringProvider: StringProvider
) {


    @SuppressWarnings("NewApi")
    fun setupShortcuts() {
        if(!Constants.AT_LEAST_NOUGAT_V2) {
            return
        }

        val balanceShortcut = buildShortcut(Shortcut.BALANCE)
        val ordersShortcut = buildShortcut(Shortcut.ORDERS)
        val newsShortcut = buildShortcut(Shortcut.NEWS)

        context.getShortcutManager().dynamicShortcuts = listOf(
            balanceShortcut,
            ordersShortcut,
            newsShortcut
        )
    }


    @SuppressLint("NewApi")
    private fun buildShortcut(shortcut: Shortcut): ShortcutInfo {
        val intent = getShortcutWrapperIntent(shortcut)

        return ShortcutInfo.Builder(context, intent.action)
            .setShortLabel(stringProvider.getString(shortcut.shortLabelId))
            .setLongLabel(stringProvider.getString(shortcut.longLabelId))
            .setIcon(Icon.createWithResource(context, shortcut.iconResId))
            .setIntent(intent)
            .build()
    }


    private fun getShortcutWrapperIntent(shortcut: Shortcut): Intent {
        return SplashActivity.newInstance(
            context = context,
            shortcut = shortcut
        ).apply {
            action = shortcut.name
        }
    }


    fun getDestinationIntentForShortcut(context: Context, shortcut: Shortcut): Intent {
        return DashboardActivity.newInstance(
            context = context,
            dashboardArgs = DashboardArgs(
                selectedBottomMenuItem = when(shortcut) {
                    Shortcut.BALANCE -> DashboardBottomMenuItem.BALANCE
                    Shortcut.ORDERS -> DashboardBottomMenuItem.ORDERS
                    Shortcut.NEWS -> DashboardBottomMenuItem.NEWS
                }
            )
        )
    }


}