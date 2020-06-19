package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException

class InboxException(
    val error: Error,
    message: String = "",
    cause: Throwable? = null
) : ApiException(message, cause) {


    companion object {

        fun invalidToken(message: String): InboxException {
            return InboxException(
                error = Error.INVALID_TOKEN,
                message = message
            )
        }


        fun unknown(message: String): InboxException {
            return InboxException(
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