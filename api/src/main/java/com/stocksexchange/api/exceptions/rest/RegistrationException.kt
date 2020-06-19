package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException

class RegistrationException(
    val error: Error,
    val errorMessages: List<String> = listOf(),
    message: String = "",
    throwable: Throwable? = null
) : ApiException(message, throwable) {


    companion object {

        fun multiple(errorMessages: List<String>): RegistrationException {
            return RegistrationException(
                error = Error.MULTIPLE,
                errorMessages = errorMessages
            )
        }


        fun unknown(message: String): RegistrationException {
            return RegistrationException(
                error = Error.UNKNOWN,
                message = message
            )
        }

    }


    enum class Error {

        MULTIPLE,
        UNKNOWN

    }


}