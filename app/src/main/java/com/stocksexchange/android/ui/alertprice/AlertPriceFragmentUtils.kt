package com.stocksexchange.android.ui.alertprice

import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.parameters.AlertPriceParameters


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        alertPriceParameters = getOrThrow(PresenterStateKeys.KEY_ALERT_PRICE_PARAMETERS)
    )
}


internal object PresenterStateKeys {

    const val KEY_ALERT_PRICE_PARAMETERS = "alert_price_parameters"

}


internal data class PresenterState(
    val alertPriceParameters: AlertPriceParameters
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_ALERT_PRICE_PARAMETERS, state.alertPriceParameters)
}