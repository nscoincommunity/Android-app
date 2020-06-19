package com.stocksexchange.android.events

import com.stocksexchange.api.model.rest.Inbox
import com.stocksexchange.android.utils.helpers.tag

class InboxNewMessageEvent private constructor(
    type: Int,
    sourceTag: String,
    val inbox: Inbox
) : BaseEvent<Void?>(type, null, sourceTag) {


    companion object {

        fun init(inbox: Inbox, source: Any): InboxNewMessageEvent {
            return InboxNewMessageEvent(TYPE_SINGLE_ITEM, tag(source), inbox)
        }

    }


}