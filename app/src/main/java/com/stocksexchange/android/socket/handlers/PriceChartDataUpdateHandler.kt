package com.stocksexchange.android.socket.handlers

import com.google.gson.Gson
import com.stocksexchange.api.model.rest.CandleStick
import com.stocksexchange.api.model.rest.PriceChartDataInterval
import com.stocksexchange.api.model.rest.parameters.PriceChartDataParameters
import com.stocksexchange.android.socket.model.data.pricechart.CandleStickSocketData
import com.stocksexchange.core.utils.extensions.fromJson
import com.stocksexchange.android.data.repositories.candlesticks.CandleSticksRepository
import com.stocksexchange.android.events.PriceChartDataUpdateEvent
import com.stocksexchange.android.model.DataActionItem
import com.stocksexchange.android.socket.handlers.base.BaseHandler
import com.stocksexchange.android.socket.model.SocketChannels
import com.stocksexchange.android.socket.model.enums.SocketEvent
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class PriceChartDataUpdateHandler(
    gson: Gson,
    private val candleSticksRepository: CandleSticksRepository
) : BaseHandler<CandleStickSocketData>(gson) {


    private var mSelectedPriceChartDataIntervalName: String = ""

    private val mCandleStickActionItems: MutableList<DataActionItem<CandleStick>> = mutableListOf()




    override suspend fun onSocketDataReceivedAsync(channel: String,
                                                   socketData: CandleStickSocketData) {
        val candleStick = socketData.toCandleStick()
        val intervalName = SocketChannels.parsePriceChartIntervalName(channel)
        val interval = try {
            PriceChartDataInterval.newInstance(intervalName)
        } catch(exception: IllegalStateException) {
            return
        }
        val params = PriceChartDataParameters.getDefaultParameters(
            currencyPairId = socketData.id.currencyPairId,
            interval = interval
        )

        candleSticksRepository.save(params, candleStick)

        // Returning if this particular interval is not currently selected
        if(mSelectedPriceChartDataIntervalName != intervalName) {
            return
        }

        val candleSticksResult = candleSticksRepository.get(params)

        if(candleSticksResult.isErroneous()) {
            return
        }

        val candleSticks = candleSticksResult.getSuccessfulResultValue()

        mCandleStickActionItems.add(DataActionItem.any(candleStick))

        EventBus.getDefault().postSticky(PriceChartDataUpdateEvent.newInstance(
            newData = candleSticks,
            dataActionItems = mCandleStickActionItems,
            source = this,
            onConsumeListener = this
        ))
    }


    override fun onConsumed() {
        if(mCandleStickActionItems.isNotEmpty()) {
            mCandleStickActionItems.clear()
        }
    }


    /**
     * Clears the currently selected price chart data interval name.
     */
    fun clearSelectedPriceChartDataIntervalName() {
        setSelectedPriceChartDataIntervalName("")
    }


    /**
     * Sets a new selected price chart data interval name.
     *
     * @param intervalName The name of the interval to set
     */
    fun setSelectedPriceChartDataIntervalName(intervalName: String) {
        mSelectedPriceChartDataIntervalName = intervalName
    }


    override fun convertSocketData(socketDataJsonObj: JSONObject): CandleStickSocketData {
        return gson.fromJson(socketDataJsonObj.toString())
    }


    override fun getLoggingKey(): String = "onPriceChartDataUpdated"


    override fun getSocketEvent(): SocketEvent = SocketEvent.PRICE_CHART_DATA_UPDATED


}