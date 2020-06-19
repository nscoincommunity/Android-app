@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.app.Dialog
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.stocksexchange.core.utils.extensions.getCompatColor
import com.stocksexchange.core.utils.extensions.getCompatDrawable


inline fun Dialog.getColor(@ColorRes id: Int): Int = context.getCompatColor(id)


inline fun Dialog.getDrawable(@DrawableRes id: Int): Drawable? = context.getCompatDrawable(id)