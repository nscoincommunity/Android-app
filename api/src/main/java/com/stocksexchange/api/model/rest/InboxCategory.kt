package com.stocksexchange.api.model.rest

enum class InboxCategory(val title: String) {

    WITHDRAWAL("withdrawal"),
    ORDER("order"),
    DEPOSIT("deposit"),
    BALANCE("balance"),
    ACCOUNT("account"),
    NONE("none")

}