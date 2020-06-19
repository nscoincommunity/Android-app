package com.stocksexchange.android.theming.factories.base

import androidx.annotation.ColorRes
import com.stocksexchange.core.providers.ColorProvider

abstract class BaseThemeFactory<Theme>(
    private val colorProvider: ColorProvider
) {


    protected fun getColor(@ColorRes id: Int): Int = colorProvider.getColor(id)




    abstract fun getDeepTealTheme(): Theme


}