package com.stocksexchange.core.utils.helpers

import android.app.Notification
import android.content.Context
import com.stocksexchange.core.utils.extensions.getCompatNotificationManager

private val DEFAULT_VIBRATION_PATTERN = longArrayOf(0L, 300L, 100L, 300L)


@Synchronized
fun show(context: Context, id: Int, notification: Notification) {
    context.getCompatNotificationManager().notify(id, notification)
}


@Synchronized
fun dismiss(context: Context, id: Int) {
    context.getCompatNotificationManager().cancel(id)
}