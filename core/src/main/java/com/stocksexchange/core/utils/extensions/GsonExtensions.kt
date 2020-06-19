package com.stocksexchange.core.utils.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Converts a json string into an object implicitly.
 *
 * @param json The json string
 *
 * @return The object to convert the json string to
 */
inline fun <reified T> Gson.fromJson(json: String): T {
    return this.fromJson(json, object : TypeToken<T>() {}.type)
}