package com.stocksexchange.android.receivers

import android.content.Context
import android.content.Intent
import com.stocksexchange.android.Constants
import com.stocksexchange.android.model.AppStateStatus
import com.stocksexchange.android.receivers.base.BaseBroadcastReceiver
import com.stocksexchange.android.ui.splash.SplashActivity
import com.stocksexchange.android.ui.splash.newInstance
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.core.handlers.PreferenceHandler
import com.stocksexchange.core.utils.extensions.getParcelableExtraOrThrow
import com.stocksexchange.core.utils.extensions.intentFor
import com.stocksexchange.core.utils.extensions.newTask
import org.koin.core.inject

/**
 * A receiver used for open activity after click on push notification
 */
class PushNotificationReceiver: BaseBroadcastReceiver() {


    companion object {

        private const val KEY_DESTINATION_INTENT = "destination_intent"

        private val PUSH_NOTIFICATION_RECEIVER = "${Constants.PACKAGE_NAME}.PUSH_NOTIFICATION_RECEIVER"


        fun newPushNotificationInstance(context: Context, destinationIntent: Intent): Intent {
            return context.intentFor<PushNotificationReceiver>().apply {
                putExtra(KEY_DESTINATION_INTENT, destinationIntent)
                action = PUSH_NOTIFICATION_RECEIVER
            }
        }

    }


    private var mDestinationIntent: Intent? = null

    private val preferenceHandler: PreferenceHandler by inject()
    private val sessionManager: SessionManager by inject()




    override fun onReceive(context: Context, intent: Intent) {
        if((intent.action != PUSH_NOTIFICATION_RECEIVER) ||
            !sessionManager.isUserSignedIn()) {
            return
        }

        mDestinationIntent = when {
            isAppStarted() -> getDestinationIntentWhenAppIsStarted(context, intent)
            isAppStopped() -> getDestinationIntentWhenAppIsStopped(context, intent)

            else -> null
        }?.newTask()?.also {
            context.startActivity(it)
        }
    }


    private fun isAppStarted(): Boolean {
        return (preferenceHandler.getAppStateStatus() == AppStateStatus.ON_START.title)
    }


    private fun isAppStopped(): Boolean {
        return (preferenceHandler.getAppStateStatus() == AppStateStatus.ON_STOP.title)
    }


    private fun getDestinationIntentWhenAppIsStarted(context: Context, originalIntent: Intent): Intent {
        return if(originalIntent.hasExtra(KEY_DESTINATION_INTENT)) {
            originalIntent.getParcelableExtraOrThrow(KEY_DESTINATION_INTENT)
        } else {
            SplashActivity.newInstance(context)
        }
    }


    private fun getDestinationIntentWhenAppIsStopped(context: Context, originalIntent: Intent): Intent {
        return if(originalIntent.hasExtra(KEY_DESTINATION_INTENT)) {
            SplashActivity.newInstance(
                context = context,
                destinationIntent = originalIntent.getParcelableExtra(KEY_DESTINATION_INTENT)
            )
        } else {
            SplashActivity.newInstance(context)
        }
    }


}