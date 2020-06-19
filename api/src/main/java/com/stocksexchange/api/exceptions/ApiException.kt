package com.stocksexchange.api.exceptions

open class ApiException(
    message: String = "",
    cause: Throwable? = null
) : Exception(message, cause) {


    override val message: String
        get() = (super.message ?: "")


}