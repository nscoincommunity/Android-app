package com.stocksexchange.android.ui.transactions.fragment

import com.stocksexchange.android.R
import com.stocksexchange.android.events.ReloadWalletsEvent
import com.stocksexchange.api.model.rest.Transaction
import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import com.stocksexchange.api.model.rest.TransactionType
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.model.MaterialDialogBuilder
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingPresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.api.exceptions.rest.WithdrawalProcessingException
import com.stocksexchange.core.handlers.ClipboardHandler
import org.greenrobot.eventbus.EventBus

class TransactionsPresenter(
    view: TransactionsContract.View,
    model: TransactionsModel,
    private val clipboardHandler: ClipboardHandler
) : BaseListDataLoadingPresenter<
    TransactionsContract.View,
    TransactionsModel,
    List<Transaction>,
    TransactionParameters
    >(view, model), TransactionsContract.ActionListener, TransactionsModel.ActionListener {


    var wasWithdrawalJustConfirmed: Boolean = false
    var wasWithdrawalJustCancelled: Boolean = false

    var transactionParameters = TransactionParameters.getDefaultParameters()

    private var mIsWithdrawalConfirmedDialogAlreadyShown: Boolean = false
    private var mIsWithdrawalCancelledDialogAlreadyShown: Boolean = false




    init {
        model.setActionListener(this)
    }


    override fun start() {
        super.start()

        showWithdrawalConfirmedDialogIfNeeded()
        showWithdrawalCancelledDialogIfNeeded()
    }


    override fun onRefreshData() {
        resetParameters()
        mView.clearAdapter()

        super.onRefreshData()
    }


    private fun showWithdrawalConfirmedDialogIfNeeded() {
        if(!wasWithdrawalJustConfirmed || mIsWithdrawalConfirmedDialogAlreadyShown) {
            return
        }

        mIsWithdrawalConfirmedDialogAlreadyShown = true

        showWithdrawalConfirmedDialog()
    }


    private fun showWithdrawalConfirmedDialog() {
        val title = mStringProvider.getString(
            R.string.withdrawals_withdrawal_confirmed_dialog_title
        )
        val content = mStringProvider.getString(
            R.string.withdrawals_withdrawal_confirmed_dialog_message
        )

        showInfoDialog(
            title = title,
            content = content
        )
    }


    private fun showWithdrawalCancelledDialogIfNeeded() {
        if(!wasWithdrawalJustCancelled || mIsWithdrawalCancelledDialogAlreadyShown) {
            return
        }

        mIsWithdrawalCancelledDialogAlreadyShown = true

        showWithdrawalCancelledDialog()
    }


    override fun getSearchQuery(): String = transactionParameters.searchQuery


    override fun onSearchQueryChanged(query: String) {
        transactionParameters = transactionParameters.copy(searchQuery = query)
    }


    override fun getDataTypeForTrigger(trigger: DataLoadingTrigger): DataType {
        return when(trigger) {
            DataLoadingTrigger.START -> DataType.NEW_DATA

            else -> super.getDataTypeForTrigger(trigger)
        }
    }


    override fun getEmptyViewCaption(params: TransactionParameters): String {
        return mStringProvider.getTransactionsEmptyCaption(params)
    }


    override fun getDataLoadingParams(): TransactionParameters {
        return transactionParameters
    }


    override fun onTransactionAddressClicked(transaction: Transaction) {
        copyTextToClipboard(transaction.withdrawal.addressData.addressParameterValue)
    }


    override fun onTransactionExplorerIdClicked(transaction: Transaction) {
        if(transaction.hasBlockExplorerUrl) {
            mView.launchBrowser(transaction.transactionExplorerUrl)
        } else {
            copyTextToClipboard(transaction.transactionExplorerId)
        }
    }


    private fun copyTextToClipboard(text: String) {
        clipboardHandler.copyText(text)

        mView.showToast(mStringProvider.getString(R.string.copied_to_clipboard))
    }


    override fun onLeftActionButtonClicked(transaction: Transaction) {
        if((transaction.type != TransactionType.WITHDRAWALS) ||
            !transaction.withdrawal.isNotConfirmed) {
            return
        }

        mModel.performWithdrawalConfirmationEmailSending(transaction.id)
    }


    override fun onRightActionButtonClicked(transaction: Transaction) {
        if((transaction.type != TransactionType.WITHDRAWALS) ||
            !transaction.withdrawal.isNotConfirmed) {
            return
        }

        showWithdrawalCancellationConfirmationDialog(transaction)
    }


    private fun showWithdrawalCancellationConfirmationDialog(transaction: Transaction) {
        val title = mStringProvider.getString(
            R.string.withdrawals_withdrawal_cancellation_confirmation_dialog_title
        )
        val content = mStringProvider.getString(
            R.string.withdrawals_withdrawal_cancellation_confirmation_dialog_message
        )

        mView.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
            title = title,
            content = content,
            negativeBtnText = mStringProvider.getString(R.string.no),
            positiveBtnText = mStringProvider.getString(R.string.yes),
            positiveBtnClick = {
                onWithdrawalCancellationConfirmed(transaction)
            }
        ))
    }


    private fun onWithdrawalCancellationConfirmed(transaction: Transaction) {
        mModel.performWithdrawalCancellation(transaction.id, transaction)
    }


    override fun onRequestSent(requestType: Int) {
        mView.showSecondaryProgressBar()
    }


    override fun onResponseReceived(requestType: Int) {
        mView.hideSecondaryProgressBar()
    }


    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        when(requestType) {

            TransactionsModel.REQUEST_TYPE_WITHDRAWAL_CONFIRMATION_EMAIL -> {
                onWithdrawalConfirmationEmailSendingSucceeded()
            }

            TransactionsModel.REQUEST_TYPE_WITHDRAWAL_CANCELLATION -> {
                onWithdrawalCancellationSucceeded(metadata as Transaction)
            }

        }
    }


    private fun onWithdrawalConfirmationEmailSendingSucceeded() {
        showWithdrawalConfirmationEmailResentDialog()
    }


    private fun showWithdrawalConfirmationEmailResentDialog() {
        val title = mStringProvider.getString(
            R.string.withdrawals_confirmation_email_resent_dialog_title
        )
        val content = mStringProvider.getString(
            R.string.withdrawals_confirmation_email_resent_dialog_message
        )

        showInfoDialog(
            title = title,
            content = content
        )
    }


    private fun onWithdrawalCancellationSucceeded(transaction: Transaction) {
        val updatedWithdrawal = transaction.withdrawal.toCancelledByUser()
        val updatedTransaction = transaction.copy(withdrawal = updatedWithdrawal)

        mModel.saveWithdrawal(updatedWithdrawal) {
            // Showing the dialog
            showWithdrawalCancelledDialog()

            // Updating the UI
            mView.updateItem(
                position = mView.getItemPosition(updatedTransaction) ?: 0,
                item = updatedTransaction
            )
        }

        postReloadWalletsEvent()
    }


    private fun postReloadWalletsEvent() {
        mView.postActionDelayed(1800) {
            if (EventBus.getDefault().hasSubscriberForEvent(ReloadWalletsEvent::class.java)) {
                EventBus.getDefault().post(ReloadWalletsEvent.reloadWallets(this))
            }
        }
    }


    private fun showWithdrawalCancelledDialog() {
        showInfoDialog(
            title = mStringProvider.getString(R.string.withdrawals_withdrawal_cancelled_dialog_title),
            content = mStringProvider.getString(R.string.withdrawals_withdrawal_cancelled_dialog_message)
        )
    }


    override fun onRequestFailed(requestType: Int, exception: Throwable, metadata: Any) {
        when(requestType) {

            TransactionsModel.REQUEST_TYPE_WITHDRAWAL_CONFIRMATION_EMAIL -> {
                onWithdrawalConfirmationEmailSendingFailed(exception)
            }

            TransactionsModel.REQUEST_TYPE_WITHDRAWAL_CANCELLATION -> {
                onWithdrawalCancellationFailed(exception)
            }

        }
    }


    private fun onWithdrawalConfirmationEmailSendingFailed(exception: Throwable) {
        onErrorReceived(exception)
    }


    private fun onWithdrawalCancellationFailed(exception: Throwable) {
        onErrorReceived(exception)
    }


    private fun onErrorReceived(exception: Throwable) {
        showErrorDialog(when(exception) {
            is WithdrawalProcessingException -> when(exception.error) {
                WithdrawalProcessingException.Error.PROCESSING,
                WithdrawalProcessingException.Error.NOT_FOUND -> {
                    mStringProvider.getString(R.string.error_withdrawal_invalid_state)
                }

                WithdrawalProcessingException.Error.UNKNOWN -> exception.message
            }

            else -> mStringProvider.getErrorMessage(exception)
        })
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            wasWithdrawalJustConfirmed = it.wasWithdrawalJustConfirmed
            wasWithdrawalJustCancelled = it.wasWithdrawalJustCancelled
            mIsWithdrawalConfirmedDialogAlreadyShown = it.isWithdrawalConfirmedDialogAlreadyShown
            mIsWithdrawalCancelledDialogAlreadyShown = it.isWithdrawalCancelledDialogAlreadyShown
            transactionParameters = it.transactionParameters
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            wasWithdrawalJustConfirmed = wasWithdrawalJustConfirmed,
            wasWithdrawalJustCancelled = wasWithdrawalJustCancelled,
            isWithdrawalConfirmedDialogAlreadyShown = mIsWithdrawalConfirmedDialogAlreadyShown,
            isWithdrawalCancelledDialogAlreadyShown = mIsWithdrawalCancelledDialogAlreadyShown,
            transactionParameters = transactionParameters
        ))
    }


    override fun toString(): String {
        val mode = transactionParameters.mode.name
        val type = transactionParameters.type.name

        return "${super.toString()}_${mode}_$type"
    }


}