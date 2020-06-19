package com.stocksexchange.android.events

import com.stocksexchange.android.model.PerformedCurrencyMarketActions
import com.stocksexchange.android.utils.helpers.tag

class PerformedCurrencyMarketActionsEvent private constructor(
    type: Int,
    attachment: PerformedCurrencyMarketActions,
    sourceTag: String,
    onConsumeListener: OnConsumeListener?
) : BaseEvent<PerformedCurrencyMarketActions>(
    type = type,
    attachment = attachment,
    sourceTag = sourceTag,
    onConsumeListener = onConsumeListener
) {


    companion object {

        fun init(performedActions: PerformedCurrencyMarketActions, source: Any,
                 onConsumeListener: OnConsumeListener? = null): PerformedCurrencyMarketActionsEvent {
            return PerformedCurrencyMarketActionsEvent(
                TYPE_MULTIPLE_ITEMS,
                performedActions,
                tag(source),
                onConsumeListener
            )
        }

    }


}