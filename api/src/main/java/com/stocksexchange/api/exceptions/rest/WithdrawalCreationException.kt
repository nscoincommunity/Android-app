package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException

class WithdrawalCreationException(
    val error: Error,
    message: String = "",
    cause: Throwable? = null
) : ApiException(message, cause) {


    companion object {

        fun invalidAddress(message: String): WithdrawalCreationException {
            return WithdrawalCreationException(
                error = Error.INVALID_ADDRESS,
                message = message
            )
        }


        fun destinationTagRequired(message: String): WithdrawalCreationException {
            return WithdrawalCreationException(
                error = Error.DESTINATION_TAG_REQUIRED,
                message = message
            )
        }


        fun notEnoughCostsToPayFee(message: String): WithdrawalCreationException {
            return WithdrawalCreationException(
                error = Error.NOT_ENOUGH_COSTS_TO_PAY_FEE,
                message = message
            )
        }


        fun unknown(message: String): WithdrawalCreationException {
            return WithdrawalCreationException(
                error = Error.UNKNOWN,
                message = message
            )
        }

    }


    enum class Error {

        INVALID_ADDRESS,
        DESTINATION_TAG_REQUIRED,
        NOT_ENOUGH_COSTS_TO_PAY_FEE,
        UNKNOWN

    }


}