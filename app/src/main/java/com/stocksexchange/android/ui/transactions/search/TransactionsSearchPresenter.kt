package com.stocksexchange.android.ui.transactions.search

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.api.model.rest.TransactionType

class TransactionsSearchPresenter(
    view: TransactionsSearchContract.View,
    model: StubModel
) : BasePresenter<TransactionsSearchContract.View, StubModel>(view, model),
    TransactionsSearchContract.ActionListener {


    var transactionType: TransactionType = TransactionType.DEPOSITS




    override fun onBackPressed(): Boolean {
        return onNavigateUpPressed()
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            transactionType = it.transactionType
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            transactionType = transactionType
        ))
    }


}