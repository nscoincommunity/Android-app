@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.android.utils.extensions

import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import com.stocksexchange.android.R
import com.stocksexchange.core.utils.extensions.setTaskDescriptionCompat


inline fun Fragment.setRecentAppsToolbarColor(@ColorInt color: Int) = setTaskDescriptionCompat(
    label = getString(R.string.app_name),
    iconId = R.mipmap.ic_launcher,
    primaryColor = color
)