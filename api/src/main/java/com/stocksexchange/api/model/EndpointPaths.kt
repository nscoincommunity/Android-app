package com.stocksexchange.api.model

object EndpointPaths {


    object Public {

        const val PING = "public/ping"
        const val GET_CURRENCIES = "public/currencies"
        const val GET_CURRENCY = "public/currencies/{${Params.Path.CURRENCY_ID}}"
        const val GET_MARKETS = "public/markets"
        const val GET_CURRENCY_PAIRS = "public/currency_pairs/list/{${Params.Path.CODE}}"
        const val GET_CURRENCY_PAIR = "public/currency_pairs/{${Params.Path.CURRENCY_PAIR_ID}}"
        const val GET_CURRENCY_PAIR_GROUPS = "public/pairs-groups"
        const val GET_TICKER_ITEMS = "public/ticker"
        const val GET_TICKER_ITEM = "public/ticker/{${Params.Path.CURRENCY_PAIR_ID}}"
        const val GET_HISTORY_TRADES = "public/trades/{${Params.Path.CURRENCY_PAIR_ID}}"
        const val GET_ORDERBOOK = "public/orderbook/{${Params.Path.CURRENCY_PAIR_ID}}"
        const val GET_CANDLE_STICKS = "public/chart/{${Params.Path.CURRENCY_PAIR_ID}}/{${Params.Path.INTERVAL_NAME}}"
        const val GET_TWITTER_NEWS = "public/twitter"
        const val SIGN_UP = "signup"
        const val SEND_ACCOUNT_VERIFICATION_EMAIL = "profile/resend/confirmation"
        const val VERIFY_ACCOUNT = "profile/verify/{${Params.Path.VERIFICATION_TOKEN}}"
        const val SEND_PASSWORD_RESET_EMAIL = "password/email"
        const val RESET_PASSWORD = "password/reset"
        const val SIGN_IN = "signin"
        const val CONFIRM_SIGNIN = "confirm_signin"
        const val GET_NEW_OAUTH_CREDENTIALS = "refresh_token/{${Params.Path.REFRESH_TOKEN}}"

        const val GET_BLOG_NEWS = "rss.xml"

    }


    object Private {

