@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.widget.SeekBar
import androidx.annotation.ColorInt


inline fun SeekBar.setThumbColor(@ColorInt color: Int) = thumb.setColor(color)


inline fun SeekBar.setPrimaryProgressColor(@ColorInt color: Int) {
    progressTintList = color.toColorStateList()
}


inline fun SeekBar.setSecondaryProgressColor(@ColorInt color: Int) {
    progressBackgroundTintList = color.toColorStateList()
}