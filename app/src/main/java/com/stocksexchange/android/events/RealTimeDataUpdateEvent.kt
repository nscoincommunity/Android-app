package com.stocksexchange.android.events

import com.stocksexchange.android.utils.helpers.tag

/**
 * An event that denotes that real-time data needs to
 * updated (refreshed).
 */
class RealTimeDataUpdateEvent private constructor(
    sourceTag: String,
    consumerCount: Int
) : BaseEvent<Void?>(TYPE_INVALID, null, sourceTag, consumerCount) {


    companion object {

        fun newInstance(source: Any, consumerCount: Int): RealTimeDataUpdateEvent {
            return RealTimeDataUpdateEvent(tag(source), consumerCount)
        }

    }


}