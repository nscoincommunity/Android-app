package com.stocksexchange.api.exceptions.rest

import com.stocksexchange.api.exceptions.ApiException

class ReferralException(
    val error: Error,
    message: String = "",
    cause: Throwable? = null
) : ApiException(message, cause) {


    companion object {

        fun invalidCode(message: String): ReferralException {
            return ReferralException(
                error = Error.INVALID_CODE,
                message = message
            )
        }


        fun unknown(message: String): ReferralException {
            return ReferralException(
                error = Error.UNKNOWN,
                message = message
            )
        }

    }


    enum class Error {

        INVALID_CODE,
        UNKNOWN

    }


}