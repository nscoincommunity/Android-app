package com.stocksexchange.android.ui.transactioncreation.base

import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.parameters.TransactionCreationParameters
import com.stocksexchange.android.events.PerformedWalletActionsEvent
import com.stocksexchange.android.model.PerformedWalletActions
import com.stocksexchange.android.model.TransactionData
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingPresenter
import com.stocksexchange.android.ui.transactioncreation.PresenterState
import com.stocksexchange.android.ui.transactioncreation.presenterStateExtractor
import com.stocksexchange.android.ui.transactioncreation.saveState
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.api.exceptions.rest.WalletException
import org.greenrobot.eventbus.EventBus

/**
 * A base presenter of the MVP architecture that contains
 * common functionality for creating transactions (deposits
 * and withdrawals).
 */
abstract class BaseTransactionCreationPresenter<out V, out M>(
    view: V,
    model: M
) : BaseDataLoadingPresenter<V, M, TransactionData, TransactionCreationParameters>(view, model),
    BaseTransactionCreationModel.BaseTransactionCreationActionListener where
        V : TransactionCreationView,
        M : BaseTransactionCreationModel<*> {


    var transactionData = TransactionData()

    var transactionCreationParams = TransactionCreationParameters.getDefaultParameters()

    protected var mPerformedWalletActions = PerformedWalletActions()




    override fun shouldDisableRpbAfterFirstSuccessfulDataLoading(): Boolean {
        return true
    }


    override fun getErrorViewCaption(exception: Throwable): String {
        return when(exception) {
            is WalletException -> when(exception.error) {
                WalletException.Error.WALLET_CREATION_DELAY -> {
                    mStringProvider.getString(R.string.error_wallet_creation_delay)
                }

                WalletException.Error.UNKNOWN -> exception.message

                else -> super.getErrorViewCaption(exception)
            }

            else -> super.getErrorViewCaption(exception)
        }
    }


    override fun getDataLoadingParams(): TransactionCreationParameters {
        return transactionCreationParams
    }


    override fun onRequestSent(requestType: Int) {
        mView.showToolbarProgressBar()
    }


    override fun onResponseReceived(requestType: Int) {
        mView.hideToolbarProgressBar()
    }


    override fun onRequestFailed(requestType: Int, exception: Throwable, metadata: Any) {
        mView.showToast(mStringProvider.getErrorMessage(exception))
    }


    override fun onNavigateUpPressed(): Boolean {
        if(!mPerformedWalletActions.isEmpty()) {
            EventBus.getDefault().postSticky(PerformedWalletActionsEvent.init(
                performedActions = mPerformedWalletActions,
                source = this
            ))
        }

        return super.onNavigateUpPressed()
    }


    override fun onBackPressed(): Boolean = onNavigateUpPressed()


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            transactionCreationParams = it.transactionCreationParams
            transactionData = it.transactionData
            mPerformedWalletActions = it.performedWalletActions
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            transactionCreationParams = transactionCreationParams,
            transactionData = transactionData,
            performedWalletActions = mPerformedWalletActions
        ))
    }


    override fun toString(): String {
        return "${super.toString()}_${transactionData.wallet.currencySymbol}"
    }


}