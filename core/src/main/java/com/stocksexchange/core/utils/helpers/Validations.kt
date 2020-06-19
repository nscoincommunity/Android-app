package com.stocksexchange.core.utils.helpers

import android.util.Patterns

fun isEmailAddressValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


fun isEmailAddressInvalid(email: String): Boolean {
    return !isEmailAddressValid(email)
}