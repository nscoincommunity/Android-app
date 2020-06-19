@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.text.Spannable


/**
 * Adds span to the range start-end of the text.
 *
 * @param start The start of the range
 * @param end The end of the range
 * @param span The span itself
 */
inline operator fun Spannable.set(start: Int, end: Int, span: Any) {
    setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}