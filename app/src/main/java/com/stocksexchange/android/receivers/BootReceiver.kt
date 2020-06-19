package com.stocksexchange.android.receivers

import android.content.Context
import android.content.Intent
import com.stocksexchange.android.receivers.base.BaseBroadcastReceiver

/**
 * A receiver used for restoring all app's relevant tasks
 * after the reboot.
 */
class BootReceiver : BaseBroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent?) {
        if(intent?.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }

        UserLogoutReceiver.recreateUserLogoutAlarm(context)
    }

}