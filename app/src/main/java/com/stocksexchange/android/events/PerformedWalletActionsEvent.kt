package com.stocksexchange.android.events

import com.stocksexchange.android.model.PerformedWalletActions
import com.stocksexchange.android.utils.helpers.tag

class PerformedWalletActionsEvent private constructor(
    type: Int,
    attachment: PerformedWalletActions,
    sourceTag: String
) : BaseEvent<PerformedWalletActions>(
    type = type,
    attachment = attachment,
    sourceTag = sourceTag
) {


    companion object {

        fun init(performedActions: PerformedWalletActions, source: Any): PerformedWalletActionsEvent {
            return PerformedWalletActionsEvent(TYPE_MULTIPLE_ITEMS, performedActions, tag(source))
        }

    }


}