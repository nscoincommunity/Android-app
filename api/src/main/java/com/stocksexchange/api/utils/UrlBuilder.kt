package com.stocksexchange.api.utils

import com.stocksexchange.api.model.EndpointPaths

class UrlBuilder {


    fun buildPingUrl(basePath: String): String {
        return "$basePath/${EndpointPaths.Public.PING}"
    }


}