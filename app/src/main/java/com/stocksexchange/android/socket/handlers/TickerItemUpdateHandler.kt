package com.stocksexchange.android.socket.handlers

import com.google.gson.Gson
import com.stocksexchange.android.socket.model.data.TickerItemUpdateSocketData
import com.stocksexchange.core.utils.extensions.fromJson
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.events.CurrencyMarketEvent
import com.stocksexchange.android.events.PerformedCurrencyMarketActionsEvent
import com.stocksexchange.android.model.PerformedCurrencyMarketActions
import com.stocksexchange.android.socket.handlers.base.BaseHandler
import com.stocksexchange.android.socket.model.enums.SocketEvent
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class TickerItemUpdateHandler(
    gson: Gson,
    private val currencyMarketsRepository: CurrencyMarketsRepository
) : BaseHandler<TickerItemUpdateSocketData>(gson) {


    private val mPerformedCurrencyMarketActions = PerformedCurrencyMarketActions()




    override suspend fun onSocketDataReceivedAsync(channel: String,
                                                   socketData: TickerItemUpdateSocketData) {
        val currencyMarketResult = currencyMarketsRepository.getCurrencyMarket(socketData.id)

        if(currencyMarketResult.isErroneous()) {
            return
        }

        val originalCurrencyMarket = currencyMarketResult.getSuccessfulResultValue()
        val updatedCurrencyMarket = originalCurrencyMarket.copy(
            tickerItem = originalCurrencyMarket.tickerItem.copy(
                _lastPrice = socketData.lastPrice,
                _openPrice = socketData.openPrice,
                _lowPrice = socketData.lowPrice,
                _highPrice = socketData.highPrice,
                _dailyVolumeInBaseCurrency = socketData.dailyVolumeInBaseCurrency,
                _dailyVolumeInQuoteCurrency = socketData.dailyVolumeInQuoteCurrency
            )
        )

        currencyMarketsRepository.save(updatedCurrencyMarket)

        val currencyMarketEvent = CurrencyMarketEvent.update(
            currencyMarket = updatedCurrencyMarket,
            source = this
        )

        if(EventBus.getDefault().hasSubscriberForEvent(currencyMarketEvent.javaClass)) {
            EventBus.getDefault().post(currencyMarketEvent)
        } else {
            mPerformedCurrencyMarketActions.addUpdatedCurrencyMarket(updatedCurrencyMarket)

            EventBus.getDefault().postSticky(PerformedCurrencyMarketActionsEvent.init(
                performedActions = mPerformedCurrencyMarketActions,
                source = this,
                onConsumeListener = this
            ))
        }
    }


    override fun onConsumed() {
        mPerformedCurrencyMarketActions.removeAllUpdatedCurrencyMarkets()
    }


    override fun convertSocketData(socketDataJsonObj: JSONObject): TickerItemUpdateSocketData {
        return gson.fromJson(socketDataJsonObj.toString())
    }


    override fun getLoggingKey(): String = "onTickerItemUpdated"


    override fun getSocketEvent(): SocketEvent = SocketEvent.TICKER_ITEM_UPDATED


}