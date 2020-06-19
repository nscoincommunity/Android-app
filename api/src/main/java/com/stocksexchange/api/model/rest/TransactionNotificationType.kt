package com.stocksexchange.api.model.rest

enum class TransactionNotificationType(val message: String) {


    DEPOSITS_DISABLED("deposits are disabled for this currency"),
    UNKNOWN("unknown");


    companion object {

        fun newInstance(message: String): TransactionNotificationType {
            return when(message) {
                DEPOSITS_DISABLED.message -> DEPOSITS_DISABLED

                else -> UNKNOWN
            }
        }

    }


}