package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException

class LoginException(
    val error: Error,
    val errorMessages: List<String> = listOf(),
    message: String = "",
    throwable: Throwable? = null
) : ApiException(message, throwable) {


    companion object {

        fun multiple(errorMessages: List<String>): LoginException {
            return LoginException(
                error = Error.MULTIPLE,
                errorMessages = errorMessages
            )
        }


        fun invalidParameters(message: String): LoginException {
            return LoginException(
                error = Error.INVALID_PARAMETERS,
                message = message
            )
        }


        fun sessionExpired(message: String): LoginException {
            return LoginException(
                error = Error.SESSION_EXPIRED,
                message = message
            )
        }


        fun confirmationObjectBadJson(): LoginException {
            return LoginException(
                error = Error.CONFIRMATION_OBJECT_BAD_JSON,
                message = "Cannot parse sign-in confirmation JSON object."
            )
        }


        fun verificationObjectBadJson(): LoginException {
            return LoginException(
                error = Error.VERIFICATION_OBJECT_BAD_JSON,
                message = "Cannot parse account verification JSON object."
            )
        }


        fun unknown(message: String): LoginException {
            return LoginException(
                error = Error.UNKNOWN,
                message = message
            )
        }

    }


    enum class Error {

        MULTIPLE,
        INVALID_PARAMETERS,
        SESSION_EXPIRED,
        CONFIRMATION_OBJECT_BAD_JSON,
        VERIFICATION_OBJECT_BAD_JSON,
        UNKNOWN

    }


}