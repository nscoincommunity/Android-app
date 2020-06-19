package com.stocksexchange.android.ui.alertprice.additem

import android.os.Bundle
import androidx.core.os.bundleOf
import com.stocksexchange.android.model.PerformedCurrencyMarketActions
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
        currencyMarket = getOrThrow(PresenterStateKeys.KEY_CURRENCY_MARKET),
        performedActions = getOrThrow(PresenterStateKeys.KEY_PERFORMED_ACTIONS)
    )
}


internal object ExtrasKeys {

    const val KEY_CURRENCY_MARKET = "currency_market"

}


internal object PresenterStateKeys {

    const val KEY_CURRENCY_MARKET = "currency_market"
    const val KEY_PERFORMED_ACTIONS = "performed_actions"

}


internal data class Extras(
    val currencyMarket: CurrencyMarket
)


internal data class PresenterState(
    val currencyMarket: CurrencyMarket,
    val performedActions: PerformedCurrencyMarketActions
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_CURRENCY_MARKET, state.currencyMarket)
    save(PresenterStateKeys.KEY_PERFORMED_ACTIONS, state.performedActions)
}


fun AlertPriceAddAFragment.Companion.newArgs(
    currencyMarket: CurrencyMarket
): Bundle {
    return bundleOf(
        ExtrasKeys.KEY_CURRENCY_MARKET to currencyMarket
    )
}