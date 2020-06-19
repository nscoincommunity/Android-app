@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.FloatRange
import kotlin.math.max
import kotlin.math.min


/**
 * Adjusts alpha of the color.
 *
 * @param alpha The alpha to set. Accepts values in a range from 0.0 (min)
 * to 1.0 (max).
 *
 * @return The color with adjusted alpha
 */
fun Int.adjustAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
    val alphaChannel = (255 * alpha).toInt()
    val redChannel = Color.red(this)
    val greenChannel = Color.green(this)
    val blueChannel = Color.blue(this)

    return Color.argb(alphaChannel, redChannel, greenChannel, blueChannel)
}


fun Int.clamp(min: Int, max: Int): Int {
    if(min > max) {
        throw IllegalStateException("The minimum value cannot be larger than the maximum value.")
    }

    return max(min, min(max, this))
}


inline fun Int.containsBits(bits: Int): Boolean {
    return ((this and bits) == bits)
}


inline fun Int.toColorStateList(): ColorStateList = ColorStateList.valueOf(this)