package com.stocksexchange.api.exceptions.rss

import com.stocksexchange.api.exceptions.ApiException

class NewsBlogException(
    val error: Error,
    message: String = "",
    cause: Throwable? = null
) : ApiException(message, cause) {


    companion object {

        fun unknown(message: String): NewsBlogException {
            return NewsBlogException(
                error = Error.UNKNOWN,
                message = message
            )
        }

    }


    enum class Error {

        UNKNOWN

    }


}