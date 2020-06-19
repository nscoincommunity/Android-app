package com.stocksexchange.android.model

import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.android.ui.currencymarketpreview.views.base.interfaces.BaseTradingListData

data class TradeData(
    override val highlightEndTimestamp: Long,
    val trade: Trade,
    val isSwipeMenuEnabled: Boolean = false,
    val isProgressBarVisible: Boolean = false
) : BaseTradingListData