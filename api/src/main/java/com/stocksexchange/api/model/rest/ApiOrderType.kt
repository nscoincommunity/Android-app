package com.stocksexchange.api.model.rest

enum class ApiOrderType(val typeStr: String) {


    BUY("BUY"),
    SELL("SELL"),

    BUY_STOP_LIMIT("BUY (STOP-LIMIT)"),
    SELL_STOP_LIMIT("SELL (STOP-LIMIT)"),

    UNKNOWN("UNKNOWN");


    val isBuy: Boolean
        get() = ((this == BUY) || (this == BUY_STOP_LIMIT))


    val isSell: Boolean
        get() = ((this == SELL) || (this == SELL_STOP_LIMIT))


    val isStopLimit: Boolean
        get() = ((this == BUY_STOP_LIMIT) || (this == SELL_STOP_LIMIT))


    companion object {


        fun newInstance(typeStr: String): ApiOrderType {
            return when(typeStr) {
                BUY.typeStr -> BUY
                SELL.typeStr -> SELL
                BUY_STOP_LIMIT.typeStr -> BUY_STOP_LIMIT
                SELL_STOP_LIMIT.typeStr -> SELL_STOP_LIMIT

                else -> UNKNOWN
            }
        }


    }


}