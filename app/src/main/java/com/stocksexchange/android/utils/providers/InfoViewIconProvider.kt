package com.stocksexchange.android.utils.providers

import android.content.Context
import android.graphics.drawable.Drawable
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.model.*
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.theming.model.Themes
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.api.model.rest.WalletMode
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import com.stocksexchange.api.model.rest.parameters.WalletParameters
import com.stocksexchange.core.utils.extensions.getColoredCompatDrawable
import com.stocksexchange.core.utils.extensions.getCompatDrawable

class InfoViewIconProvider(
    private val context: Context,
    private val sessionManager: SessionManager
) {


    fun getCurrencyMarketsIcon(parameters: CurrencyMarketParameters): Drawable? {
        return getDrawable(when(parameters.currencyMarketType) {
            CurrencyMarketType.NORMAL -> R.mipmap.ic_currency_pairs_stub
            CurrencyMarketType.FAVORITES -> R.drawable.ic_star
            CurrencyMarketType.SEARCH -> R.mipmap.ic_search_stub
        }) {
            isDeepTealTheme() && (parameters.currencyMarketType != CurrencyMarketType.FAVORITES)
        }
    }


    fun getWalletsIcon(parameters: WalletParameters): Drawable? {
        return getDrawable(when(parameters.mode) {
            WalletMode.STANDARD -> R.mipmap.ic_wallet_stub
            WalletMode.SEARCH -> R.mipmap.ic_search_stub
        })
    }


    fun getTransactionCreation(): Drawable? {
        return getDrawable(R.drawable.ic_transaction_creation)
    }


    fun getWithdrawalIcon(): Drawable? {
        return getDrawable(R.mipmap.ic_withdrawals_stub)
    }


    fun getTwitterNewsIcon(): Drawable? {
        return getDrawable(R.drawable.ic_empty_news_report)
    }


    fun getBlogNewsIcon(): Drawable? {
        return getDrawable(R.drawable.ic_empty_news_report)
    }


    fun getOrdersIcon(parameters: OrderParameters): Drawable? {
        return getDrawable(when(parameters.mode) {
            OrderMode.STANDARD -> when(parameters.lifecycleType) {
                OrderLifecycleType.ACTIVE -> R.mipmap.ic_active_orders_stub
                OrderLifecycleType.COMPLETED -> R.mipmap.ic_completed_orders_stub
                OrderLifecycleType.CANCELLED -> R.mipmap.ic_cancelled_orders_stub
            }

            OrderMode.SEARCH -> R.mipmap.ic_search_stub
        })
    }


    fun getTransactionsIcon(parameters: TransactionParameters): Drawable? {
        return getDrawable(when(parameters.mode) {

            TransactionMode.STANDARD -> {
                when(parameters.type) {
                    TransactionType.DEPOSITS -> R.mipmap.ic_deposits_stub
                    TransactionType.WITHDRAWALS -> R.mipmap.ic_withdrawals_stub
                }
            }

            TransactionMode.SEARCH -> R.mipmap.ic_search_stub

        })
    }


    fun getChartViewIcon(): Drawable? {
        return getDrawable(R.mipmap.ic_chart_stub)
    }


    fun getInboxIcon(): Drawable? {
        return getDrawable(R.drawable.ic_empty_inbox_report)
    }


    fun getAlertPriceIcon(): Drawable? {
        return getDrawable(R.drawable.ic_empty_inbox_report)
    }


    fun getMarketDetailsViewIcon(): Drawable? {
        return getDrawable(R.mipmap.ic_information_stub)
    }


    fun getOrderbookViewIcon(): Drawable? {
        return getDrawable(R.mipmap.ic_orderbook_stub)
    }


    fun getTradeHistoryViewIcon(): Drawable? {
        return getDrawable(R.mipmap.ic_active_orders_stub)
    }


    fun getUserActiveOrdersViewIcon(): Drawable? {
        return getTradeHistoryViewIcon()
    }


    fun getUserHistoryOrdersViewIcon(): Drawable? {
        return getDrawable(R.mipmap.ic_completed_orders_stub)
    }


    private fun getCurrentTheme(): Theme = sessionManager.getSettings().theme


    private fun isDeepTealTheme(): Boolean = (getCurrentTheme().id == Themes.DEEP_TEAL.id)


    private fun getDrawable(
        drawableId: Int,
        shouldFetchWithoutColor: (() -> Boolean) = { isDeepTealTheme() }
    ): Drawable? {
        return if(shouldFetchWithoutColor()) {
            context.getCompatDrawable(drawableId)
        } else {
            context.getColoredCompatDrawable(
                drawableId,
                getCurrentTheme().generalTheme.infoViewColor
            )
        }
    }


}