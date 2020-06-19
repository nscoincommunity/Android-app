package com.stocksexchange.core.exceptions

class NoInternetException(
    message: String? = "",
    cause: Throwable? = null
) : Exception(message, cause)