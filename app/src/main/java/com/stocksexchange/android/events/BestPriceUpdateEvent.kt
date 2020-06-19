package com.stocksexchange.android.events

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.android.utils.helpers.tag

class BestPriceUpdateEvent private constructor(
    type: Int,
    attachment: CurrencyMarket,
    sourceTag: String
) : BaseEvent<CurrencyMarket>(type, attachment, sourceTag) {


    companion object {

        fun init(currencyMarket: CurrencyMarket, source: Any): BestPriceUpdateEvent {
            return BestPriceUpdateEvent(TYPE_SINGLE_ITEM, currencyMarket, tag(source))
        }

    }


}