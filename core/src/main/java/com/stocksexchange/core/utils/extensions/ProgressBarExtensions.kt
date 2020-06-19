@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.widget.ProgressBar
import androidx.annotation.ColorInt


inline fun ProgressBar.setColor(@ColorInt color: Int) = indeterminateDrawable?.setColor(color)