package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException

/**
 * An exception to use that the server denied the request
 * because there have been way too many in the short period of time.
 */
class TooManyRequestsException(
    message: String = "",
    cause: Throwable? = null
) : ApiException(message, cause)