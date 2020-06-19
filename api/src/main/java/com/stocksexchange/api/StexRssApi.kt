package com.stocksexchange.api

import com.stocksexchange.api.model.rss.NewsBlogRssModel
import com.stocksexchange.api.services.StexRssService
import com.stocksexchange.api.utils.ResponseExtractor
import com.stocksexchange.core.model.Result

/**
 * An implementation of the STEX RSS API.
 */
class StexRssApi(
    private val stexRssService: StexRssService,
    private val responseExtractor: ResponseExtractor
) {


    fun getBlogNews(): Result<NewsBlogRssModel> {
        return responseExtractor.extractNewsBlogRssResponse(stexRssService.getBlogNews())
    }


}