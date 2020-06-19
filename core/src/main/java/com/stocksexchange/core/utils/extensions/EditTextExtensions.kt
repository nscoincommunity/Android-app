@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.graphics.drawable.Drawable
import android.widget.EditText
import android.widget.TextView
import com.stocksexchange.core.Constants


/**
 * Sets a cursor drawable for the EditText.
 *
 * Note: Solution based on reflection since there is
 * no a viable option to set drawable programmatically
 * as of now. Based on the answer: https://stackoverflow.com/a/26543290
 *
 * Note: This solution won't work on PIE devices.
 *
 * @param drawable The drawable to set
 */
fun EditText.setCursorDrawable(drawable: Drawable?) {
    if(Constants.AT_LEAST_PIE || (drawable == null)) {
        return
    }

    try {
        val cursorDrawableResourceField = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        cursorDrawableResourceField.isAccessible = true

        val editorField = TextView::class.java.getDeclaredField("mEditor")
        editorField.isAccessible = true

        val cursorDrawableFieldOwner = editorField.get(this)
        val cursorDrawableFieldClass = cursorDrawableFieldOwner.javaClass

        val cursorDrawableField = cursorDrawableFieldClass.getDeclaredField("mCursorDrawable")
        cursorDrawableField.isAccessible = true
        cursorDrawableField.set(
            cursorDrawableFieldOwner,
            arrayOf(drawable, drawable)
        )
    } catch (exception: Exception) {
        // Ignore
    }
}


inline fun EditText.getContent(): String = text.toString()


inline fun EditText.isEmpty(): Boolean = getContent().isEmpty()


inline fun EditText.enableEditing() {
    isFocusableInTouchMode = true
}


inline fun EditText.disableEditing() {
    isFocusable = false
}