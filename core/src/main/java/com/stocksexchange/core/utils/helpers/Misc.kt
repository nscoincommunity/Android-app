package com.stocksexchange.core.utils.helpers

import android.media.RingtoneManager
import android.os.Build
import com.stocksexchange.core.Constants


fun composeFeedbackEmailSubject(
    title: String,
    versionName: String
): String {
    val stringBuilder = StringBuilder()

    stringBuilder.append(title).append(" v").append(versionName).append(" | ")
    stringBuilder.append("Device: ").append(Build.MODEL).append("(").append(Build.PRODUCT).append(") | ")
    stringBuilder.append("API ").append(Constants.API_VERSION)

    return stringBuilder.toString()
}


fun composeUserAgentHeader(
    appName: String,
    versionName: String
): String {
    return StringBuilder().run {
        append("$appName/$versionName ")
        append("(Android ${Constants.API_VERSION}; ${Build.MODEL}; ${Build.BRAND} ${Build.DEVICE})")

        toString()
    }
}


fun getDefaultNotificationRingtone(): String {
    return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
}


fun composeInvalidDataDialogMessage(errorsList: List<String>,
                                    footerText: String): String {
    val stringBuilder = StringBuilder()
    var index = 1

    for(error in errorsList) {
        if(index > 1) {
            stringBuilder.append("\n")
        }

        stringBuilder
            .append(index)
            .append(". ")
            .append(error)

        index++
    }

    stringBuilder
        .append("\n\n")
        .append(footerText)

    return stringBuilder.toString()
}