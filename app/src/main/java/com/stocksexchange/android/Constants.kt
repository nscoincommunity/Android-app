package com.stocksexchange.android

/**
 * A singleton holding global constants.
 */
object Constants {


    val PACKAGE_NAME = (Constants::class.java.`package`?.name ?: "")

    // Request codes related
    const val REQUEST_CODE_SHARE_VIA = 1001
    const val REQUEST_CODE_COPY_LINK = 1002
    const val REQUEST_CODE_AUTHENTICATION_ACTIVITY = 1003
    const val REQUEST_CODE_APP_LOCK_RECEIVER = 1004
    const val REQUEST_CODE_USER_LOGOUT_RECEIVER = 1005
    const val REQUEST_CODE_IN_APP_UPDATE = 1006
    const val REQUEST_CODE_NEW_EMAIL = 1007
    const val REQUEST_CODE_CAMERA_PERMISSION = 1008
    const val REQUEST_CODE_QR_CODE = 1009

    // Intercom user registration delay
    const val INTERCOM_USER_REGISTRATION_DELAY_IN_MS_DEFAULT = 7500L
    const val INTERCOM_USER_REGISTRATION_DELAY_IN_MS_WIFI = 5000L
    const val INTERCOM_USER_REGISTRATION_DELAY_IN_MS_2G = 12500L
    const val INTERCOM_USER_REGISTRATION_DELAY_IN_MS_3G = 5500L
    const val INTERCOM_USER_REGISTRATION_DELAY_IN_MS_4G = 5000L
    const val INTERCOM_USER_REGISTRATION_DELAY_IN_MS_5G = 2500L

    // Links
    const val STEX_API_BASE_URL = BuildConfig.STEX_API_BASE_URL
    const val STEX_HOSTNAME = "stex.com"
    const val STEX_WEBSITE_URL = "https://stex.com"
    const val STEX_TERMS_OF_USE_URL = BuildConfig.STEX_TERMS_OF_USE_URL
    const val STEX_PRIVACY_POLICY_URL = BuildConfig.STEX_PRIVACY_POLICY_URL
    const val STEX_TWITTER_URL = "https://twitter.com/stexExchangeR"
    const val STEX_FACEBOOK_URL = "https://www.facebook.com/stex.exchanger"
    const val STEX_TELEGRAM_URL = "https://t.co/mZKBF2bNPN"
    const val STEX_MEDIUM_URL = "https://medium.com/@stex"
    const val STEX_SOCKET_URL = BuildConfig.STEX_SOCKET_URL
    const val STEX_PROFILE_VERIFICATION_URL = "https://app.stex.com/en/profile/verification"

    const val STEX_PROTOCOL_IMAGE_URL_TEMPLATE = "https://app.stex.com/coin_images/%s/dark/%s/icon.png"

    const val STEX_ACCOUNT_VERIFICATION_URL_TOKEN_PRECURSOR = "verify"
    const val STEX_ACCOUNT_VERIFICATION_URL_PREFIX = "/user/$STEX_ACCOUNT_VERIFICATION_URL_TOKEN_PRECURSOR/"

    const val STEX_PASSWORD_RESET_URL_TOKEN_PRECURSOR = "reset"
    const val STEX_PASSWORD_RESET_URL_PREFIX = "/password/$STEX_PASSWORD_RESET_URL_TOKEN_PRECURSOR/"

    const val STEX_MARKET_PREVIEW_URL_SYMBOLS_PRECURSOR = "pair"
    const val STEX_MARKET_PREVIEW_BASIC_TRADE_URL_PATH_SEGMENT = "/basic-trade/$STEX_MARKET_PREVIEW_URL_SYMBOLS_PRECURSOR/"
    const val STEX_MARKET_PREVIEW_ADVANCED_TRADE_URL_PATH_SEGMENT = "/trade/$STEX_MARKET_PREVIEW_URL_SYMBOLS_PRECURSOR/"

    const val STEX_WITHDRAWAL_CONFIRMATION_DATA_PRECURSOR = "confirm"
    const val STEX_WITHDRAWAL_CONFIRMATION_URL_PREFIX = "/withdrawal/$STEX_WITHDRAWAL_CONFIRMATION_DATA_PRECURSOR/"

    const val STEX_WITHDRAWAL_CANCELLATION_ID_PRECURSOR = "cancel"
    const val STEX_WITHDRAWAL_CANCELLATION_URL_PREFIX = "/withdrawal/$STEX_WITHDRAWAL_CANCELLATION_ID_PRECURSOR/"

    const val SUPPORT_EMAIL_ADDRESS = "support@stex.com"

    const val CANDY_LINK_APP_URL = "https://play.google.com/store/apps/details?id=com.candylink.openvpn"

    const val EU_PARLIAMENT_DIRECTIVE_LINK_URL = "https://eur-lex.europa.eu/eli/dir/2018/843/oj"

    const val MAIN_VIEW_ANIMATION_DURATION = 300L

    const val MIN_DATA_REFRESHING_INTERVAL = 3_000L

    const val STEX_OK_HTTP_CLIENT_TIMEOUT = 8_000L

    const val PLAY_STORE_APP_REFERENCE = "https://play.google.com/store/apps/details?id=com.stocksexchange.android"

    const val EMAIL_CONFIRMATION_CODE_LENGTH = 25
    const val SMS_CONFIRMATION_CODE_LENGTH = 6
    const val TWO_FACTOR_AUTH_CODE_LENGTH = 6

    const val REFERRAL_CODE_LENGTH = 8

    const val TRADING_AMOUNT_SEEK_BAR_MIN_PROGRESS = 0
    const val TRADING_AMOUNT_SEEK_BAR_MAX_PROGRESS = 100

    val CLASS_LOADER = javaClass.classLoader

    const val IMPLEMENTATION_NOTIFICATION_TURN_ON = false
    const val IS_REMOTE_SERVICES_URLS_FINDER_ENABLED = false

    const val SWITCH_ANIMATION_DURATION = 250L

}

