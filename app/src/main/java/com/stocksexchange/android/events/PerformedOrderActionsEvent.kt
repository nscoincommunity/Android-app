package com.stocksexchange.android.events

import com.stocksexchange.android.model.PerformedOrderActions
import com.stocksexchange.android.utils.helpers.tag

class PerformedOrderActionsEvent private constructor(
    type: Int,
    attachment: PerformedOrderActions,
    sourceTag: String
) : BaseEvent<PerformedOrderActions>(type, attachment, sourceTag) {


    companion object {

        fun init(performedActions: PerformedOrderActions, source: Any): PerformedOrderActionsEvent {
            return PerformedOrderActionsEvent(TYPE_MULTIPLE_ITEMS, performedActions, tag(source))
        }

    }


}