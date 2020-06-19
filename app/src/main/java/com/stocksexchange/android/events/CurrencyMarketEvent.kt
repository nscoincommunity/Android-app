package com.stocksexchange.android.events

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.android.utils.helpers.tag

class CurrencyMarketEvent private constructor(
    type: Int,
    attachment: CurrencyMarket,
    sourceTag: String,
    consumerCount: Int,
    val action: Action
) : BaseEvent<CurrencyMarket>(type, attachment, sourceTag, consumerCount) {


    companion object {


        fun update(currencyMarket: CurrencyMarket, source: Any,
                   consumerCount: Int = 0): CurrencyMarketEvent {
            return init(currencyMarket, source, Action.UPDATED, consumerCount)
        }


        fun favorite(currencyMarket: CurrencyMarket, source: Any,
                     consumerCount: Int = 0): CurrencyMarketEvent {
            return init(currencyMarket, source, Action.FAVORITED, consumerCount)
        }


        fun unfavorite(currencyMarket: CurrencyMarket, source: Any,
                       consumerCount: Int = 0): CurrencyMarketEvent {
            return init(currencyMarket, source, Action.UNFAVORITED, consumerCount)
        }


        private fun init(currencyMarket: CurrencyMarket, source: Any, action: Action,
                         consumerCount: Int = 0): CurrencyMarketEvent {
            return CurrencyMarketEvent(
                TYPE_SINGLE_ITEM,
                currencyMarket,
                tag(source),
                consumerCount,
                action
            )
        }


    }


    enum class Action {

        UPDATED,
        FAVORITED,
        UNFAVORITED

    }


}