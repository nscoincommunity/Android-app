package com.stocksexchange.android.ui.currencymarkets.fragment

import android.os.Bundle
import androidx.core.os.bundleOf
import com.stocksexchange.android.model.CurrencyMarketParameters
import com.stocksexchange.android.model.CurrencyMarketType
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.CurrencyPairGroup
import com.stocksexchange.core.utils.extensions.getParcelableOrThrow


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        currencyMarketParameters = getParcelableOrThrow(ExtrasKeys.KEY_CURRENCY_MARKET_PARAMS)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        isDataSetSorted = getOrThrow(PresenterStateKeys.KEY_IS_DATA_SET_SORTED),
        currencyMarketParameters = getOrThrow(PresenterStateKeys.KEY_CURRENCY_MARKET_PARAMS)
    )
}


internal object ExtrasKeys {

    const val KEY_CURRENCY_MARKET_PARAMS = "currency_market_params"

}


internal object PresenterStateKeys {

    const val KEY_IS_DATA_SET_SORTED = "is_data_set_sorted"
    const val KEY_CURRENCY_MARKET_PARAMS = "currency_market_params"

}


internal data class Extras(
    val currencyMarketParameters: CurrencyMarketParameters
)


internal data class PresenterState(
    val isDataSetSorted: Boolean,
    val currencyMarketParameters: CurrencyMarketParameters
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_IS_DATA_SET_SORTED, state.isDataSetSorted)
    save(PresenterStateKeys.KEY_CURRENCY_MARKET_PARAMS, state.currencyMarketParameters)
}


fun CurrencyMarketsFragment.Companion.newNormalInstance(
    currencyPairGroup: CurrencyPairGroup
) : CurrencyMarketsFragment {
    return newInstance(
        currencyMarketType = CurrencyMarketType.NORMAL,
        currencyPairGroup = currencyPairGroup
    )
}


fun CurrencyMarketsFragment.Companion.newFavoritesInstance(): CurrencyMarketsFragment {
    return newInstance(currencyMarketType = CurrencyMarketType.FAVORITES)
}


fun CurrencyMarketsFragment.Companion.newSearchInstance(): CurrencyMarketsFragment {
    return newInstance(currencyMarketType = CurrencyMarketType.SEARCH)
}


fun CurrencyMarketsFragment.Companion.newInstance(
    currencyMarketType: CurrencyMarketType,
    currencyPairGroup: CurrencyPairGroup = CurrencyPairGroup.STUB
) : CurrencyMarketsFragment {
    return CurrencyMarketsFragment().apply {
        val params = CurrencyMarketParameters.getDefaultParameters().copy(
            currencyMarketType = currencyMarketType,
            currencyPairGroup = currencyPairGroup
        )

        arguments = bundleOf(
            ExtrasKeys.KEY_CURRENCY_MARKET_PARAMS to params
        )
    }
}