package com.stocksexchange.core.utils.helpers

/**
 * Generates a random string based on the passed parameters.
 *
 * @param length The length of the random string to generate
 * @param includeUppercaseCharacters Whether to include uppercase characters
 * in the generated string
 * @param includeNumbers Whether to include numbers in the generated string
 *
 * @return The generated string
 */
fun generateRandomString(
    length: Int,
    includeUppercaseCharacters: Boolean = true,
    includeNumbers: Boolean = true
): String {
    if(length <= 0) {
        throw IllegalArgumentException("The value of the length parameter should be at least 1.")
    }

    val charactersSourceSetBuilder = StringBuilder().apply {
        ('a'..'z').forEach {
            append(it)
        }

        if(includeUppercaseCharacters) {
            ('A'..'Z').forEach {
                append(it)
            }
        }

        if(includeNumbers) {
            ('0'..'9').forEach {
                append(it)
            }
        }
    }

    val charactersSourceSet = charactersSourceSetBuilder.toString()

    return (1..length)
        .map { charactersSourceSet.random() }
        .joinToString("")
}