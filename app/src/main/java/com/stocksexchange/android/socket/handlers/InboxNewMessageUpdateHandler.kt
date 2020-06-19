package com.stocksexchange.android.socket.handlers

import com.google.gson.Gson
import com.stocksexchange.api.model.rest.Inbox
import com.stocksexchange.core.utils.extensions.fromJson
import com.stocksexchange.android.events.InboxNewMessageEvent
import com.stocksexchange.android.socket.handlers.base.BaseHandler
import com.stocksexchange.android.socket.model.enums.SocketEvent
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class InboxNewMessageUpdateHandler(
    gson: Gson
) : BaseHandler<Inbox>(gson) {


    override suspend fun onSocketDataReceivedAsync(channel: String, socketData: Inbox) {
        if (EventBus.getDefault().hasSubscriberForEvent(InboxNewMessageEvent::class.java)) {
            EventBus.getDefault().post(InboxNewMessageEvent.init(socketData,this))
        }
    }


    override fun convertSocketData(socketDataJsonObj: JSONObject): Inbox {
        return gson.fromJson(socketDataJsonObj.toString())
    }


    override fun getLoggingKey(): String = "InboxNewMessageUpdateHandler"


    override fun getSocketEvent(): SocketEvent = SocketEvent.INBOX_NEW_MESSAGE


}