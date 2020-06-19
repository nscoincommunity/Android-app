package com.stocksexchange.android.socket.handlers

import com.google.gson.Gson
import com.stocksexchange.api.model.rest.Order
import com.stocksexchange.api.model.rest.OrderStatus
import com.stocksexchange.android.socket.model.data.activeorders.ActiveOrderStatusUpdateSocketData
import com.stocksexchange.core.utils.extensions.fromJson
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.data.repositories.orders.OrdersRepository
import com.stocksexchange.android.events.OrderEvent
import com.stocksexchange.android.model.OrderData
import com.stocksexchange.android.socket.handlers.base.BaseHandler
import com.stocksexchange.android.socket.model.enums.SocketEvent
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class UserActiveOrderStatusUpdateHandler(
    gson: Gson,
    private val ordersRepository: OrdersRepository,
    private val currencyMarketsRepository: CurrencyMarketsRepository
) : BaseHandler<ActiveOrderStatusUpdateSocketData>(gson) {


    override suspend fun onSocketDataReceivedAsync(channel: String,
                                                   socketData: ActiveOrderStatusUpdateSocketData) {
        val orderResult = ordersRepository.getOrder(socketData.orderId)

        if(orderResult.isErroneous()) {
            return
        }

        val originalOrder = orderResult.getSuccessfulResultValue()

        if((originalOrder.status !in listOf(OrderStatus.PROCESSING, OrderStatus.PENDING)) ||
            (originalOrder.statusStr == socketData.newStatusStr)) {
            return
        }

        val updatedOrder = Order.updateActiveOrderStatus(originalOrder, socketData.newStatusStr)

        ordersRepository.save(updatedOrder)

        val currencyMarketResult = currencyMarketsRepository.getCurrencyMarket(
            pairId = socketData.currencyPairId
        )

        if(currencyMarketResult.isErroneous()) {
            return
        }

        val orderData = OrderData(
            order = updatedOrder,
            currencyMarket = currencyMarketResult.getSuccessfulResultValue()
        )

        if(EventBus.getDefault().hasSubscriberForEvent(OrderEvent::class.java)) {
            EventBus.getDefault().post(OrderEvent.updateStatus(orderData, this))
        }
    }


    override fun convertSocketData(socketDataJsonObj: JSONObject): ActiveOrderStatusUpdateSocketData {
        return gson.fromJson(socketDataJsonObj.toString())
    }


    override fun getLoggingKey(): String = "onUserActiveOrderStatusUpdated"


    override fun getSocketEvent(): SocketEvent = SocketEvent.USER_ACTIVE_ORDER_STATUS_UPDATED


}