package com.stocksexchange.android.notification

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.stocksexchange.android.R
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.api.model.rest.InboxGetUnreadCountResponse
import com.stocksexchange.android.data.repositories.inbox.InboxRepository
import com.stocksexchange.android.data.repositories.notification.NotificationRepository
import com.stocksexchange.android.events.InboxCountItemChangeEvent
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.notification.FirebasePushClientImpl.ActionListener
import com.stocksexchange.android.receivers.PushNotificationReceiver
import com.stocksexchange.android.ui.dashboard.DashboardActivity
import com.stocksexchange.android.ui.dashboard.newInstance
import com.stocksexchange.android.utils.DashboardArgsCreator
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.core.Constants
import com.stocksexchange.core.handlers.PreferenceHandler
import com.stocksexchange.core.utils.extensions.getCompatNotificationManager
import org.greenrobot.eventbus.EventBus
import org.koin.core.get

class FirebasePushClientImpl(
    private val context: Context,
    private val sessionManager: SessionManager,
    private val preferenceHandler: PreferenceHandler,
    private val notificationRepository: NotificationRepository,
    private val inboxRepository: InboxRepository,
    private val currencyMarketsRepository: CurrencyMarketsRepository
) : BaseModel<ActionListener>(), FirebasePushClient {


    companion object {

        const val MAX_SHOW_NOTIFICATION_ITEMS = 5

        const val OPEN_APP_NOTIFY_ID = 1001

        const val REQUEST_TYPE_NOTIFICATION_TOKEN_UPDATE = 0
        const val REQUEST_TYPE_INBOX_UNREAD_COUNT = 1
        const val REQUEST_TYPE_GET_CURRENCY_MARKET = 2
        const val NOTIFICATION_CHANNEL_NAME = "All Notification"
        const val NOTIFICATION_DESCRIPTION = "Notification from Stex"

        const val DATA_KEY_IS_STEX_PUSH = "is_stex_push"
        const val DATA_KEY_IS_STEX_PUSH_VALUE = "1"
        const val DATA_KEY_CHANNEL = "channel"
        const val DATA_KEY_TITLE = "title"
        const val DATA_KEY_BODY = "body"
        const val DATA_KEY_USER_ID = "user_id"

        const val DATA_KEY_CHANNEL_PRICE_ALERT = "price_alert"
        const val DATA_KEY_CHANNEL_OPEN_APP = "open_app"
        const val DATA_KEY_CHANNEL_NOTIFICATION = "notification"
        const val DATA_KEY_CURRENCY_PAIR_ID = "currency_pair_id"

    }


    private val mPushClientListener = FirebasePushClientListener(this)
    private val mNotificationManager = context.getCompatNotificationManager()
    private var mSendToken: String = ""
    private var mBody: String? = null
    private var mTitle: String? = null
    private var notificationId: Int = 0




    init {
        setActionListener(mPushClientListener)
    }


    override fun isStexPush(data: Map<String, String>?): Boolean {
        val isStexPush = data?.get(DATA_KEY_IS_STEX_PUSH)
        if ((isStexPush == null) || (isStexPush != DATA_KEY_IS_STEX_PUSH_VALUE)) {
            return false
        }

        return true
    }


    override fun showPushNotification(data: Map<String, String>?) {
        if (data == null) {
            return
        }

        if (!sessionManager.isUserSignedIn()) {
            return
        }

        if (!sessionManager.getSettings().isNotificationEnabled) {
            return
        }

        val profileInfo = sessionManager.getProfileInfo()
        val userIdFromPush = data[DATA_KEY_USER_ID]
        if ((profileInfo != null) && (profileInfo.id.toString() != userIdFromPush)) {
            return
        }

        notificationId = getNotificationId()

        val channel = data[DATA_KEY_CHANNEL]
        mTitle = data[DATA_KEY_TITLE]
        mBody = data[DATA_KEY_BODY]

        if (channel == DATA_KEY_CHANNEL_PRICE_ALERT) {
            val marketId = data[DATA_KEY_CURRENCY_PAIR_ID]
            marketId?.let {
                getCurrencyMarket(it.toInt())
            }
        } else if (channel == DATA_KEY_CHANNEL_OPEN_APP) {
            showNotification(mTitle, mBody, getPendingOpenAppIntent())
        }
    }


    @SuppressLint("NewApi")
    private fun showNotification(title: String?, body: String?, pendingIntent: PendingIntent) {
        if ((mTitle == null) || (mBody == null)) {
            return
        }

        val notificationChannelId = context.resources.getString(R.string.notification_channel_id)

        if (Constants.AT_LEAST_OREO) {
            val channel = NotificationChannel(
                notificationChannelId,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.description = NOTIFICATION_DESCRIPTION
            mNotificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_push_notification_small)
            .setColor(context.resources.getColor(R.color.black))
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_MAX)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)

        mNotificationManager.notify(notificationId, builder.build())
        preferenceHandler.setLastNotificationId(notificationId)
    }


    private fun getNotificationId(): Int {
        var notificationId = preferenceHandler.getLastNotificationId() + 1
        if (notificationId >= MAX_SHOW_NOTIFICATION_ITEMS) {
            notificationId = 0
        }

        return notificationId
    }


    private fun getPendingInboxIntent(): PendingIntent {
        val intent = DashboardActivity.newInstance(
            context = context,
            dashboardArgs = get<DashboardArgsCreator>().getInboxScreenOpeningArgs()
        )

        return PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    private fun getPendingCurrencyMarketPreviewIntent(currencyMarket: CurrencyMarket): PendingIntent {
        val intent = DashboardActivity.newInstance(
            context = context,
            dashboardArgs = get<DashboardArgsCreator>().getCurrencyMarketPreviewScreenOpeningArgs(
                currencyMarket
            )
        )
        val receiver = PushNotificationReceiver.newPushNotificationInstance(context, intent)

        return PendingIntent.getBroadcast(context,
            getNotificationId(), receiver, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    private fun getPendingOpenAppIntent(): PendingIntent {
        val intent = DashboardActivity.newInstance(
            context = context,
            dashboardArgs = get<DashboardArgsCreator>().getOpenAppArgs()
        )
        val receiver = PushNotificationReceiver.newPushNotificationInstance(context, intent)

        return PendingIntent.getBroadcast(context,
            OPEN_APP_NOTIFY_ID, receiver, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    override fun updateNotificationToken() {
        if (!sessionManager.isUserSignedIn()) {
            return
        }

        try {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    val token = task.result?.token
                    token?.let {
                        sendTokenToApi(token.trim())
                    }
                })
        } catch (e: Exception) {
            return
        }
    }


    override fun sendTokenToApi(token: String) {
        if (!sessionManager.isUserSignedIn()
            || preferenceHandler.getFirebasePushToken() == token
        ) {
            return
        }

        mSendToken = token

        performRequest(
            requestType = REQUEST_TYPE_NOTIFICATION_TOKEN_UPDATE,
            params = token
        )
    }


    override fun getInboxUnreadCount() {
        if (sessionManager.isUserSignedIn()) {
            performRequest(
                requestType = REQUEST_TYPE_INBOX_UNREAD_COUNT
            )
        }
    }


    private fun getCurrencyMarket(id: Int) {
        if (sessionManager.isUserSignedIn()) {
            performRequest(
                requestType = REQUEST_TYPE_GET_CURRENCY_MARKET,
                params = id
            )
        }
    }


    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when (requestType) {
            REQUEST_TYPE_NOTIFICATION_TOKEN_UPDATE -> {
                notificationRepository.updateNotificationToken(params as String).apply {
                    log("updateNotificationToken($params)")
                }
            }

            REQUEST_TYPE_INBOX_UNREAD_COUNT -> {
                inboxRepository.getInboxUnreadCount().apply {
                    log("inboxRepository.getInboxUnreadCount()")
                }
            }

            REQUEST_TYPE_GET_CURRENCY_MARKET -> {
                currencyMarketsRepository.getCurrencyMarket(params as Int).apply {
                    log("currencyMarketsRepository.getCurrencyMarket($params)")
                }
            }

            else -> throw IllegalStateException()
        }
    }


    fun saveLastSucceededSendToken() {
        preferenceHandler.saveFirebasePushToken(mSendToken)
    }


    fun onGetInboxUnreadCount(inboxUnreadCount: InboxGetUnreadCountResponse) {
        preferenceHandler.saveInboxUnreadCount(inboxUnreadCount.counts)

        if (EventBus.getDefault().hasSubscriberForEvent(InboxCountItemChangeEvent::class.java)) {
            EventBus.getDefault().post(InboxCountItemChangeEvent.init(this))
        }
    }


    fun onGetCurrencyMarket(currencyMarket: CurrencyMarket) {
        showNotification(mTitle, mBody, getPendingCurrencyMarketPreviewIntent(currencyMarket))
    }


    interface ActionListener : BaseActionListener


}