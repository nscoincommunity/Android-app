package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException

class AccountVerificationException(
    val error: Error,
    val errorMessages: List<String> = listOf(),
    message: String = "",
    throwable: Throwable? = null
) : ApiException(message, throwable) {


    companion object {

        fun multiple(errorMessages: List<String>): AccountVerificationException {
            return AccountVerificationException(
                error = Error.MULTIPLE,
                errorMessages = errorMessages
            )
        }


        fun userNotFound(message: String): AccountVerificationException {
            return AccountVerificationException(
                error = Error.USER_NOT_FOUND,
                message = message
            )
        }


        fun unknown(message: String): AccountVerificationException {
            return AccountVerificationException(
                error = Error.UNKNOWN,
                message = message
            )
        }

    }


    enum class Error {

        MULTIPLE,
        USER_NOT_FOUND,
        UNKNOWN

    }


}