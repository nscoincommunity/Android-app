package com.stocksexchange.api.model.rest

enum class OrderStatus {


    ALL,

    PENDING,
    PROCESSING,
    FINISHED,
    PARTIAL,
    CANCELLED,
    WITH_TRADES,    // Combines FINISHED & PARTIAL

    UNKNOWN;


    fun getStatusNames(): List<String> {
        return if(this == WITH_TRADES) {
            COMPLETED_ORDER_STATUSES
        } else {
            listOf(this.name)
        }
    }




    companion object {

        private val COMPLETED_ORDER_STATUSES = listOf(FINISHED.name, PARTIAL.name)


        fun newInstance(statusStr: String): OrderStatus {
            return when(statusStr) {
                PENDING.name -> PENDING
                PROCESSING.name -> PROCESSING
                FINISHED.name -> FINISHED
                PARTIAL.name -> PARTIAL
                CANCELLED.name -> CANCELLED
                WITH_TRADES.name -> WITH_TRADES

                else -> UNKNOWN
            }
        }

    }


}