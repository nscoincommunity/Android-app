@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.widget.ImageView
import androidx.annotation.ColorInt


inline fun ImageView.setColor(@ColorInt color: Int) = drawable?.setColor(color)