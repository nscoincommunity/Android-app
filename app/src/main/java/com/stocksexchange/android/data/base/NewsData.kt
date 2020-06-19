package com.stocksexchange.android.data.base

interface NewsData<
    BlogNewsResponseResult,
    TwitterNewsResponseResult
> {

    suspend fun getTwitterNews(): TwitterNewsResponseResult

    suspend fun getBlogNews(): BlogNewsResponseResult

}