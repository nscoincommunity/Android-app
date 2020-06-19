package com.stocksexchange.core.model

data class NetworkInfo(
    val networkType: NetworkType,
    val networkGeneration: NetworkGeneration = NetworkGeneration.UNKNOWN
)