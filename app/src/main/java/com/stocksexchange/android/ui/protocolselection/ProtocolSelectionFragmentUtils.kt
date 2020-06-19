package com.stocksexchange.android.ui.protocolselection

import android.os.Bundle
import androidx.core.os.bundleOf
import com.stocksexchange.android.model.PerformedWalletActions
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.TransactionType
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.core.utils.extensions.getParcelableOrThrow
import com.stocksexchange.core.utils.extensions.getSerializableOrThrow


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        transactionType = getSerializableOrThrow(ExtrasKeys.KEY_TRANSACTION_TYPE),
        wallet = getParcelableOrThrow(ExtrasKeys.KEY_WALLET)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        transactionType = getOrThrow(PresenterStateKeys.KEY_TRANSACTION_TYPE),
        wallet = getOrThrow(PresenterStateKeys.KEY_WALLET),
        performedWalletActions = getOrThrow(PresenterStateKeys.KEY_PERFORMED_WALLET_ACTIONS)
    )
}


internal object ExtrasKeys {

    const val KEY_TRANSACTION_TYPE = "transaction_type"
    const val KEY_WALLET = "wallet"

}


internal object PresenterStateKeys {

    const val KEY_TRANSACTION_TYPE = "transaction_type"
    const val KEY_WALLET = "wallet"
    const val KEY_PERFORMED_WALLET_ACTIONS = "performed_wallet_actions"

}


internal data class Extras(
    val transactionType: TransactionType,
    val wallet: Wallet
)


internal data class PresenterState(
    val transactionType: TransactionType,
    val wallet: Wallet,
    val performedWalletActions: PerformedWalletActions
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_TRANSACTION_TYPE, state.transactionType)
    save(PresenterStateKeys.KEY_WALLET, state.wallet)
    save(PresenterStateKeys.KEY_PERFORMED_WALLET_ACTIONS, state.performedWalletActions)
}


fun ProtocolSelectionFragment.Companion.newArgs(
    transactionType: TransactionType,
    wallet: Wallet
) : Bundle {
    return bundleOf(
        ExtrasKeys.KEY_TRANSACTION_TYPE to transactionType,
        ExtrasKeys.KEY_WALLET to wallet
    )
}