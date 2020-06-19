package com.stocksexchange.api.interceptors

import com.stocksexchange.api.model.HttpHeaders
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An interceptor to supply a User-Agent header to all
 * outgoing requests.
 */
class UserAgentInterceptor(
    private val userAgent: String
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val userAgentRequest = chain.request()
            .newBuilder()
            .header(HttpHeaders.USER_AGENT, userAgent)
            .build()

        return chain.proceed(userAgentRequest)
    }


}