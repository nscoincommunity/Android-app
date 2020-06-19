package com.stocksexchange.api.model.rest

enum class TradeType {


    BUY,
    SELL;


    companion object {

        fun newInstance(typeStr: String): TradeType {
            return when(typeStr) {
                BUY.name -> BUY
                SELL.name -> SELL

                else -> throw IllegalStateException("Cannot recognize the trade type: $typeStr")
            }
        }

    }


}