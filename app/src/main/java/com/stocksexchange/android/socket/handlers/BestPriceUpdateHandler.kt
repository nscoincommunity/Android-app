package com.stocksexchange.android.socket.handlers

import com.google.gson.Gson
import com.stocksexchange.core.utils.extensions.fromJson
import com.stocksexchange.android.socket.model.data.bestpriceupdate.BestPriceUpdateSocketData
import com.stocksexchange.android.socket.model.data.bestpriceupdate.BestPriceUpdateType
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.events.BestPriceUpdateEvent
import com.stocksexchange.android.socket.handlers.base.BaseHandler
import com.stocksexchange.android.socket.model.enums.SocketEvent
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class BestPriceUpdateHandler(
    gson: Gson,
    private val currencyMarketsRepository: CurrencyMarketsRepository
) : BaseHandler<BestPriceUpdateSocketData>(gson) {


    override suspend fun onSocketDataReceivedAsync(channel: String,
                                                   socketData: BestPriceUpdateSocketData) {
        val currencyMarketResult = currencyMarketsRepository.getCurrencyMarket(
            socketData.currencyPairId
        )

        if(currencyMarketResult.isErroneous()) {
            return
        }

        val originalCurrencyMarket = currencyMarketResult.getSuccessfulResultValue()
        val originalBestBidPrice = originalCurrencyMarket.bestBidPrice
        val originalBestAskPrice = originalCurrencyMarket.bestAskPrice
        val hasBestBidBestPriceUpdated = (socketData.type == BestPriceUpdateType.BID)
        val hasBestAskBestPriceUpdated = (socketData.type == BestPriceUpdateType.ASK)

        val updatedCurrencyMarket = originalCurrencyMarket.copy(
            tickerItem = originalCurrencyMarket.tickerItem.copy(
                _bestBidPrice = if(hasBestBidBestPriceUpdated) socketData.bestPrice else originalBestBidPrice,
                _bestAskPrice = if(hasBestAskBestPriceUpdated) socketData.bestPrice else originalBestAskPrice
            )
        )

        currencyMarketsRepository.save(updatedCurrencyMarket)

        if(EventBus.getDefault().hasSubscriberForEvent(BestPriceUpdateEvent::class.java)) {
            EventBus.getDefault().post(BestPriceUpdateEvent.init(
                currencyMarket = updatedCurrencyMarket,
                source = this
            ))
        }
    }


    override fun convertSocketData(socketDataJsonObj: JSONObject): BestPriceUpdateSocketData {
        return gson.fromJson(socketDataJsonObj.toString())
    }


    override fun getLoggingKey(): String = "onBestPriceUpdated"


    override fun getSocketEvent(): SocketEvent = SocketEvent.BEST_PRICE_UPDATED


}