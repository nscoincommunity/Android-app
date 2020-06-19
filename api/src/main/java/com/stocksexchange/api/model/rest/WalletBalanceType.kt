package com.stocksexchange.api.model.rest

enum class WalletBalanceType(val type: String) {

    CURRENT("BALANCE"),
    FROZEN("FROZEN"),
    BONUS("BONUS"),
    TOTAL("TOTAL")

}