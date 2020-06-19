package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException

class OrderCreationException(
    val error: Error,
    message: String = "",
    cause: Throwable? = null
) : ApiException(message, cause) {


    companion object {

        fun amountToSmall(message: String): OrderCreationException {
            return OrderCreationException(
                error = Error.AMOUNT_TOO_SMALL,
                message = message
            )
        }


        fun maxNumOfOpenOrders(message: String): OrderCreationException {
            return OrderCreationException(
                error = Error.MAX_NUM_OF_OPEN_ORDERS,
                message = message
            )
        }


        fun unknown(message: String): OrderCreationException {
            return OrderCreationException(
                error = Error.UNKNOWN,
                message = message
            )
        }

    }


    enum class Error {

        AMOUNT_TOO_SMALL,
        MAX_NUM_OF_OPEN_ORDERS,
        UNKNOWN

    }


}