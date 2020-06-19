package com.stocksexchange.android.ui.transactioncreation

import android.os.Bundle
import androidx.core.os.bundleOf
import com.stocksexchange.android.model.PerformedWalletActions
import com.stocksexchange.android.model.TransactionData
import com.stocksexchange.android.ui.transactioncreation.base.BaseTransactionCreationFragment
import com.stocksexchange.android.ui.transactioncreation.depositcreation.DepositCreationFragment
import com.stocksexchange.android.ui.transactioncreation.withdrawalcreation.WithdrawalCreationFragment
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.Protocol
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.api.model.rest.parameters.TransactionCreationParameters
import com.stocksexchange.core.utils.extensions.getParcelableOrThrow


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        transactionCreationParams = getParcelableOrThrow(ExtrasKeys.KEY_TRANSACTION_CREATION_PARAMS),
        transactionData = getParcelableOrThrow(ExtrasKeys.KEY_TRANSACTION_DATA)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        transactionCreationParams = getOrThrow(PresenterStateKeys.KEY_TRANSACTION_CREATION_PARAMS),
        transactionData = getOrThrow(PresenterStateKeys.KEY_TRANSACTION_DATA),
        performedWalletActions = getOrThrow(PresenterStateKeys.KEY_PERFORMED_WALLET_ACTIONS)
    )
}


internal object ExtrasKeys {

    const val KEY_TRANSACTION_CREATION_PARAMS = "transaction_creation_params"
    const val KEY_TRANSACTION_DATA = "transaction_data"

}


internal object PresenterStateKeys {

    const val KEY_TRANSACTION_CREATION_PARAMS = "transaction_creation_params"
    const val KEY_TRANSACTION_DATA = "transaction_data"
    const val KEY_PERFORMED_WALLET_ACTIONS = "performed_wallet_actions"

}


internal data class Extras(
    val transactionCreationParams: TransactionCreationParameters,
    val transactionData: TransactionData
)


internal data class PresenterState(
    val transactionCreationParams: TransactionCreationParameters,
    val transactionData: TransactionData,
    val performedWalletActions: PerformedWalletActions
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_TRANSACTION_CREATION_PARAMS, state.transactionCreationParams)
    save(PresenterStateKeys.KEY_TRANSACTION_DATA, state.transactionData)
    save(PresenterStateKeys.KEY_PERFORMED_WALLET_ACTIONS, state.performedWalletActions)
}


fun DepositCreationFragment.Companion.newArgs(
    wallet: Wallet,
    protocol: Protocol = Protocol.STUB_PROTOCOL
) : Bundle = BaseTransactionCreationFragment.newArgs(wallet, protocol)


fun WithdrawalCreationFragment.Companion.newArgs(
    wallet: Wallet,
    protocol: Protocol = Protocol.STUB_PROTOCOL
) : Bundle = BaseTransactionCreationFragment.newArgs(wallet, protocol)


fun BaseTransactionCreationFragment.Companion.newArgs(
    wallet: Wallet,
    protocol: Protocol = Protocol.STUB_PROTOCOL
) : Bundle {
    return bundleOf(
        ExtrasKeys.KEY_TRANSACTION_CREATION_PARAMS to TransactionCreationParameters.getDefaultParameters(wallet, protocol),
        ExtrasKeys.KEY_TRANSACTION_DATA to TransactionData(wallet)
    )
}