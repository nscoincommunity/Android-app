package com.stocksexchange.api.services

import com.stocksexchange.api.model.EndpointPaths
import com.stocksexchange.api.model.rss.NewsBlogRssModel
import retrofit2.Call
import retrofit2.http.*

/**
 * A service of the STEX RSS API that describes and
 * documents every endpoint in detail.
 */
interface StexRssService {


    /**
     *
     * PUBLIC ENDPOINTS
     *
     */


    /**
     * Retrieves the blog news.
     */
    @GET(EndpointPaths.Public.GET_BLOG_NEWS)
    fun getBlogNews(): Call<NewsBlogRssModel>


}