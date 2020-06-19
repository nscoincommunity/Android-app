package com.stocksexchange.android.utils.extensions

import android.widget.TextView
import androidx.annotation.ColorInt
import com.stocksexchange.core.utils.extensions.setCompoundDrawablesColor


fun TextView.toPrimaryButton(
    @ColorInt contentColor: Int,
    @ColorInt pressedStateBackgroundColor: Int,
    @ColorInt releasedStateBackgroundColor: Int
) {
    setTextColor(contentColor)
    setCompoundDrawablesColor(contentColor)
    background = context.getPrimaryButtonBackground(
        pressedStateBackgroundColor,
        releasedStateBackgroundColor
    )
}


fun TextView.toNewPrimaryButton(
    @ColorInt textColor: Int,
    @ColorInt backgroundColor: Int
) {
    setTextColor(textColor)
    background = context.getNewPrimaryButtonBackground(backgroundColor)
}


fun TextView.toSecondaryButton(
    @ColorInt backgroundColor: Int,
    @ColorInt foregroundColor: Int
) {
    setTextColor(backgroundColor)
    background = context.getSecondaryButtonBackground(
        backgroundColor,
        foregroundColor
    )
}


fun TextView.toSelectableButton(
    @ColorInt textColor: Int,
    @ColorInt backgroundColor: Int
) {
    setTextColor(textColor)
    background = context.getSelectableButtonBackground(backgroundColor)
}


fun TextView.toBorderedButton(@ColorInt color: Int) {
    setTextColor(color)
    background = context.getBorderedButtonBackground(color)
}