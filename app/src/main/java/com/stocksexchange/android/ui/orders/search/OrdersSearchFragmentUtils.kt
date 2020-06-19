package com.stocksexchange.android.ui.orders.search

import android.os.Bundle
import androidx.core.os.bundleOf
import com.stocksexchange.android.model.PerformedOrderActions
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.OrderLifecycleType
import com.stocksexchange.core.utils.extensions.getSerializableOrThrow


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        orderLifecycleType = getSerializableOrThrow(ExtrasKeys.KEY_ORDER_LIFECYCLE_TYPE)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        orderLifecycleType = getOrThrow(PresenterStateKeys.KEY_ORDER_LIFECYCLE_TYPE),
        performedOrderActions = getOrThrow(PresenterStateKeys.KEY_PERFORMED_ORDER_ACTIONS)
    )
}


internal object ExtrasKeys {

    const val KEY_ORDER_LIFECYCLE_TYPE = "order_lifecycle_type"

}


internal object PresenterStateKeys {

    const val KEY_ORDER_LIFECYCLE_TYPE = "order_lifecycle_type"
    const val KEY_PERFORMED_ORDER_ACTIONS = "performed_order_actions"

}


internal data class Extras(
    val orderLifecycleType: OrderLifecycleType
)


internal data class PresenterState(
    val orderLifecycleType: OrderLifecycleType,
    val performedOrderActions: PerformedOrderActions
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_ORDER_LIFECYCLE_TYPE, state.orderLifecycleType)
    save(PresenterStateKeys.KEY_PERFORMED_ORDER_ACTIONS, state.performedOrderActions)
}


fun OrdersSearchFragment.Companion.newArgs(
    orderLifecycleType: OrderLifecycleType
) : Bundle {
    return bundleOf(
        ExtrasKeys.KEY_ORDER_LIFECYCLE_TYPE to orderLifecycleType
    )
}