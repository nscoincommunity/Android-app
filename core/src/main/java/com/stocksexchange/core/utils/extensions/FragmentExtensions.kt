@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


inline val Fragment.act: FragmentActivity
    get() = activity!!


inline val Fragment.ctx: Context
    get() = activity!!


inline fun Fragment.makeScreenAwake() = act.makeScreenAwake()


inline fun Fragment.makeScreenAsleep() = act.makeScreenAsleep()


inline fun Fragment.shortToast(message: CharSequence): Toast = act.shortToast(message)


inline fun Fragment.longToast(message: CharSequence): Toast = act.longToast(message)


inline fun Fragment.dpToPx(value: Int): Int = ctx.dpToPx(value)


inline fun Fragment.dpToPx(value: Float): Float = ctx.dpToPx(value)


inline fun Fragment.spToPx(value: Int): Int = ctx.spToPx(value)


inline fun Fragment.spToPx(value: Float): Float = ctx.spToPx(value)


inline fun Fragment.dimenInPx(@DimenRes resourceId: Int): Int = ctx.dimenInPx(resourceId)


inline fun Fragment.getCompatDrawable(@DrawableRes id: Int): Drawable? = ctx.getCompatDrawable(id)


inline fun Fragment.getCompatColor(@ColorRes id: Int): Int = act.getCompatColor(id)


inline fun Fragment.setScreenAwake(isAwake: Boolean) = act.setScreenAwake(isAwake)


inline fun Fragment.setStatusBarColor(@ColorInt color: Int) = act.setStatusBarColor(color)


inline fun Fragment.setNavigationBarColor(@ColorInt color: Int) = act.setNavigationBarColor(color)


inline fun Fragment.setSoftInputMode(mode: Int) = act.setSoftInputMode(mode)


inline fun Fragment.setTaskDescriptionCompat(
    label: String,
    @DrawableRes iconId: Int,
    @ColorInt primaryColor: Int
) = act.setTaskDescriptionCompat(label, iconId, primaryColor)


inline fun Fragment.addWindowFlags(flags: Int) = act.addWindowFlags(flags)


inline fun Fragment.clearWindowFlags(flags: Int) = act.clearWindowFlags(flags)


inline fun Fragment.getStatusBarColor(): Int = act.getStatusBarColor()


inline fun Fragment.getNavigationBarColor(): Int = act.getNavigationBarColor()