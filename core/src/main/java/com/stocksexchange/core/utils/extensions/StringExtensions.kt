@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.graphics.Color
import com.stocksexchange.core.utils.helpers.hashHmacSha256
import com.stocksexchange.core.utils.helpers.hashMD5
import com.stocksexchange.core.utils.helpers.hashSha1
import java.util.*


/**
 * Truncates the string to the specified limit with the option
 * to ellipsize the ending of the string.
 *
 * @param characterLimit The number of characters to truncate
 * @param ellipsize Whether to ellipsize the ending or not
 *
 * @return The truncated string
 */
fun String.truncate(characterLimit: Int, ellipsize: Boolean = true): String {
    if(isBlank() || (length <= characterLimit)) {
        return this
    }

    return (substring(0, characterLimit) + (if(ellipsize) "…" else ""))
}


/**
 * Appends the exclamation mark.
 *
 * @return The string with the exclamation mark appended
 */
fun String.appendExclamationMark(): String = appendPunctuationMark("!")


/**
 * Appends the colon character.
 *
 * @return The string with the colon character appended
 */
fun String.appendColonCharacter(): String = appendPunctuationMark(":")


/**
 * Appends the punctuation mark.
 *
 * @param mark The mark to append
 *
 * @return The string with a punctuation mark appended
 */
fun String.appendPunctuationMark(mark: String): String {
    return if(endsWith(mark)) this else "$this$mark"
}


/**
 * Capitalizes the first letter of the string.
 *
 * @param locale The currently selected locale
 *
 * @return The capitalized string
 */
fun String.capitalize(locale: Locale): String {
    if(isBlank()) {
        return this
    }

    return substring(0, 1).toUpperCase(locale) + substring(1)
}


/**
 * Checks whether the string is color or not.
 *
 * @return true if this string is color; false otherwise
 */
fun String.isColor(): Boolean {
    if(isBlank()) {
        return false
    }

    return try {
        Color.parseColor(this)
        true
    } catch(exception: Exception) {
        false
    }
}


/**
 * Strips the period character.
 *
 * @return The string without period characters
 */
fun String.stripPeriodCharacter() = replace(".", "")


fun String.replaceCommaWithPeriod() = replace(",", ".")


fun String.replaceSpaceWithPeriod() = replace(" ", "")


fun String.replaceCommaSpaceWithPeriod() = replace(",", ".").replace(" ", "")


inline fun String.sha1(): String = hashSha1(this)


inline fun String.hmacSha256(secret: String): String = hashHmacSha256(secret, this)


inline fun String.md5(): String = hashMD5(this)