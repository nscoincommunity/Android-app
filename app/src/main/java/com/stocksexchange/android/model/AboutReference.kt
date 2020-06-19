package com.stocksexchange.android.model

import com.stocksexchange.android.Constants

enum class AboutReference(val url: String) {

    WEBSITE(Constants.STEX_WEBSITE_URL),
    TERMS_OF_USE(Constants.STEX_TERMS_OF_USE_URL),
    PRIVACY_POLICY(Constants.STEX_PRIVACY_POLICY_URL),
    CANDY_LINK(Constants.CANDY_LINK_APP_URL)

}