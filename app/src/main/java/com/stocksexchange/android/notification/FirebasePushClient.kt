package com.stocksexchange.android.notification

interface FirebasePushClient {

    fun isStexPush(data: Map<String, String>?): Boolean

    fun showPushNotification(data: Map<String, String>?)

    fun sendTokenToApi(token: String)

    fun updateNotificationToken()

    fun getInboxUnreadCount()

}