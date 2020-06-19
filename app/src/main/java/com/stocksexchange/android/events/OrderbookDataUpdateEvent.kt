package com.stocksexchange.android.events

import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.api.model.rest.OrderbookOrder
import com.stocksexchange.android.model.DataActionItem
import com.stocksexchange.android.utils.helpers.tag

class OrderbookDataUpdateEvent private constructor(
    attachment: Orderbook,
    sourceTag: String,
    onConsumeListener: OnConsumeListener?,
    val dataActionItems: List<DataActionItem<OrderbookOrder>>
) : BaseEvent<Orderbook>(
    type = TYPE_MULTIPLE_ITEMS,
    attachment = attachment,
    sourceTag = sourceTag,
    onConsumeListener = onConsumeListener
) {


    companion object {

        fun newInstance(newData: Orderbook, dataActionItems: List<DataActionItem<OrderbookOrder>>,
                        source: Any, onConsumeListener: OnConsumeListener?): OrderbookDataUpdateEvent {
            return OrderbookDataUpdateEvent(newData, tag(source), onConsumeListener, dataActionItems)
        }

    }


}