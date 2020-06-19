@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.stocksexchange.core.Constants



inline fun Activity.makeScreenAwake() = window.makeScreenAwake()


inline fun Activity.makeScreenAsleep() = window.makeScreenAsleep()


inline fun Activity.setScreenAwake(isAwake: Boolean) = if(isAwake) {
    makeScreenAwake()
} else {
    makeScreenAsleep()
}


inline fun Activity.setStatusBarColor(@ColorInt color: Int) {
    window.statusBarColor = color
}


inline fun Activity.setNavigationBarColor(@ColorInt color: Int) {
    window.navigationBarColor = color
}


inline fun Activity.setSoftInputMode(mode: Int) = window.setSoftInputMode(mode)


@SuppressLint("NewApi")
inline fun Activity.setTaskDescriptionCompat(
    label: String,
    @DrawableRes iconId: Int,
    @ColorInt primaryColor: Int
) {
    setTaskDescription(if(Constants.AT_LEAST_PIE) {
        ActivityManager.TaskDescription(label, iconId, primaryColor)
    } else {
        ActivityManager.TaskDescription(
            label,
            BitmapFactory.decodeResource(resources, iconId),
            primaryColor
        )
    })
}


inline fun Activity.addWindowFlags(flags: Int) = window.addFlags(flags)


inline fun Activity.clearWindowFlags(flags: Int) = window.clearFlags(flags)


inline fun Activity.getStatusBarColor(): Int = window.statusBarColor


inline fun Activity.getNavigationBarColor(): Int = window.navigationBarColor