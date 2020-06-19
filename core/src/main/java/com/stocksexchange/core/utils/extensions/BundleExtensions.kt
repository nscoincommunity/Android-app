package com.stocksexchange.core.utils.extensions

import android.os.Bundle
import android.os.Parcelable


/**
 * Extracts data from the bundle using the passed extractor.
 *
 * @param extractor The extract to fetch data
 *
 * @return The data returned by the extractor
 */
inline fun <R> Bundle.extract(extractor : Bundle.() -> R) : R {
    return extractor(this)
}


/**
 * Retrieves a serializable value from the bundle or throws an exception
 * if the value cannot be found.
 *
 * @param key The key of the value to retrieve
 *
 * @return The serializable value
 */
@Suppress("UNCHECKED_CAST")
fun <T> Bundle.getSerializableOrThrow(key: String): T {
    return (getSerializable(key) as? T) ?:
            throw Exception("The bundle does not contain a serializable value with the key: $key")
}


@Suppress("UNCHECKED_CAST")
fun <T> Bundle.getSerializableOrDefault(key: String, default: T): T {
    return ((getSerializable(key) as? T) ?: default)
}


/**
 * Retrieves a parcelable value from the bundle or throws an exception
 * if the value cannot be found.
 *
 * @param key The key of the value to retrieve
 *
 * @return The parcelable value
 */
fun <T : Parcelable> Bundle.getParcelableOrThrow(key: String): T {
    return (getParcelable(key) ?:
            throw Exception("The bundle does not contain a parcelable value with the key: $key"))
}


fun <T : Parcelable> Bundle.getParcelableOrDefault(key: String, default: T): T {
    return (getParcelable(key) ?: default)
}