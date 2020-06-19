package com.stocksexchange.android.analytics.model

enum class Parameter(val paramName: String) {

    SEARCH_START("search_start"),

    VISIT_OUR_WEBSITE("visit_our_website"),
    OUR_TERMS_OF_USE("our_terms_of_use"),
    PRIVACY_POLICY("privacy_policy"),
    CANDY_LINK("candy_link"),

    REGISTER_UNIDENTIFIABLE_USER("register_unidentifiable_user"),
    REGISTER_IDENTIFIABLE_USER("register_identifiable_user"),

    SHOW_PRICE_DEPTH_CHART("show_price_depth_chart"),
    HIDE_PRICE_DEPTH_CHART("hide_price_depth_chart"),
    SAVE_FAVORITES("save_favorites"),
    PRICE_CHART("price_chart"),
    DEPTH_CHART("depth_chart"),
    ORDERBOOK("orderbook"),
    TRADE_HISTORY("trade_history"),
    MY_ORDERS("my_orders"),
    MY_HISTORY("my_history"),

    STOP_LIMIT("stop_limit"),

    WORKING_API_BASE_URL("working_api_base_url"),
    NOT_WORKING_API_BASE_URL("not_working_api_base_url"),
    DIFFENT_DOMAINS_IN_URLS("different_domains_in_urls")

}