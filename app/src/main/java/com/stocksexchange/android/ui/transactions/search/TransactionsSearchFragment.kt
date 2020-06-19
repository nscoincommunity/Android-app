package com.stocksexchange.android.ui.transactions.search

import android.os.Bundle
import android.text.InputType
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.TransactionType
import com.stocksexchange.android.ui.base.fragments.BaseSearchFragment
import com.stocksexchange.android.ui.transactions.fragment.TransactionsFragment
import com.stocksexchange.android.ui.transactions.fragment.newSearchInstance
import com.stocksexchange.android.ui.views.toolbars.SearchToolbar
import com.stocksexchange.core.utils.extensions.extract
import kotlinx.android.synthetic.main.transactions_search_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class TransactionsSearchFragment : BaseSearchFragment<TransactionsFragment, TransactionsSearchPresenter>(),
    TransactionsSearchContract.View {


    companion object {}


    override val mPresenter: TransactionsSearchPresenter by inject { parametersOf(this) }




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.transactionType = it.transactionType
        }
    }


    override fun performSearch(query: String) {
        mFragment.onPerformSearch(query)
    }


    override fun cancelSearch() {
        mFragment.onCancelSearch()
    }


    override fun getInputHint(): String {
        return getStr(when(mPresenter.transactionType) {
            TransactionType.DEPOSITS -> R.string.search_deposits
            TransactionType.WITHDRAWALS -> R.string.search_withdrawals
        })
    }


    override fun getInputType(): Int {
        return (super.getInputType() or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS)
    }


    override fun getSearchToolbar(): SearchToolbar = mRootView.searchToolbar


    override fun getActivityFragment(): TransactionsFragment {
        return TransactionsFragment.newSearchInstance(mPresenter.transactionType)
    }


    override fun getContentLayoutResourceId(): Int = R.layout.transactions_search_fragment_layout


}