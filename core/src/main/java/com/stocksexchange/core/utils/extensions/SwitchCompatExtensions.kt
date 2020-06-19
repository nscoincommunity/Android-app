package com.stocksexchange.core.utils.extensions

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.appcompat.widget.SwitchCompat
import androidx.core.graphics.drawable.DrawableCompat


fun SwitchCompat.setColor(@ColorInt color: Int) {
    setColors(color, color, color, color)
}


fun SwitchCompat.setColors(
    @ColorInt pointerDeactivatedColor: Int,
    @ColorInt pointerActivatedColor: Int,
    @ColorInt backgroundDeactivatedColor: Int,
    @ColorInt backgroundActivatedColor: Int
) {
    val switchStates: Array<IntArray> = arrayOf(
        intArrayOf(-android.R.attr.state_checked),
        intArrayOf(android.R.attr.state_checked)
    )

    val switchThumbDrawableColors = intArrayOf(
        pointerDeactivatedColor,
        pointerActivatedColor
    )

    val switchTrackDrawableColors = intArrayOf(
        backgroundDeactivatedColor.adjustAlpha(0.5f),
        backgroundActivatedColor.adjustAlpha(0.5f)
    )

    DrawableCompat.setTintList(
        DrawableCompat.wrap(thumbDrawable),
        ColorStateList(switchStates, switchThumbDrawableColors)
    )

    DrawableCompat.setTintList(
        DrawableCompat.wrap(trackDrawable),
        ColorStateList(switchStates, switchTrackDrawableColors)
    )
}