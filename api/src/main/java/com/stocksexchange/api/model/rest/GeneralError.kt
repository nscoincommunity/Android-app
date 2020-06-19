package com.stocksexchange.api.model.rest

enum class GeneralError(val message: String) {

    // Errors from public endpoints
    WRONG_PARAMETERS("Wrong parameters"),
    LOGIN_SESSION_EXPIRED("The signin session has expired. Please try again."),
    USER_NOT_FOUND("User not found"),
    NOTHING_FOUND("Nothing found"),
    INVALID_REFRESH_TOKEN("Session has expired or invalid token"),
    TOO_MANY_ATTEMPTS("Too Many Attempts."),
    UNAUTHENTICATED("Unauthenticated."),

    // Errors from private endpoints
    AMOUNT_TOO_SMALL("Entered Amount is Too Small"),
    MAX_NUM_OF_OPEN_ORDERS("The maximum amount of open orders with the same price cannot exceed  10"),
    WRONG_ADDRESS("Wrong address"),
    NOT_ENOUGH_COSTS_TO_PAY_FEE("You do not have enough"),
    CURRENCY_IS_DELISTED("Currency is delisted"),
    DESTINATION_TAG_REQUIRED("Destination tag for this transaction is required"),
    DEPOSITS_DISABLED("Deposits are disabled"),
    ALERT_PRICE_ALREADY_LESS("LESS Price Alert already fired"),
    ALERT_PRICE_ALREADY_MORE("GREATER Price Alert already fired"),
    ALERT_PRICE_EXIST("Price Alert Already Exists"),

    UNKNOWN("Unknown error.")

}