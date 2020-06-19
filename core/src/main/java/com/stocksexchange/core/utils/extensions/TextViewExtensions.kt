package com.stocksexchange.core.utils.extensions

import android.graphics.drawable.Drawable
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.annotation.ColorInt


fun TextView.setLeftDrawable(drawable: Drawable?) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
}


fun TextView.getLeftDrawable(): Drawable? {
    return compoundDrawablesRelative[0]
}


fun TextView.setTopDrawable(drawable: Drawable?) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null)
}


fun TextView.getTopDrawable(): Drawable? {
    return compoundDrawablesRelative[1]
}


fun TextView.setRightDrawable(drawable: Drawable?) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
}


fun TextView.getRightDrawable(): Drawable? {
    return compoundDrawablesRelative[2]
}


fun TextView.setBottomDrawable(drawable: Drawable?) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, drawable)
}


fun TextView.getBottomDrawable(): Drawable? {
    return compoundDrawablesRelative[3]
}


fun TextView.clearDrawable() {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
    this.compoundDrawablePadding = 0
}


fun TextView.setTypefaceStyle(style: Int) {
    setTypeface(null, style)
}


fun TextView.setCompoundDrawablesColor(@ColorInt color: Int) {
    setCompoundDrawables(
        getLeftDrawable()?.apply { setColor(color) },
        getTopDrawable()?.apply { setColor(color) },
        getRightDrawable()?.apply { setColor(color) },
        getBottomDrawable()?.apply { setColor(color) }
    )
}


/**
 * Changes the text of this TextView by cross fading previous text with new one.
 *
 * @param text The new text to set
 * @param duration The duration for the cross fade animation. Default is 300 milliseconds.
 * @param interpolator The interpolator for the animation. Default is LinearInterpolator.
 * @param onFinish The callback to invoke when the animation has been finished
 */
fun TextView.crossFadeText(text: CharSequence, duration: Long = 300L,
                           interpolator: Interpolator = LinearInterpolator(),
                           onFinish: (() -> Unit)? = null) {
    crossFadeItself(duration, interpolator, onFinish) {
        setText(text)
    }
}


fun TextView.setMultilineTextEnabled(isMultilineTextEnabled: Boolean) {
    if(isMultilineTextEnabled) {
        enableMultilineText()
    } else {
        disableMultilineText()
    }
}


fun TextView.enableMultilineText() {
    minLines = 0
    maxLines = Integer.MAX_VALUE
    isSingleLine = false
}


fun TextView.disableMultilineText() {
    minLines = 1
    maxLines = 1
    isSingleLine = true
}


fun TextView.setLineCount(count: Int) {
    minLines = count
    maxLines = count
    setLines(count)
}