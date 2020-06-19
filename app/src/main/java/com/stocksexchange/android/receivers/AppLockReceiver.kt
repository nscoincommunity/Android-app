package com.stocksexchange.android.receivers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.stocksexchange.android.Constants
import com.stocksexchange.android.receivers.base.BaseBroadcastReceiver

/**
 * A receiver used for handling app locking functionality.
 */
class AppLockReceiver(private val listener: Listener) : BaseBroadcastReceiver() {


    companion object {

        val ACTION_APP_LOCK = "${Constants.PACKAGE_NAME}.ACTION_APP_LOCK"


        fun init(context: Context, requestCode: Int): PendingIntent {
            return PendingIntent.getBroadcast(
                context,
                requestCode,
                Intent(ACTION_APP_LOCK),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

    }




    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == ACTION_APP_LOCK) {
            listener.onAppLockRequested()
        }
    }


    interface Listener {

        /**
         * A method that gets invoked whenever the app
         * should be locked due to inactivity for a certain
         * period of time.
         */
        fun onAppLockRequested()

    }


}