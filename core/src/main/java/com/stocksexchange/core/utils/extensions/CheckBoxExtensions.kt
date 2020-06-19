package com.stocksexchange.core.utils.extensions

import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.widget.CompoundButtonCompat


fun AppCompatCheckBox.setColor(@ColorInt color: Int) {
    CompoundButtonCompat.setButtonTintList(this, color.toColorStateList())
}