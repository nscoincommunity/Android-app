package com.stocksexchange.android.socket

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.stocksexchange.android.receivers.NetworkStateReceiver
import com.stocksexchange.android.socket.handlers.PriceChartDataUpdateHandler
import com.stocksexchange.android.socket.handlers.base.Handler
import com.stocksexchange.android.socket.model.SocketChannels
import com.stocksexchange.android.socket.model.enums.SocketEvent
import com.stocksexchange.android.socket.model.SocketEventActions
import com.stocksexchange.android.socket.model.SocketEventPayloadProperties
import com.stocksexchange.android.socket.model.SocketState
import com.stocksexchange.android.socket.model.enums.Status
import com.stocksexchange.api.utils.CredentialsHandler
import com.stocksexchange.core.managers.BackgroundManager
import com.stocksexchange.core.utils.extensions.isNetworkAvailable
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import okhttp3.OkHttpClient
import org.json.JSONObject
import timber.log.Timber

/**
 * A class that is responsible for socket related functionality.
 */
class SocketConnection constructor(
    application: Application,
    private val credentialsHandler: CredentialsHandler,
    private val handlersMap: Map<SocketEvent, Handler>
) {


    companion object {

        private const val MAX_RECONNECTION_ATTEMPTS = 3

    }


    private var mIsNetworkAvailable: Boolean = application.isNetworkAvailable()
    private var mIsInForeground: Boolean = true

    private var mStatus: Status = Status.INVALID

    private val mSocketState: SocketState = SocketState()
    private val mSocketStateCache: SocketState = SocketState()

    private var mSocket: Socket? = null




    init {
        initNetworkReceiver(application)
        initBackgroundManager(application)
    }


    private fun initNetworkReceiver(context: Context) {
        val networkListener = object : NetworkStateReceiver.Listener {

            override fun onConnected() {
                mIsNetworkAvailable = true

                if(shouldConnect()) {
                    connect()
                }
            }

            override fun onDisconnected() {
                mIsNetworkAvailable = false
            }

        }

        context.registerReceiver(
            NetworkStateReceiver(context, networkListener),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }


    private fun initBackgroundManager(application: Application) {
        val backgroundManagerListener = object : BackgroundManager.Listener {

            override fun onBecameForeground() {
                mIsInForeground = true

                if(shouldConnect()) {
                    connect()
                }
            }

            override fun onBecameBackground() {
                mIsInForeground = false

                if(shouldDisconnect()) {
                    disconnect()
                }
            }

        }

        BackgroundManager.getInstance(application).registerListener(backgroundManagerListener)
    }


    /**
     * Initializes the socket with the specified URL.
     *
     * @param socketUrl The URL of the socket
     * @param okHttpClient The network client
     */
    fun initSocket(socketUrl: String, okHttpClient: OkHttpClient) {
        IO.setDefaultOkHttpCallFactory(okHttpClient)
        IO.setDefaultOkHttpWebSocketFactory(okHttpClient)

        val options = IO.Options().apply {
            reconnectionAttempts = MAX_RECONNECTION_ATTEMPTS
            transports = arrayOf(WebSocket.NAME)
            callFactory = okHttpClient
            webSocketFactory = okHttpClient
        }

        mSocket = IO.socket(socketUrl, options)?.apply {
            // Built-in events
            on(Socket.EVENT_CONNECT, Listener(Socket.EVENT_CONNECT))
            on(Socket.EVENT_CONNECT_ERROR, Listener(Socket.EVENT_CONNECT_ERROR))
            on(Socket.EVENT_ERROR, Listener(Socket.EVENT_ERROR))
            on(Socket.EVENT_DISCONNECT, Listener(Socket.EVENT_DISCONNECT))

            // Custom events
            for(socketEvent in SocketEvent.values()) {
                on(socketEvent.eventName, Listener(socketEvent.eventName))
            }
        }

        if(shouldConnect()) {
            connect()
        }
    }


    private fun shouldConnect(): Boolean {
        return (mIsInForeground && mIsNetworkAvailable)
    }


    private fun shouldDisconnect(): Boolean {
        return (!mIsInForeground && mIsNetworkAvailable)
    }


    /**
     * Tries to establish a web socket connection. Does nothing if the
     * connection is already established.
     */
    fun connect() {
        if(mStatus in listOf(Status.CONNECTED, Status.SENT_CONNECTION_REQUEST)) {
            return
        }

        mSocket?.connect()
        mStatus = Status.SENT_CONNECTION_REQUEST

        Timber.i("Sent a socket connection request.")
    }


    /**
     * Closes a web socket connection. Does nothing if the connection is not established.
     */
    fun disconnect() {
        if(mStatus in listOf(Status.DISCONNECTED, Status.SENT_DISCONNECTION_REQUEST)) {
            return
        }

        mSocket?.disconnect()
        mStatus = Status.SENT_DISCONNECTION_REQUEST

        Timber.i("Sent a socket disconnection request.")
    }


    fun startListeningToTickerUpdates(subscriberKey: String) {
        sendSubscribingEvent(subscriberKey, SocketChannels.getTickerUpdatesChannel())
    }


    fun stopListeningToTickerUpdates(subscriberKey: String) {
        sendUnsubscribingEvent(subscriberKey, SocketChannels.getTickerUpdatesChannel())
    }


    fun startListeningToPriceChartDataUpdates(subscriberKey: String, intervalName: String, currencyPairId: String) {
        // Denoting the currently selected price chart interval name
        (handlersMap[SocketEvent.PRICE_CHART_DATA_UPDATED] as? PriceChartDataUpdateHandler)
            ?.setSelectedPriceChartDataIntervalName(intervalName)

        // Subscribing
        sendSubscribingEvent(subscriberKey, SocketChannels.getPriceChartDataUpdatesChannel(intervalName, currencyPairId))
    }


    fun stopListeningToAllPriceChartDataUpdates(subscriberKey: String, currencyPairId: String) {
        // Creating a mutable set to prevent concurrent modification
        val activeChannelsSet = if(isInBlackZone()) {
            mSocketStateCache
        } else {
            mSocketState
        }.getActiveChannels().toMutableSet()

        for(channel in activeChannelsSet) {
            if(SocketChannels.isPriceChartDataUpdatesChannel(channel, currencyPairId)) {
                sendUnsubscribingEvent(subscriberKey, channel)
            }
        }

        // Clearing the currently selected price chart interval name
        (handlersMap[SocketEvent.PRICE_CHART_DATA_UPDATED] as? PriceChartDataUpdateHandler)
            ?.clearSelectedPriceChartDataIntervalName()
    }


    fun startListeningToOrderbookOrdersUpdates(subscriberKey: String, currencyPairId: String) {
        sendSubscribingEvent(subscriberKey, SocketChannels.getOrderbookBidOrdersUpdatesChannel(currencyPairId))
        sendSubscribingEvent(subscriberKey, SocketChannels.getOrderbookAskOrdersUpdatesChannel(currencyPairId))
    }


    fun stopListeningToOrderbookOrdersUpdates(subscriberKey: String, currencyPairId: String) {
        sendUnsubscribingEvent(subscriberKey, SocketChannels.getOrderbookBidOrdersUpdatesChannel(currencyPairId))
        sendUnsubscribingEvent(subscriberKey, SocketChannels.getOrderbookAskOrdersUpdatesChannel(currencyPairId))
    }


    fun startListeningToTradeHistoryItemsCreation(subscriberKey: String, currencyPairId: String) {
        sendSubscribingEvent(subscriberKey, SocketChannels.getTradeHistoryItemsCreationChannel(currencyPairId))
    }


    fun stopListeningToTradeHistoryItemsCreation(subscriberKey: String, currencyPairId: String) {
        sendUnsubscribingEvent(subscriberKey, SocketChannels.getTradeHistoryItemsCreationChannel(currencyPairId))
    }


    fun startListeningToBestPriceUpdates(subscriberKey: String, currencyPairId: String) {
        sendSubscribingEvent(subscriberKey, SocketChannels.geBestBidPriceUpdatesChannel(currencyPairId))
        sendSubscribingEvent(subscriberKey, SocketChannels.getBestAskPriceUpdatesChannel(currencyPairId))
    }


    fun stopListeningToBestPriceUpdates(subscriberKey: String, currencyPairId: String) {
        sendUnsubscribingEvent(subscriberKey, SocketChannels.geBestBidPriceUpdatesChannel(currencyPairId))
        sendUnsubscribingEvent(subscriberKey, SocketChannels.getBestAskPriceUpdatesChannel(currencyPairId))
    }


    fun startListeningToWalletBalanceUpdates(subscriberKey: String, walletId: String) {
        sendSubscribingEvent(subscriberKey, SocketChannels.getWalletBalanceUpdatesChannel(walletId))
    }


    fun stopListeningToWalletBalanceUpdates(subscriberKey: String, walletId: String) {
        sendUnsubscribingEvent(subscriberKey, SocketChannels.getWalletBalanceUpdatesChannel(walletId))
    }


    fun startListeningToWalletsBalancesUpdates(subscriberKey: String, walletIds: List<String>) {
        for(walletId in walletIds) {
            startListeningToWalletBalanceUpdates(subscriberKey, walletId)
        }
    }


    fun stopListeningToWalletsBalancesUpdates(subscriberKey: String, walletIds: List<String>) {
        for(walletId in walletIds) {
            stopListeningToWalletBalanceUpdates(subscriberKey, walletId)
        }
    }


    fun startListeningToMarketPreviewUpdates(subscriberKey: String, priceChartIntervalName: String, currencyPairId: String) {
        startListeningToTickerUpdates(subscriberKey)
        startListeningToPriceChartDataUpdates(subscriberKey, priceChartIntervalName, currencyPairId)
        startListeningToOrderbookOrdersUpdates(subscriberKey, currencyPairId)
        startListeningToTradeHistoryItemsCreation(subscriberKey, currencyPairId)
    }


    fun stopListeningToMarketPreviewUpdates(subscriberKey: String, currencyPairId: String) {
        stopListeningToTickerUpdates(subscriberKey)
        stopListeningToAllPriceChartDataUpdates(subscriberKey, currencyPairId)
        stopListeningToOrderbookOrdersUpdates(subscriberKey, currencyPairId)
        stopListeningToTradeHistoryItemsCreation(subscriberKey, currencyPairId)
    }


    fun startListeningToActiveOrdersUpdates(subscriberKey: String, userId: String, currencyPairId: String) {
        startListeningToActiveOrdersFillsUpdates(subscriberKey, userId, currencyPairId)
        startListeningToActiveOrdersStatusesUpdates(subscriberKey, userId, currencyPairId)
    }


    fun stopListeningToActiveOrdersUpdates(subscriberKey: String, userId: String, currencyPairId: String) {
        stopListeningToActiveOrdersFillsUpdates(subscriberKey, userId, currencyPairId)
        stopListeningToActiveOrdersStatusesUpdates(subscriberKey, userId, currencyPairId)
    }


    fun startListeningToActiveOrdersFillsUpdates(subscriberKey: String, userId: String, currencyPairId: String) {
        sendSubscribingEvent(subscriberKey, SocketChannels.getUserBuyActiveOrdersFillsUpdatesChannel(userId, currencyPairId))
        sendSubscribingEvent(subscriberKey, SocketChannels.getUserSellActiveOrdersFillsUpdatesChannel(userId, currencyPairId))
    }


    fun stopListeningToActiveOrdersFillsUpdates(subscriberKey: String, userId: String, currencyPairId: String) {
        sendUnsubscribingEvent(subscriberKey, SocketChannels.getUserBuyActiveOrdersFillsUpdatesChannel(userId, currencyPairId))
        sendUnsubscribingEvent(subscriberKey, SocketChannels.getUserSellActiveOrdersFillsUpdatesChannel(userId, currencyPairId))
    }


    fun startListeningToActiveOrdersStatusesUpdates(subscriberKey: String, userId: String, currencyPairId: String) {
        sendSubscribingEvent(subscriberKey, SocketChannels.getUserActiveOrdersStatusesUpdatesChannel(userId, currencyPairId))
    }


    fun stopListeningToActiveOrdersStatusesUpdates(subscriberKey: String, userId: String, currencyPairId: String) {
        sendUnsubscribingEvent(subscriberKey, SocketChannels.getUserActiveOrdersStatusesUpdatesChannel(userId, currencyPairId))
    }


    fun startListeningToUserInboxCountUpdates(subscriberKey: String, userId: String) {
        sendSubscribingEvent(subscriberKey, SocketChannels.getUserInboxCountChannel(userId))
    }


    fun stopListeningToUserInboxCountUpdates(subscriberKey: String, userId: String) {
        sendUnsubscribingEvent(subscriberKey, SocketChannels.getUserInboxCountChannel(userId))
    }


    fun startListeningUserInboxNewMessageUpdates(subscriberKey: String, userId: String) {
        sendSubscribingEvent(subscriberKey, SocketChannels.getUserInboxNewMessage(userId))
    }


    fun stopListeningToUserInboxNewMessageUpdates(subscriberKey: String, userId: String) {
        sendUnsubscribingEvent(subscriberKey, SocketChannels.getUserInboxNewMessage(userId))
    }


    private fun sendSubscribingEvent(subscriberKey: String, channel: String) {
        if(!shouldSendSubscribingEvent(subscriberKey, channel)) {
            return
        }

        mSocketState.addSubscriberToChannel(subscriberKey, channel)

        val jsonObject = getEmittingEventJsonObject(channel)

        mSocket?.emit(SocketEventActions.SUBSCRIBE, jsonObject)

        Timber.i("Sent subscribing event. Channel: $channel. Subscriber: $subscriberKey")
    }


    private fun shouldSendSubscribingEvent(subscriberKey: String, channel: String): Boolean {
        return if(isInBlackZone()) {
            mSocketStateCache.addSubscriberToChannel(subscriberKey, channel)
            false
        } else {
            if(mSocketState.doesChannelHaveSubscribers(channel)) {
                mSocketState.addSubscriberToChannel(subscriberKey, channel)
                false
            } else {
                true
            }
        }
    }


    private fun sendUnsubscribingEvent(subscriberKey: String, channel: String) {
        if(!shouldSendUnsubscribingEvent(subscriberKey, channel)) {
            return
        }

        mSocketState.removeSubscriberFromChannel(subscriberKey, channel)

        val jsonObject = getEmittingEventJsonObject(channel)

        mSocket?.emit(SocketEventActions.UNSUBSCRIBE, jsonObject)

        Timber.i("Sent unsubscribing event. Channel: $channel. Subscriber: $subscriberKey")
    }


    private fun shouldSendUnsubscribingEvent(subscriberKey: String, channel: String): Boolean {
        return if(isInBlackZone()) {
            mSocketStateCache.removeSubscriberFromChannel(subscriberKey, channel)
            false
        } else {
            if(mSocketState.getSubscriberCountForChannel(channel) > 1) {
                mSocketState.removeSubscriberFromChannel(subscriberKey, channel)
                false
            } else {
                true
            }
        }
    }


    private fun isInBlackZone(): Boolean {
        return (!mIsNetworkAvailable || !mIsInForeground)
    }


    private fun getEmittingEventJsonObject(channel: String): JSONObject {
        return JSONObject().apply {
            put(SocketEventPayloadProperties.CHANNEL, channel)
            put(SocketEventPayloadProperties.AUTH, getEmittingEventAuthJsonObject(channel))
        }
    }


    private fun getEmittingEventAuthJsonObject(channel: String): JSONObject {
        val isPrivateChannel = SocketChannels.isPrivateChannel(channel)
        val hasOAuthCredentials = credentialsHandler.hasOAuthCredentials()

        return if(isPrivateChannel && hasOAuthCredentials) {
            val oauthCredentials = credentialsHandler.getOAuthCredentials()
            val headersJsonObject = JSONObject().apply {
                put(SocketEventPayloadProperties.AUTHORIZATION, oauthCredentials.authorizationHeader)
            }
            val authObject = JSONObject().apply {
                put(SocketEventPayloadProperties.HEADERS, headersJsonObject)
            }

            authObject
        } else {
            JSONObject()
        }
    }


    private fun saveEmittingEventsState() {
        mSocketStateCache.save(mSocketState)
        mSocketState.clear()
    }


    private fun restoreEmittingEventsState() {
        if(mSocketStateCache.isEmpty()) {
            return
        }

        for((channel, subscriberKeys) in mSocketStateCache) {
            for(subscriberKey in subscriberKeys) {
                sendSubscribingEvent(subscriberKey, channel)
            }
        }

        mSocketStateCache.clear()
    }


    private fun isValidSocketResponse(socketData: Array<out Any>): Boolean {
        if(socketData.size < 2) {
            return false
        }

        val channel = socketData[0]
        val payload = socketData[1]

        if((channel !is String) ||
            (!mSocketState.doesChannelHaveSubscribers(channel)) ||
            (payload !is JSONObject)) {
            return false
        }

        return true
    }


    private fun onConnected() {
        mStatus = Status.CONNECTED
        Timber.i("onConnected")

        restoreEmittingEventsState()
    }


    private fun onConnectionError() {
        mStatus = Status.CONNECTION_ERROR
        Timber.i("onConnectionError")
    }


    private fun onError(args: Array<out Any>) {
        mStatus = Status.ERROR
        Timber.i("onError")

        if(args.isEmpty() || args[0] !is Throwable) {
            return
        }

        Timber.e(args[0] as Throwable, "A web socket error has occurred.")
    }


    private fun onDisconnected() {
        mStatus = Status.DISCONNECTED
        Timber.i("onDisconnected")

        saveEmittingEventsState()
    }


    private inner class Listener(val event: String) : Emitter.Listener {

        override fun call(vararg args: Any) {
            when(event) {
                Socket.EVENT_CONNECT -> onConnected()
                Socket.EVENT_CONNECT_ERROR -> onConnectionError()
                Socket.EVENT_ERROR -> onError(args)
                Socket.EVENT_DISCONNECT -> onDisconnected()

                else -> onDataReceived(args)
            }
        }

        private fun onDataReceived(data: Array<out Any>) {
            if(!isValidSocketResponse(data)) {
                return
            }

            for(handler in handlersMap.values) {
                if(handler.canHandleSocketEvent(event)) {
                    handler.onSocketDataReceived(data)
                    break
                }
            }
        }

    }


}