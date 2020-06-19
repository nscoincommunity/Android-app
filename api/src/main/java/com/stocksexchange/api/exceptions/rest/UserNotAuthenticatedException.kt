package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException

class UserNotAuthenticatedException(
    message: String = "",
    cause: Throwable? = null
) : ApiException(message, cause)