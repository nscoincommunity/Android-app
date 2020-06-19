package com.stocksexchange.android.data.base

interface UtilitiesData<
    PingResponseResult
> {

    suspend fun ping(url: String): PingResponseResult

}