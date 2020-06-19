package com.stocksexchange.android.socket.handlers

import com.google.gson.Gson
import com.stocksexchange.api.model.rest.OrderStatus
import com.stocksexchange.android.socket.model.data.activeorders.ActiveOrderFillUpdateSocketData
import com.stocksexchange.core.utils.extensions.fromJson
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.data.repositories.orders.OrdersRepository
import com.stocksexchange.android.events.OrderEvent
import com.stocksexchange.android.model.OrderData
import com.stocksexchange.android.socket.handlers.base.BaseHandler
import com.stocksexchange.android.socket.model.enums.SocketEvent
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class UserActiveOrderFillsUpdateHandler(
    gson: Gson,
    private val ordersRepository: OrdersRepository,
    private val currencyMarketsRepository: CurrencyMarketsRepository
) : BaseHandler<ActiveOrderFillUpdateSocketData>(gson) {


    override suspend fun onSocketDataReceivedAsync(channel: String,
                                                   socketData: ActiveOrderFillUpdateSocketData) {
        val orderResult = ordersRepository.getOrder(socketData.orderId)

        if(orderResult.isErroneous()) {
            return
        }

        val originalOrder = orderResult.getSuccessfulResultValue()

        if(originalOrder.status !in listOf(OrderStatus.PROCESSING, OrderStatus.PENDING)) {
            return
        }

        val amountLeftToFill = socketData.baseCurrencyAmount
        val filledAmount = (originalOrder.initialAmount - amountLeftToFill)
        var updatedOrder = originalOrder

        // Adjusting the status (just in case)
        if(updatedOrder.status == OrderStatus.PROCESSING) {
            updatedOrder = updatedOrder.copy(statusStr = OrderStatus.PENDING.name)
        }

        // Adjusting the filled amount
        updatedOrder = updatedOrder.copy(filledAmount = filledAmount)

        ordersRepository.save(updatedOrder)

        val currencyMarketResult = currencyMarketsRepository.getCurrencyMarket(
            pairId = socketData.currencyPairId
        )

        if(currencyMarketResult.isErroneous()) {
            return
        }

        val currencyMarket = currencyMarketResult.getSuccessfulResultValue()
        val orderData = OrderData(updatedOrder, currencyMarket)

        if(EventBus.getDefault().hasSubscriberForEvent(OrderEvent::class.java)) {
            EventBus.getDefault().post(OrderEvent.updateFilledAmount(
                orderData = orderData,
                source = this
            ))
        }
    }


    override fun convertSocketData(socketDataJsonObj: JSONObject): ActiveOrderFillUpdateSocketData {
        return gson.fromJson(socketDataJsonObj.toString())
    }


    override fun getLoggingKey(): String = "onUserActiveOrderFillsUpdated"


    override fun getSocketEvent(): SocketEvent = SocketEvent.USER_ACTIVE_ORDER_FILLS_UPDATED


}