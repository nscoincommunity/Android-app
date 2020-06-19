package com.stocksexchange.core.utils.text

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan

/**
 * A base span that contains coloring and highlighting
 * functionality.
 */
abstract class SelectorSpan(
    private val mTextColor: Int,
    private val mBackgroundSelectedStateColor: Int
) : ClickableSpan() {


    var isSelected: Boolean = false




    override fun updateDrawState(ds: TextPaint) {
        with(ds) {
            isUnderlineText = shouldUnderlineText()

            color = mTextColor
            bgColor = if(shouldHighlightBackground()) {
                mBackgroundSelectedStateColor
            } else {
                Color.TRANSPARENT
            }
        }
    }


    protected open fun shouldUnderlineText(): Boolean = false


    protected open fun shouldHighlightBackground(): Boolean = isSelected


}
