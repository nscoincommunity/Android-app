package com.stocksexchange.android.events

import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.android.model.DataActionItem
import com.stocksexchange.android.utils.helpers.tag

class TradeHistoryDataUpdateEvent private constructor(
    attachment: List<Trade>,
    sourceTag: String,
    onConsumeListener: OnConsumeListener?,
    val dataActionItems: List<DataActionItem<Trade>>
) : BaseEvent<List<Trade>>(
    type = TYPE_MULTIPLE_ITEMS,
    attachment = attachment,
    sourceTag = sourceTag,
    onConsumeListener = onConsumeListener
) {


    companion object {

        fun newInstance(newData: List<Trade>, dataActionItems: List<DataActionItem<Trade>>,
                        source: Any, onConsumeListener: OnConsumeListener?): TradeHistoryDataUpdateEvent {
            return TradeHistoryDataUpdateEvent(newData, tag(source), onConsumeListener, dataActionItems)
        }

    }


}