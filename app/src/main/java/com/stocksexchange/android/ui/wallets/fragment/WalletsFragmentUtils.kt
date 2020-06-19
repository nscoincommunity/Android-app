package com.stocksexchange.android.ui.wallets.fragment

import android.os.Bundle
import androidx.core.os.bundleOf
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.parameters.WalletParameters
import com.stocksexchange.core.utils.extensions.getParcelableOrThrow


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        walletParameters = getParcelableOrThrow(ExtrasKeys.KEY_WALLET_PARAMETERS)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        walletParameters = getOrThrow(PresenterStateKeys.KEY_WALLET_PARAMETERS)
    )
}


internal object ExtrasKeys {

    const val KEY_WALLET_PARAMETERS = "wallet_parameters"

}


internal object PresenterStateKeys {

    const val KEY_WALLET_PARAMETERS = "wallet_parameters"

}


internal data class Extras(
    val walletParameters: WalletParameters
)


internal data class PresenterState(
    val walletParameters: WalletParameters
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_WALLET_PARAMETERS, state.walletParameters)
}


fun WalletsFragment.Companion.newInstance(
    walletParameters: WalletParameters
): WalletsFragment {
    return WalletsFragment().apply {
        arguments = bundleOf(ExtrasKeys.KEY_WALLET_PARAMETERS to walletParameters)
    }
}


fun WalletsFragment.Companion.newStandardInstance(): WalletsFragment {
    return newInstance(WalletParameters.getStandardParameters())
}


fun WalletsFragment.Companion.newSearchInstance(): WalletsFragment {
    return newInstance(WalletParameters.getSearchParameters())
}