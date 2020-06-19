package com.stocksexchange.api.model.rest

import com.google.gson.annotations.SerializedName

data class OrdersCancellationResponse(
    @SerializedName("put_into_processing_queue") val ordersPutIntoProcessingQueue: List<Order>,
    @SerializedName("not_put_into_processing_queue") val ordersNotPutIntoProcessingQueue: List<Order>,
    @SerializedName("message") val message: String
) {


    val isEmpty: Boolean
        get() = (
            ordersPutIntoProcessingQueue.isEmpty() &&
            ordersNotPutIntoProcessingQueue.isEmpty()
        )




    /**
     * Checks if the specified order was put into processing queue for
     * cancellation.
     *
     * @param id The ID of the order
     *
     * @return true if was put; false otherwise
     */
    fun wasPutIntoProcessingQueue(id: Long): Boolean {
        return ordersPutIntoProcessingQueue.any { it.id == id  }
    }


    /**
     * Checks if the specified order was not put into processing queue
     * for cancellation.
     *
     * @param id The ID of the order
     *
     * @return true if was not put; false otherwise
     */
    fun wasNotPutIntoProcessingQueue(id: Long): Boolean {
        return ordersNotPutIntoProcessingQueue.any { it.id == id }
    }


}