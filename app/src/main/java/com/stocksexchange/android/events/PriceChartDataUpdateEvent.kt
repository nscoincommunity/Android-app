package com.stocksexchange.android.events

import com.stocksexchange.api.model.rest.CandleStick
import com.stocksexchange.android.model.DataActionItem
import com.stocksexchange.android.utils.helpers.tag

class PriceChartDataUpdateEvent private constructor(
    attachment: List<CandleStick>,
    sourceTag: String,
    onConsumeListener: OnConsumeListener?,
    val dataActionItems: List<DataActionItem<CandleStick>>
) : BaseEvent<List<CandleStick>>(
    type = TYPE_MULTIPLE_ITEMS,
    attachment = attachment,
    sourceTag = sourceTag,
    onConsumeListener = onConsumeListener
) {


    companion object {

        fun newInstance(newData: List<CandleStick>, dataActionItems: List<DataActionItem<CandleStick>>,
                        source: Any, onConsumeListener: OnConsumeListener?): PriceChartDataUpdateEvent {
            return PriceChartDataUpdateEvent(newData, tag(source), onConsumeListener, dataActionItems)
        }

    }


}