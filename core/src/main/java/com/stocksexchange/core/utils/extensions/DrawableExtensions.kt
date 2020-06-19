package com.stocksexchange.core.utils.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt


fun Drawable.setColor(@ColorInt color: Int) {
    mutate().colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}


fun Drawable.setGradientColors(@ColorInt startColor: Int, @ColorInt endColor: Int) {
    if(this !is GradientDrawable) {
        return
    }

    mutate()
    colors = intArrayOf(startColor, endColor)
}


fun Drawable.applyBounds(
    left: Int,
    top: Int,
    right: Int,
    bottom: Int
) = apply { setBounds(left, top, right, bottom) }


fun Drawable.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    applyBounds(
        left = 0,
        top = 0,
        right = canvas.width,
        bottom = canvas.height
    )

    draw(canvas)

    return bitmap
}