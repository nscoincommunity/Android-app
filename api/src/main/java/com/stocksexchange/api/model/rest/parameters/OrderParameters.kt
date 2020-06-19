package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.core.utils.interfaces.HasUniqueKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderParameters(
    val mode: OrderMode,
    val lifecycleType: OrderLifecycleType,
    val status: OrderStatus,
    val selectivityType: OrderSelectivityType,
    val sortOrder: SortOrder,
    val searchQuery: String,
    val currencyPairId: Int,
    val limit: Int,
    val offset: Int
) : Parcelable, HasUniqueKey {


    companion object {

        private const val DEFAULT_CURRENCY_PAIR_ID = -1
        private const val DEFAULT_LIMIT = 100


        fun getAllActiveOrdersParams(): OrderParameters {
            return getActiveOrdersParams(
                selectivityType = OrderSelectivityType.ANY_PAIR_ID,
                currencyPairId = DEFAULT_CURRENCY_PAIR_ID
            )
        }


        fun getSpecificActiveOrdersParams(currencyPairId: Int): OrderParameters {
            return getActiveOrdersParams(
                selectivityType = OrderSelectivityType.SPECIFIC_PAIR_ID,
                currencyPairId = currencyPairId
            )
        }


        fun getActiveOrdersParams(selectivityType: OrderSelectivityType,
                                  currencyPairId: Int): OrderParameters {
            return getDefaultParameters().copy(
                lifecycleType = OrderLifecycleType.ACTIVE,
                status = OrderStatus.PENDING,
                selectivityType = selectivityType,
                currencyPairId = currencyPairId
            )
        }


        fun getAllCompletedOrdersParams(): OrderParameters {
            return getCompletedOrdersParams(
                selectivityType = OrderSelectivityType.ANY_PAIR_ID,
                currencyPairId = DEFAULT_CURRENCY_PAIR_ID
            )
        }


        fun getSpecificCompletedOrdersParams(currencyPairId: Int): OrderParameters {
            return getCompletedOrdersParams(
                selectivityType = OrderSelectivityType.SPECIFIC_PAIR_ID,
                currencyPairId = currencyPairId
            )
        }


        fun getCompletedOrdersParams(selectivityType: OrderSelectivityType,
                                     currencyPairId: Int): OrderParameters {
            return getDefaultParameters().copy(
                lifecycleType = OrderLifecycleType.COMPLETED,
                status = OrderStatus.WITH_TRADES,
                selectivityType = selectivityType,
                currencyPairId = currencyPairId
            )
        }


        fun getAllCancelledOrdersParams(): OrderParameters {
            return getCancelledOrdersParams(
                selectivityType = OrderSelectivityType.ANY_PAIR_ID,
                currencyPairId = DEFAULT_CURRENCY_PAIR_ID
            )
        }


        fun getSpecificCancelledOrdersParams(currencyPairId: Int): OrderParameters {
            return getCancelledOrdersParams(
                selectivityType = OrderSelectivityType.SPECIFIC_PAIR_ID,
                currencyPairId = currencyPairId
            )
        }


        fun getCancelledOrdersParams(selectivityType: OrderSelectivityType,
                                     currencyPairId: Int): OrderParameters {
            return getDefaultParameters().copy(
                lifecycleType = OrderLifecycleType.CANCELLED,
                status = OrderStatus.CANCELLED,
                selectivityType = selectivityType,
                currencyPairId = currencyPairId
            )
        }


        fun getSearchOrdersParameters(lifecycleType: OrderLifecycleType): OrderParameters {
            return when(lifecycleType) {
                OrderLifecycleType.ACTIVE -> getAllActiveOrdersParams()
                OrderLifecycleType.COMPLETED -> getAllCompletedOrdersParams()
                OrderLifecycleType.CANCELLED -> getAllCancelledOrdersParams()
            }.copy(mode = OrderMode.SEARCH)
        }


        fun getDefaultParameters(): OrderParameters {
            return OrderParameters(
                mode = OrderMode.STANDARD,
                lifecycleType = OrderLifecycleType.ACTIVE,
                status = OrderStatus.UNKNOWN,
                selectivityType = OrderSelectivityType.ANY_PAIR_ID,
                sortOrder = SortOrder.DESC,
                searchQuery = "",
                currencyPairId = DEFAULT_CURRENCY_PAIR_ID,
                limit = DEFAULT_LIMIT,
                offset = 0
            )
        }

    }


    val hasCurrencyPairId: Boolean
        get() = (currencyPairId != DEFAULT_CURRENCY_PAIR_ID)


    val hasOffset: Boolean
        get() = (offset > 0)


    val currencyPaIrIdOrNull: Int?
        get() = (if(hasCurrencyPairId) currencyPairId else null)


    val currencyPairIdStrOrEmptyStr: String
        get() = (if(hasCurrencyPairId) currencyPairId.toString() else "")


    val lowercasedSearchQuery: String
        get() = searchQuery.toLowerCase()


    override val uniqueKey: String
        get() = (status.name + limit + offset)




    fun resetOffset(): OrderParameters {
        return copy(offset = 0)
    }


    fun increaseOffset(value: Int): OrderParameters {
        return copy(offset = (offset + value))
    }


}