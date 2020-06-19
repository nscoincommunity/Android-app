package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException


class AlertPriceException(
    val error: Error,
    message: String = "",
    val errorMessages: LinkedHashMap<String, List<String>> = LinkedHashMap(),
    cause: Throwable? = null
) : ApiException(message, cause) {


    companion object {

        fun invalidToken(message: String): AlertPriceException {
            return AlertPriceException(
                error = Error.INVALID_TOKEN,
                message = message
            )
        }


        fun unknown(message: String): AlertPriceException {
            return AlertPriceException(
                error = Error.UNKNOWN,
                message = message
            )
        }


        fun alreadyMore(errorMessages: LinkedHashMap<String, List<String>>): AlertPriceException {
            return AlertPriceException(
                error = Error.ALREADY_MORE,
                errorMessages = errorMessages
            )
        }


        fun alreadyLess(errorMessages: LinkedHashMap<String, List<String>>): AlertPriceException {
            return AlertPriceException(
                error = Error.ALREADY_LESS,
                errorMessages = errorMessages
            )
        }


        fun alreadyExist(): AlertPriceException {
            return AlertPriceException(
                error = Error.ALREADY_EXIST
            )
        }

    }


    enum class Error {

        INVALID_TOKEN,
        ALREADY_MORE,
        ALREADY_LESS,
        ALREADY_EXIST,
        UNKNOWN

    }


}