package com.stocksexchange.core.providers

import android.content.Context
import androidx.annotation.ColorRes
import com.stocksexchange.core.utils.extensions.getCompatColor

class ColorProvider(private val context: Context) {


    fun getColor(@ColorRes colorResId: Int): Int {
        return context.getCompatColor(colorResId)
    }


}