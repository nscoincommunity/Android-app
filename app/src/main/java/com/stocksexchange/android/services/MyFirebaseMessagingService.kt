package com.stocksexchange.android.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.stocksexchange.android.notification.FirebasePushClient
import io.intercom.android.sdk.push.IntercomPushClient
import org.koin.android.ext.android.inject

class MyFirebaseMessagingService : FirebaseMessagingService() {


    private val mIntercomPushClient: IntercomPushClient = IntercomPushClient()
    private val mFirebasePushClient: FirebasePushClient by inject()




    override fun onNewToken(token: String) {
        mIntercomPushClient.sendTokenToIntercom(application, token)
        mFirebasePushClient.sendTokenToApi(token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(mIntercomPushClient.isIntercomPush(remoteMessage.data)) {
            mIntercomPushClient.handlePush(application, remoteMessage.data)
        } else if (mFirebasePushClient.isStexPush(remoteMessage.data)) {
            mFirebasePushClient.showPushNotification(remoteMessage.data)
        }
    }


}