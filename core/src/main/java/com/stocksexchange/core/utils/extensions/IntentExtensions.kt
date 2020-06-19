@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.content.Intent
import android.os.Parcelable


@Suppress("UNCHECKED_CAST")
fun <T> Intent.getSerializableExtraOrThrow(key: String): T {
    return (getSerializableExtra(key) as? T) ?:
            throw Exception("The intent does not contain a serializable value with the key: $key")
}


@Suppress("UNCHECKED_CAST")
fun <T> Intent.getSerializableExtraOrDefault(key: String, default: T): T {
    return ((getSerializableExtra(key) as? T) ?: default)
}


fun <T : Parcelable> Intent.getParcelableExtraOrThrow(key: String): T {
    return getParcelableExtra(key) ?:
            throw Exception("The intent does not contain a parcelable value with the key: $key")
}


fun <T : Parcelable> Intent.getParcelableExtraOrDefault(key: String, default: T): T {
    return (getParcelableExtra(key) ?: default)
}


inline fun Intent.clearTop(): Intent = apply {
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
}


inline fun Intent.singleTop(): Intent = apply {
    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
}


inline fun Intent.newTask(): Intent = apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}