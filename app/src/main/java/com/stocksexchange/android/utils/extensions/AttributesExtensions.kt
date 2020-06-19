package com.stocksexchange.android.utils.extensions

import com.stocksexchange.android.model.Attributes


/**
 * Extracts data from the attributes using the passed extractor.
 *
 * @param extractor The extract to fetch data
 *
 * @return The data returned by the extractor
 */
inline fun <T> Attributes.extract(extractor: Attributes.() -> T): T {
    return extractor(this)
}