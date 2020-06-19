package com.stocksexchange.core.utils.extensions

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import com.stocksexchange.core.Constants


/**
 * Sets an alarm in a backwards compatible way.
 *
 * @param triggerAtMillis The time in milliseconds at which to set an alarm
 * @param pendingIntent The operation to be performed when the alarm goes off
 */
@SuppressWarnings("NewApi")
fun AlarmManager.setAlarm(triggerAtMillis: Long, pendingIntent: PendingIntent) {
    if(Constants.AT_LEAST_MARSHMALLOW) {
        setExactAndAllowWhileIdle(RTC_WAKEUP, triggerAtMillis, pendingIntent)
    } else if(Constants.AT_LEAST_KITKAT && !Constants.AT_LEAST_MARSHMALLOW) {
        setExact(RTC_WAKEUP, triggerAtMillis, pendingIntent)
    } else {
        set(RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }
}