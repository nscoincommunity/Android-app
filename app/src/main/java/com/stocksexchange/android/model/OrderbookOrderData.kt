package com.stocksexchange.android.model

import com.stocksexchange.api.model.rest.OrderbookOrder
import com.stocksexchange.android.ui.currencymarketpreview.views.base.interfaces.BaseTradingListData

data class OrderbookOrderData(
    val type: OrderbookOrderType,
    val order: OrderbookOrder,
    val volumeLevel: Int,
    override val highlightEndTimestamp: Long
) : BaseTradingListData {


    companion object {

        const val VOLUME_LEVEL_MAX_VALUE = 10000

    }


}