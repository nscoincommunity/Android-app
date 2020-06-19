package com.stocksexchange.android.ui.orderbook

import android.os.Bundle
import androidx.core.os.bundleOf
import com.stocksexchange.android.model.PerformedOrderActions
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.core.utils.extensions.getParcelableOrThrow


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        currencyMarket = getParcelableOrThrow(ExtrasKeys.KEY_CURRENCY_MARKET)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        isAppBarScrollingEnabled = getOrThrow(PresenterStateKeys.KEY_IS_APP_BAR_SCROLLING_ENABLED),
        isRealTimeDataUpdateEventReceived = getOrThrow(PresenterStateKeys.KEY_IS_REAL_TIME_DATA_UPDATE_EVENT_RECEIVED),
        isDataLoadingPerformed = getOrThrow(PresenterStateKeys.KEY_IS_DATA_LOADING_PERFORMED),
        currencyMarket = getOrThrow(PresenterStateKeys.KEY_CURRENCY_MARKET),
        performedOrderActions = getOrThrow(PresenterStateKeys.KEY_PERFORMED_ORDER_ACTIONS)
    )
}


internal object ExtrasKeys {

    const val KEY_CURRENCY_MARKET = "currency_market"

}


internal object PresenterStateKeys {

    const val KEY_IS_APP_BAR_SCROLLING_ENABLED = "is_app_bar_scrolling_enabled"
    const val KEY_IS_REAL_TIME_DATA_UPDATE_EVENT_RECEIVED = "is_real_time_data_update_event_received"
    const val KEY_IS_DATA_LOADING_PERFORMED = "is_data_loading_performed"
    const val KEY_CURRENCY_MARKET = "currency_market"
    const val KEY_PERFORMED_ORDER_ACTIONS = "performed_order_actions"

}


internal data class Extras(
    val currencyMarket: CurrencyMarket
)


internal data class PresenterState(
    val isAppBarScrollingEnabled: Boolean,
    val isRealTimeDataUpdateEventReceived: Boolean,
    val isDataLoadingPerformed: Boolean,
    val currencyMarket: CurrencyMarket,
    val performedOrderActions: PerformedOrderActions
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_IS_APP_BAR_SCROLLING_ENABLED, state.isAppBarScrollingEnabled)
    save(PresenterStateKeys.KEY_IS_REAL_TIME_DATA_UPDATE_EVENT_RECEIVED, state.isRealTimeDataUpdateEventReceived)
    save(PresenterStateKeys.KEY_IS_DATA_LOADING_PERFORMED, state.isDataLoadingPerformed)
    save(PresenterStateKeys.KEY_CURRENCY_MARKET, state.currencyMarket)
    save(PresenterStateKeys.KEY_PERFORMED_ORDER_ACTIONS, state.performedOrderActions)
}


fun OrderbookFragment.Companion.newArgs(
    currencyMarket: CurrencyMarket
): Bundle {
    return bundleOf(
        ExtrasKeys.KEY_CURRENCY_MARKET to currencyMarket
    )
}