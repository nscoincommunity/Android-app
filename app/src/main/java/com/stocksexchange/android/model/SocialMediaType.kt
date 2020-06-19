package com.stocksexchange.android.model

import com.stocksexchange.android.Constants

enum class SocialMediaType(val url: String) {

    FACEBOOK(Constants.STEX_FACEBOOK_URL),
    TWITTER(Constants.STEX_TWITTER_URL),
    TELEGRAM(Constants.STEX_TELEGRAM_URL),
    MEDIUM(Constants.STEX_MEDIUM_URL)

}