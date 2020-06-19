package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException

class NotificationException(
    val error: Error,
    message: String = "",
    cause: Throwable? = null
) : ApiException(message, cause) {


    companion object {

        fun invalidToken(message: String): NotificationException {
            return NotificationException(
                error = Error.INVALID_TOKEN,
                message = message
            )
        }


        fun unknown(message: String): NotificationException {
            return NotificationException(
                error = Error.UNKNOWN,
                message = message
            )
        }

    }


    enum class Error {

        INVALID_TOKEN,
        UNKNOWN

    }


}