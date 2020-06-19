package com.stocksexchange.android.events

import com.stocksexchange.android.utils.helpers.tag

class InboxCountItemChangeEvent private constructor(
    type: Int,
    sourceTag: String
) : BaseEvent<Void?>(type, null, sourceTag) {


    companion object {

        fun init(source: Any): InboxCountItemChangeEvent {
            return InboxCountItemChangeEvent(TYPE_SINGLE_ITEM, tag(source))
        }

    }


}