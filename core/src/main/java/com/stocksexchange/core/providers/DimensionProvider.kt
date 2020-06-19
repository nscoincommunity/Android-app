package com.stocksexchange.core.providers

import android.content.Context
import androidx.annotation.DimenRes
import com.stocksexchange.core.utils.extensions.getDimension

class DimensionProvider(private val context: Context) {


    fun getDimension(@DimenRes id: Int): Float {
        return context.getDimension(id)
    }


}