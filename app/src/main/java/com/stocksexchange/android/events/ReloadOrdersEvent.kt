package com.stocksexchange.android.events

import com.stocksexchange.android.utils.helpers.tag


class ReloadOrdersEvent private constructor(
    type: Int,
    sourceTag: String,
    val action: Action
) : BaseEvent<Void?>(type, null, sourceTag) {


    companion object {


        fun reloadOrders(source: Any): ReloadOrdersEvent {
            return init(source, Action.ORDER_ACTIVE)
        }


        private fun init(source: Any, action: Action): ReloadOrdersEvent {
            return ReloadOrdersEvent(
                TYPE_SINGLE_ITEM,
                tag(source),
                action
            )
        }


    }


    enum class Action {

        ORDER_ACTIVE

    }


}