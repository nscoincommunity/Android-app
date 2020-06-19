package com.stocksexchange.android.model

data class DeepLinkData(
    val deepLinkType: DeepLinkType,
    val data: Any? = null
)