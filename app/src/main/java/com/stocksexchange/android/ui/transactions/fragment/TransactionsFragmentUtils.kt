package com.stocksexchange.android.ui.transactions.fragment

import android.os.Bundle
import androidx.core.os.bundleOf
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.api.model.rest.TransactionType
import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import com.stocksexchange.core.utils.extensions.getParcelableOrThrow


internal val extrasExtractor: (Bundle.() -> Extras) = {
    Extras(
        wasWithdrawalJustConfirmed = getBoolean(ExtrasKeys.KEY_WAS_WITHDRAWAL_JUST_CONFIRMED),
        wasWithdrawalJustCancelled = getBoolean(ExtrasKeys.KEY_WAS_WITHDRAWAL_JUST_CANCELLED),
        transactionParameters = getParcelableOrThrow(ExtrasKeys.KEY_TRANSACTION_PARAMETERS)
    )
}


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        wasWithdrawalJustConfirmed = getOrThrow(PresenterStateKeys.KEY_WAS_WITHDRAWAL_JUST_CONFIRMED),
        wasWithdrawalJustCancelled = getOrThrow(PresenterStateKeys.KEY_WAS_WITHDRAWAL_JUST_CANCELLED),
        isWithdrawalConfirmedDialogAlreadyShown = getOrThrow(PresenterStateKeys.KEY_IS_WITHDRAWAL_CONFIRMED_DIALOG_ALREADY_SHOWN),
        isWithdrawalCancelledDialogAlreadyShown = getOrThrow(PresenterStateKeys.KEY_IS_WITHDRAWAL_CANCELLED_DIALOG_ALREADY_SHOWN),
        transactionParameters = getOrThrow(PresenterStateKeys.KEY_TRANSACTION_PARAMETERS)
    )
}


internal object ExtrasKeys {

    const val KEY_WAS_WITHDRAWAL_JUST_CONFIRMED = "was_withdrawal_just_confirmed"
    const val KEY_WAS_WITHDRAWAL_JUST_CANCELLED = "was_withdrawal_just_cancelled"
    const val KEY_TRANSACTION_PARAMETERS = "transaction_parameters"

}


internal object PresenterStateKeys {

    const val KEY_WAS_WITHDRAWAL_JUST_CONFIRMED = "was_withdrawal_just_confirmed"
    const val KEY_WAS_WITHDRAWAL_JUST_CANCELLED = "was_withdrawal_just_cancelled"
    const val KEY_IS_WITHDRAWAL_CONFIRMED_DIALOG_ALREADY_SHOWN = "is_withdrawal_confirmed_dialog_already_shown"
    const val KEY_IS_WITHDRAWAL_CANCELLED_DIALOG_ALREADY_SHOWN = "is_withdrawal_cancelled_dialog_already_shown"
    const val KEY_TRANSACTION_PARAMETERS = "transaction_parameters"

}


internal data class Extras(
    val wasWithdrawalJustConfirmed: Boolean,
    val wasWithdrawalJustCancelled: Boolean,
    val transactionParameters: TransactionParameters
)


internal data class PresenterState(
    val wasWithdrawalJustConfirmed: Boolean,
    val wasWithdrawalJustCancelled: Boolean,
    val isWithdrawalConfirmedDialogAlreadyShown: Boolean,
    val isWithdrawalCancelledDialogAlreadyShown: Boolean,
    val transactionParameters: TransactionParameters
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_WAS_WITHDRAWAL_JUST_CONFIRMED, state.wasWithdrawalJustConfirmed)
    save(PresenterStateKeys.KEY_WAS_WITHDRAWAL_JUST_CANCELLED, state.wasWithdrawalJustCancelled)
    save(PresenterStateKeys.KEY_IS_WITHDRAWAL_CONFIRMED_DIALOG_ALREADY_SHOWN, state.isWithdrawalConfirmedDialogAlreadyShown)
    save(PresenterStateKeys.KEY_IS_WITHDRAWAL_CANCELLED_DIALOG_ALREADY_SHOWN, state.isWithdrawalCancelledDialogAlreadyShown)
    save(PresenterStateKeys.KEY_TRANSACTION_PARAMETERS, state.transactionParameters)
}


fun TransactionsFragment.Companion.newStandardInstance(
    type: TransactionType,
    wasWithdrawalJustConfirmed: Boolean = false,
    wasWithdrawalJustCancelled: Boolean = false
) : TransactionsFragment {
    return newInstance(
        parameters = TransactionParameters.getStandardParameters(type),
        wasWithdrawalJustConfirmed = wasWithdrawalJustConfirmed,
        wasWithdrawalJustCancelled = wasWithdrawalJustCancelled
    )
}


fun TransactionsFragment.Companion.newSearchInstance(
    type: TransactionType
) : TransactionsFragment {
    return newInstance(
        parameters = TransactionParameters.getSearchParameters(type)
    )
}


fun TransactionsFragment.Companion.newInstance(
    parameters: TransactionParameters,
    wasWithdrawalJustConfirmed: Boolean = false,
    wasWithdrawalJustCancelled: Boolean = false
): TransactionsFragment {
    return TransactionsFragment().apply {
        arguments = bundleOf(
            ExtrasKeys.KEY_WAS_WITHDRAWAL_JUST_CONFIRMED to wasWithdrawalJustConfirmed,
            ExtrasKeys.KEY_WAS_WITHDRAWAL_JUST_CANCELLED to wasWithdrawalJustCancelled,
            ExtrasKeys.KEY_TRANSACTION_PARAMETERS to parameters
        )
    }
}