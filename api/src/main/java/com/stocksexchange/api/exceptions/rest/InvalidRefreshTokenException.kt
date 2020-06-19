package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException

/**
 * An exception to use to denote that a refresh token
 * has expired or is invalid.
 */
class InvalidRefreshTokenException(
    message: String = "",
    cause: Throwable? = null
) : ApiException(message, cause)