package com.stocksexchange.api.utils.extensions

import okhttp3.Response


/**
 * A field that returns a count of responses prior to and
 * including this one.
 */
val Response.responseCount: Int
    get() {
        var result = 1
        var response: Response? = priorResponse()

        while(response != null) {
            result++

            response = response.priorResponse()
        }

        return result
    }