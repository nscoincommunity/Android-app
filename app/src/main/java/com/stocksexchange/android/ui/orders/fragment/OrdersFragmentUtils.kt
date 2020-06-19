package com.stocksexchange.android.ui.orders.fragment

import android.os.Bundle
import androidx.core.os.bundleOf
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.OrderLifecycleType
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import com.stocksexchange.core.utils.extensions.getParcelableOrThrow


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        orderParameters = getParcelableOrThrow(ExtrasKeys.KEY_ORDER_PARAMETERS)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        orderParameters = getOrThrow(PresenterStateKeys.KEY_ORDER_PARAMETERS)
    )
}


internal object ExtrasKeys {

    const val KEY_ORDER_PARAMETERS = "order_parameters"

}


internal object PresenterStateKeys {

    const val KEY_ORDER_PARAMETERS = "order_parameters"

}


internal data class Extras(
    val orderParameters: OrderParameters
)


internal data class PresenterState(
    val orderParameters: OrderParameters
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_ORDER_PARAMETERS, state.orderParameters)
}


fun OrdersFragment.Companion.newAllActiveOrdersInstance(): OrdersFragment {
    return newInstance(OrderParameters.getAllActiveOrdersParams())
}


fun OrdersFragment.Companion.newSpecificActiveOrdersInstance(
    currencyPairId: Int
) : OrdersFragment {
    return newInstance(OrderParameters.getSpecificActiveOrdersParams(currencyPairId))
}


fun OrdersFragment.Companion.newSpecificCompletedOrdersInstance(
    currencyPairId: Int
) : OrdersFragment {
    return newInstance(OrderParameters.getSpecificCompletedOrdersParams(currencyPairId))
}


fun OrdersFragment.Companion.newAllCompletedOrdersInstance(): OrdersFragment {
    return newInstance(OrderParameters.getAllCompletedOrdersParams())
}


fun OrdersFragment.Companion.newAllCancelledOrdersInstance(): OrdersFragment {
    return newInstance(OrderParameters.getAllCancelledOrdersParams())
}


fun OrdersFragment.Companion.newSpecificCancelledOrdersInstance(
    currencyPairId: Int
) : OrdersFragment {
    return newInstance(OrderParameters.getSpecificCancelledOrdersParams(currencyPairId))
}


fun OrdersFragment.Companion.newSearchInstance(
    type: OrderLifecycleType
) : OrdersFragment {
    return newInstance(OrderParameters.getSearchOrdersParameters(type))
}


fun OrdersFragment.Companion.newInstance(
    parameters: OrderParameters
) : OrdersFragment {
    return OrdersFragment().apply {
        arguments = bundleOf(
            ExtrasKeys.KEY_ORDER_PARAMETERS to parameters
        )
    }
}