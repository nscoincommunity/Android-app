package com.stocksexchange.api.model.rest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stocksexchange.core.Constants
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("id") val id: Long,
    @SerializedName("currency_pair_id") val currencyPairId: Int,
    @SerializedName("currency_pair_name") val currencyPairName: String,
    @SerializedName("price") val price: Double,
    @SerializedName("trigger_price") val triggerPrice: Double = -1.0,
    @SerializedName("initial_amount") val initialAmount: Double,
    @SerializedName("processed_amount") val filledAmount: Double,
    @SerializedName("type") val typeStr: String,
    @SerializedName("original_type") val originalTypeStr: String = "",
    @SerializedName("timestamp") val timestamp: Long,   // in seconds
    @SerializedName("status") val statusStr: String,
    @SerializedName("trades") private val _trades: List<Trade>? = null,
    @SerializedName("fees") private val _fees: List<Fee>? = null
) : Parcelable, Comparable<Order> {


    companion object {


        fun updateActiveOrderStatus(order: Order, newStatus: OrderStatus): Order {
            return if(newStatus == OrderStatus.FINISHED) {
                order.copy(
                    filledAmount = order.initialAmount,
                    statusStr = newStatus.name
                )
            } else {
                order.copy(statusStr = newStatus.name)
            }
        }


        fun updateActiveOrderStatus(order: Order, newStatusStr: String): Order {
            return updateActiveOrderStatus(
                order = order,
                newStatus = OrderStatus.newInstance(newStatusStr)
            )
        }


        fun cancelActiveOrder(order: Order): Order {
            return updateActiveOrderStatus(
                order = order,
                newStatus = if(order.hasFilledAmount) {
                    if(order.isFilledCompletely) {
                        OrderStatus.FINISHED
                    } else {
                        OrderStatus.PARTIAL
                    }
                } else {
                    OrderStatus.CANCELLED
                }
            )
        }


    }


    val hasCurrencyPairName: Boolean
        get() = (
            currencyPairName.isNotBlank() &&
            hasBaseCurrencySymbol &&
            hasQuoteCurrencySymbol
        )


    val hasBaseCurrencySymbol: Boolean
        get() = baseCurrencySymbol.isNotBlank()


    val hasQuoteCurrencySymbol: Boolean
        get() = quoteCurrencySymbol.isNotBlank()


    val hasTriggerPrice: Boolean
        get() = (triggerPrice > 0.0)


    val hasFilledAmount: Boolean
        get() = (filledAmount > 0.0)


    val hasOriginalType: Boolean
        get() = originalTypeStr.isNotEmpty()


    val hasTrades: Boolean
        get() = trades.isNotEmpty()


    val hasFees: Boolean
        get() = fees.isNotEmpty()


    val isFilledCompletely: Boolean
        get() = (initialAmount == filledAmount)


    val timestampInMillis: Long
        get() = (timestamp * 1000L)


    val baseCurrencySymbol: String
        get() = (currencyPairName.split(Constants.CURRENCY_MARKET_SEPARATOR).firstOrNull() ?: "")


    val quoteCurrencySymbol: String
        get() = (currencyPairName.split(Constants.CURRENCY_MARKET_SEPARATOR).lastOrNull() ?: "")


    val formattedCurrencyPairName: String
        get() = "$baseCurrencySymbol / $quoteCurrencySymbol"


    val type: ApiOrderType
        get() = ApiOrderType.newInstance(typeStr)


    val originalType: ApiOrderType
        get() = ApiOrderType.newInstance(originalTypeStr)


    val status: OrderStatus
        get() = OrderStatus.newInstance(statusStr)


    val trades: List<Trade>
        get() = (_trades ?: listOf())


    val fees: List<Fee>
        get() = (_fees ?: listOf())




    override fun compareTo(other: Order): Int = when {
        (timestamp > other.timestamp) -> 1
        (timestamp < other.timestamp) -> -1
        else -> 0
    }


    @Parcelize
    data class Trade(
        @SerializedName("id") val id: Long,
        @SerializedName("buy_order_id") val buyOrderId: Long,
        @SerializedName("sell_order_id") val sellOrderId: Long,
        @SerializedName("price") val price: Double,
        @SerializedName("amount") val amount: Double,
        @SerializedName("tradeType") val tradeTypeStr: String,
        @SerializedName("timestamp") val timestamp: Long
    ) : Parcelable {

        val tradeType: TradeType
            get() = TradeType.newInstance(tradeTypeStr)

    }


    @Parcelize
    data class Fee(
        @SerializedName("id") val id: Long,
        @SerializedName("currency_id") val currencyId: Int,
        @SerializedName("amount") val amount: Double,
        @SerializedName("timestamp") val timestamp: Long
    ) : Parcelable


}