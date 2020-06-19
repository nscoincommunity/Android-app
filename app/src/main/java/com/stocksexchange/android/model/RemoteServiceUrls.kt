package com.stocksexchange.android.model

data class RemoteServiceUrls(
    val apiBaseUrl: String,
    val rssUrl: String,
    val socketUrl: String,
    val hashedDomainName: String = ""
) {


    companion object {

        val STUB = RemoteServiceUrls(
            apiBaseUrl = "",
            rssUrl = "",
            socketUrl = ""
        )

    }


    val hasHashedDomainName: Boolean
        get() = hashedDomainName.isNotBlank()


}