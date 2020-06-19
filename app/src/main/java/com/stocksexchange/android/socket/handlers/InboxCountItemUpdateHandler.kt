package com.stocksexchange.android.socket.handlers

import com.google.gson.Gson
import com.stocksexchange.core.utils.extensions.fromJson
import com.stocksexchange.android.events.InboxCountItemChangeEvent
import com.stocksexchange.android.socket.handlers.base.BaseHandler
import com.stocksexchange.android.socket.model.data.InboxCountUpdateSocketData
import com.stocksexchange.android.socket.model.enums.SocketEvent
import com.stocksexchange.core.handlers.PreferenceHandler
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class InboxCountItemUpdateHandler(
    gson: Gson,
    private val preferenceHandler: PreferenceHandler
) : BaseHandler<InboxCountUpdateSocketData>(gson) {


    override suspend fun onSocketDataReceivedAsync(channel: String,
                                                   socketData: InboxCountUpdateSocketData) {
        preferenceHandler.saveInboxUnreadCount(socketData.count)

        if (EventBus.getDefault().hasSubscriberForEvent(InboxCountItemChangeEvent::class.java)) {
            EventBus.getDefault().post(InboxCountItemChangeEvent.init(this))
        }
    }


    override fun convertSocketData(socketDataJsonObj: JSONObject): InboxCountUpdateSocketData {
        return gson.fromJson(socketDataJsonObj.toString())
    }


    override fun getLoggingKey(): String = "onInboxItemCountUpdated"


    override fun getSocketEvent(): SocketEvent = SocketEvent.INBOX_COUNT_UPDATE


}