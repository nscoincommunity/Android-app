package com.stocksexchange.api.interceptors

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class HostInterceptor : Interceptor {


    private var mSchema: String = ""
    private var mHost: String = ""




    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHost = originalRequest.url().host()
        val originalSchema = originalRequest.url().scheme()

        return chain.proceed(if(shouldChangeUrl(originalSchema, originalHost)) {
            val newUrl = originalRequest.url().newBuilder()
                .scheme(mSchema)
                .host(mHost)
                .build()

            originalRequest.newBuilder()
                .url(newUrl)
                .build()
        } else {
            originalRequest
        })
    }


    fun setHost(host: String) {
        val url = HttpUrl.parse(host)

        mSchema = (url?.scheme() ?: "")
        mHost = (url?.host() ?: "")
    }


    private fun shouldChangeUrl(originalSchema: String, originalHost: String): Boolean {
        return ((mSchema.isNotBlank() && mHost.isNotBlank()) &&
            ((originalSchema != mSchema) && (originalHost != mHost)))
    }


}