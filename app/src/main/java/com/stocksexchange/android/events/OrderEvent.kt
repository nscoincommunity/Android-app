package com.stocksexchange.android.events

import com.stocksexchange.android.model.OrderData
import com.stocksexchange.android.utils.helpers.tag

class OrderEvent private constructor(
    type: Int,
    attachment: OrderData,
    sourceTag: String,
    consumerCount: Int,
    val action: Action
) : BaseEvent<OrderData>(type, attachment, sourceTag, consumerCount) {


    companion object {


        fun updateFilledAmount(orderData: OrderData, source: Any,
                               consumerCount: Int = 0): OrderEvent {
            return init(orderData, source, Action.FILLED_AMOUNT_UPDATED, consumerCount)
        }


        fun updateStatus(orderData: OrderData, source: Any,
                         consumerCount: Int = 0): OrderEvent {
            return init(orderData, source, Action.STATUS_UPDATED, consumerCount)
        }


        private fun init(orderData: OrderData, source: Any,
                         action: Action, consumerCount: Int = 0): OrderEvent {
            return OrderEvent(
                TYPE_SINGLE_ITEM,
                orderData,
                tag(source),
                consumerCount,
                action
            )
        }


    }


    enum class Action {

        FILLED_AMOUNT_UPDATED,
        STATUS_UPDATED

    }


}