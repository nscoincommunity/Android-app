package com.stocksexchange.android.socket.handlers

import com.google.gson.Gson
import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.api.model.rest.OrderbookOrder
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters
import com.stocksexchange.api.model.rest.SortOrder
import com.stocksexchange.android.socket.model.data.OrderbookOrderUpdateSocketData
import com.stocksexchange.core.utils.extensions.fromJson
import com.stocksexchange.android.data.repositories.orderbooks.OrderbooksRepository
import com.stocksexchange.android.events.OrderbookDataUpdateEvent
import com.stocksexchange.android.model.DataActionItem
import com.stocksexchange.android.model.OrderbookOrderType
import com.stocksexchange.android.utils.extensions.binarySearch
import com.stocksexchange.android.socket.handlers.base.BaseHandler
import com.stocksexchange.android.socket.model.SocketChannels
import com.stocksexchange.android.socket.model.enums.SocketEvent
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class OrderbookOrderUpdateHandler(
    gson: Gson,
    private val orderbooksRepository: OrderbooksRepository
) : BaseHandler<OrderbookOrderUpdateSocketData>(gson) {


    private val mOrderbookOrderActionItems: MutableList<DataActionItem<OrderbookOrder>> = mutableListOf()




    override suspend fun onSocketDataReceivedAsync(channel: String,
                                                   socketData: OrderbookOrderUpdateSocketData) {
        val orderbookParameters = OrderbookParameters.getDefaultParameters(socketData.currencyPairId)
        val orderbookResult = orderbooksRepository.get(orderbookParameters)

        if(orderbookResult.isErroneous()) {
            return
        }

        val orderbook = orderbookResult.getSuccessfulResultValue()
        val buyOrders = orderbook.buyOrders.toMutableList()
        val sellOrders = orderbook.sellOrders.toMutableList()
        val newOrder = socketData.toOrderbookOrder()
        val isRemoval = (newOrder.amount == 0.0)
        val type = if(SocketChannels.isOrderbookBidOrdersUpdatesChannel(channel)) {
            OrderbookOrderType.BID
        } else if(SocketChannels.isOrderbookAskOrdersUpdatesChannel(channel)) {
            OrderbookOrderType.ASK
        } else {
            return
        }
        val orders = when(type) {
            OrderbookOrderType.BID -> buyOrders
            OrderbookOrderType.ASK -> sellOrders
        }
        var isOperationPerformed = false
        var orderbookDataActionItem: DataActionItem<OrderbookOrder>? = null

        orders.binarySearch(
            sortOrder = when(type) {
                OrderbookOrderType.BID -> SortOrder.DESC
                OrderbookOrderType.ASK -> SortOrder.ASC
            },
            onPresent = {
                if(isRemoval) {
                    orderbookDataActionItem = DataActionItem.remove(newOrder)
                    orders.removeAt(it)
                } else {
                    orderbookDataActionItem = DataActionItem.update(newOrder)
                    orders[it] = newOrder
                }

                isOperationPerformed = true
            },
            onAbsent = {
                if(!isRemoval) {
                    orderbookDataActionItem = DataActionItem.insert(newOrder)
                    orders.add(it, newOrder)

                    isOperationPerformed = true
                }
            },
            compare = {
                when {
                    (it.price < newOrder.price) -> -1
                    (it.price > newOrder.price) -> 1
                    else -> 0
                }
            }
        )

        if(!isOperationPerformed) {
            return
        }

        val newOrderbook = Orderbook(buyOrders, sellOrders)

        orderbooksRepository.save(orderbookParameters, newOrderbook)

        mOrderbookOrderActionItems.add(orderbookDataActionItem!!)

        EventBus.getDefault().postSticky(OrderbookDataUpdateEvent.newInstance(
            newData = newOrderbook,
            dataActionItems = mOrderbookOrderActionItems,
            source = this,
            onConsumeListener = this
        ))
    }


    override fun onConsumed() {
        if(mOrderbookOrderActionItems.isNotEmpty()) {
            mOrderbookOrderActionItems.clear()
        }
    }


    override fun convertSocketData(socketDataJsonObj: JSONObject): OrderbookOrderUpdateSocketData {
        return gson.fromJson(socketDataJsonObj.toString())
    }


    override fun getLoggingKey(): String = "onOrderbookOrderUpdated"


    override fun getSocketEvent(): SocketEvent = SocketEvent.ORDERBOOK_ORDER_UPDATED


}