@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.view.Window
import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON


/**
 * Makes the screen to be constantly awake.
 */
inline fun Window.makeScreenAwake() = addFlags(FLAG_KEEP_SCREEN_ON)


/**
 * Makes the screen to be able to turn off.
 */
inline fun Window.makeScreenAsleep() = clearFlags(FLAG_KEEP_SCREEN_ON)