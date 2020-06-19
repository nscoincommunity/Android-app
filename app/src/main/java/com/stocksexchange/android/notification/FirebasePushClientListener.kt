package com.stocksexchange.android.notification

import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.api.model.rest.InboxGetUnreadCountResponse

class FirebasePushClientListener(
    val model: FirebasePushClientImpl
) : FirebasePushClientImpl.ActionListener {


    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        when (requestType) {
            FirebasePushClientImpl.REQUEST_TYPE_NOTIFICATION_TOKEN_UPDATE -> {
                model.saveLastSucceededSendToken()
            }

            FirebasePushClientImpl.REQUEST_TYPE_INBOX_UNREAD_COUNT -> {
                model.onGetInboxUnreadCount(response as InboxGetUnreadCountResponse)
            }

            FirebasePushClientImpl.REQUEST_TYPE_GET_CURRENCY_MARKET -> {
                model.onGetCurrencyMarket(response as CurrencyMarket)
            }
        }
    }


    override fun onRequestFailed(requestType: Int, exception: Throwable, metadata: Any) {

    }


    override fun onRequestSent(requestType: Int) {

    }


    override fun onResponseReceived(requestType: Int) {

    }


}