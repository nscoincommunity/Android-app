package com.stocksexchange.api.model.rest

enum class SignInConfirmationType(val abbreviation: String) {


    EMAIL("email"),
    SMS("sms"),
    TWO_FACTOR_AUTHENTICATION("2fa"),

    UNKNOWN("unknown");


    companion object {

        fun newInstance(abbreviation: String): SignInConfirmationType {
            return when(abbreviation) {
                EMAIL.abbreviation -> EMAIL
                SMS.abbreviation -> SMS
                TWO_FACTOR_AUTHENTICATION.abbreviation -> TWO_FACTOR_AUTHENTICATION

                else -> UNKNOWN
            }
        }

    }


}