package com.stocksexchange.android.events

import com.stocksexchange.android.utils.helpers.tag

class OrderbookDataReloadEvent private constructor(
    sourceTag: String
) : BaseEvent<Void?>(TYPE_INVALID, null, sourceTag) {


    companion object {

        fun newInstance(source: Any): OrderbookDataReloadEvent {
            return OrderbookDataReloadEvent(tag(source))
        }

    }


}