        const val GET_PROFILE_INFO = "profile/info"
        const val GET_TRADING_FEES = "trading/fees/{${Params.Path.CURRENCY_PAIR_ID}}"
        const val GET_ACTIVE_ORDERS = "trading/orders/{${Params.Path.CURRENCY_PAIR_ID}}"
        const val GET_ACTIVE_ORDER = "trading/order/{${Params.Path.ORDER_ID}}"
        const val CANCEL_ALL_ACTIVE_ORDERS = "trading/orders"
        const val CANCEL_ACTIVE_ORDERS = "trading/orders/{${Params.Path.CURRENCY_PAIR_ID}}"
        const val CANCEL_ACTIVE_ORDER = "trading/order/{${Params.Path.ORDER_ID}}"
        const val CREATE_ORDER = "trading/orders/{${Params.Path.CURRENCY_PAIR_ID}}"
        const val GET_HISTORY_ORDERS = "reports/orders"
        const val GET_HISTORY_ORDER = "reports/order/{${Params.Path.ORDER_ID}}"
        const val GET_WALLETS = "profile/wallets"
        const val GET_WALLET = "profile/wallets/{${Params.Path.WALLET_ID}}"
        const val CREATE_WALLET = "profile/wallets/{${Params.Path.CURRENCY_ID}}"
        const val GET_DEPOSIT_ADDRESS_DATA = "profile/wallets/address/{${Params.Path.WALLET_ID}}"
        const val CREATE_DEPOSIT_ADDRESS_DATA = "profile/wallets/address/{${Params.Path.WALLET_ID}}"
        const val GET_DEPOSITS = "profile/deposits"
        const val GET_DEPOSIT = "profile/deposits/{${Params.Path.DEPOSIT_ID}}"
        const val GET_WITHDRAWALS = "profile/withdrawals"
        const val GET_WITHDRAWAL = "profile/withdrawals/{${Params.Path.WITHDRAWAL_ID}}"
        const val CREATE_WITHDRAWAL = "profile/withdraw"
        const val SEND_WITHDRAWAL_CONFIRMATION_EMAIL = "profile/withdraw/send-confirm/{${Params.Path.WITHDRAWAL_ID}}"
        const val CONFIRM_WITHDRAWAL = "profile/withdraw/confirm/{${Params.Path.WITHDRAWAL_ID}}/{${Params.Path.CONFIRMATION_TOKEN}}"
        const val CANCEL_WITHDRAWAL = "profile/withdraw/{${Params.Path.WITHDRAWAL_ID}}"
        const val PROVIDE_REFERRAL_CODE = "profile/referral/insert/{${Params.Path.CODE}}"
        const val UPDATE_NOTIFICATION_TOKEN = "settings/notifications/add/{${Params.Path.NOTIFICATION_TOKEN}}"
        const val SET_NOTIFICATION_STATUS = "settings/notifications/mobile/{${Params.Path.STATUS}}"
        const val GET_INBOX = "profile/notifications"
        const val DELETE_INBOX_ITEM = "/profile/notifications/{${Params.Path.ID}}"
        const val READ_ALL_INBOX = "/profile/notifications/readAll"
        const val GET_INBOX_UNREAD_ITEM_COUNT = "profile/notifications/counts"
        const val GET_PRICE_ALERT = "/profile/notifications/price"
        const val CREATE_ALERT_PRICE = "/profile/notifications/price"
        const val DELETE_ALERT_PRICE = "/profile/notifications/price/{${Params.Path.ID}}"

    }


    object Headers {

        const val AUTHORIZATION = "Authorization"

    }


    object Params {


        object Path {

            const val CURRENCY_ID = "currency_id"
            const val CODE = "code"
            const val CURRENCY_PAIR_ID = "currency_pair_id"
            const val INTERVAL_NAME = "interval_name"
            const val VERIFICATION_TOKEN = "verification_token"
            const val REFRESH_TOKEN = "refresh_token"
            const val ORDER_ID = "order_id"
            const val WALLET_ID = "wallet_id"
            const val DEPOSIT_ID = "deposit_id"
            const val WITHDRAWAL_ID = "withdrawal_id"
            const val CONFIRMATION_TOKEN = "confirmation_token"
            const val NOTIFICATION_TOKEN = "notification_token"
            const val STATUS = "status"
            const val ID = "id"

        }


        object Query {

            const val CURRENCY_PAIR_ID = "currencyPairId"
            const val LIMIT = "limit"
            const val OFFSET = "offset"
            const val FROM = "from"
            const val TILL = "till"
            const val SORT = "sort"
            const val SORT_BY = "sortBy"
            const val LIMIT_BIDS = "limit_bids"
            const val LIMIT_ASKS = "limit_asks"
            const val START_TIME = "timeStart"
            const val END_TIME = "timeEnd"
            const val ORDER_STATUS = "orderStatus"
            const val PROTOCOL_ID = "protocol_id"

        }


        object Field {

            const val EMAIL = "email"
            const val PASSWORD = "password"
            const val TOKEN = "token"
            const val KEY = "key"
            const val CODE = "code"
            const val TYPE = "type"
            const val AMOUNT = "amount"
            const val PRICE = "price"
            const val TRIGGER_PRICE = "trigger_price"
            const val CURRENCY_ID = "currency_id"
            const val PROTOCOL_ID = "protocol_id"
            const val ADDRESS = "address"
            const val ADDITIONAL_ADDRESS = "additional_address_parameter"
            const val CURRENCY_PAIR_ID = "currencyPairId"
            const val COMPARISON = "comparison"

        }


    }


}