package com.stocksexchange.core.exceptions

class NotFoundException(
    message: String = "",
    cause: Throwable? = null
) : Exception(message, cause)