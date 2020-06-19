package com.stocksexchange.android.socket.handlers

import com.google.gson.Gson
import com.stocksexchange.api.model.rest.Trade
import com.stocksexchange.api.model.rest.parameters.TradeHistoryParameters
import com.stocksexchange.android.socket.model.data.TradeHistorySocketData
import com.stocksexchange.core.utils.extensions.fromJson
import com.stocksexchange.android.data.repositories.tradehistory.TradeHistoryRepository
import com.stocksexchange.android.events.TradeHistoryDataUpdateEvent
import com.stocksexchange.android.model.DataActionItem
import com.stocksexchange.android.socket.handlers.base.BaseHandler
import com.stocksexchange.android.socket.model.enums.SocketEvent
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class TradeHistoryItemCreationHandler(
    gson: Gson,
    private val tradeHistoryRepository: TradeHistoryRepository
) : BaseHandler<TradeHistorySocketData>(gson) {


    private val mTradeActionItems: MutableList<DataActionItem<Trade>> = mutableListOf()




    override suspend fun onSocketDataReceivedAsync(channel: String,
                                                   socketData: TradeHistorySocketData) {
        val trade = socketData.toTrade()
        val params = TradeHistoryParameters.getDefaultParameters(socketData.currencyPairId)

        tradeHistoryRepository.save(params, trade)

        val tradesResult = tradeHistoryRepository.get(params)

        if(tradesResult.isErroneous()) {
            return
        }

        val trades = tradesResult.getSuccessfulResultValue()

        mTradeActionItems.add(DataActionItem.insert(trade))

        EventBus.getDefault().postSticky(TradeHistoryDataUpdateEvent.newInstance(
            newData = trades,
            dataActionItems = mTradeActionItems,
            source = this,
            onConsumeListener = this
        ))
    }


    override fun onConsumed() {
        if(mTradeActionItems.isNotEmpty()) {
            mTradeActionItems.clear()
        }
    }


    override fun convertSocketData(socketDataJsonObj: JSONObject): TradeHistorySocketData {
        return gson.fromJson(socketDataJsonObj.toString())
    }


    override fun getLoggingKey(): String = "onTradeHistoryItemCreated"


    override fun getSocketEvent(): SocketEvent = SocketEvent.TRADE_HISTORY_ITEM_CREATED


}