package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException

class WithdrawalProcessingException(
    val error: Error,
    message: String = "",
    cause: Throwable? = null
) : ApiException(message, cause) {


    companion object {

        fun processing(message: String): WithdrawalProcessingException {
            return WithdrawalProcessingException(
                error = Error.PROCESSING,
                message = message
            )
        }


        fun notFound(message: String): WithdrawalProcessingException {
            return WithdrawalProcessingException(
                error = Error.NOT_FOUND,
                message = message
            )
        }


        fun unknown(message: String): WithdrawalProcessingException {
            return WithdrawalProcessingException(
                error = Error.UNKNOWN,
                message = message
            )
        }

    }


    enum class Error {

        PROCESSING,
        NOT_FOUND,
        UNKNOWN

    }


}