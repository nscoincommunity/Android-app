package com.stocksexchange.android.ui.transactions.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.Transaction
import com.stocksexchange.android.mappings.mapToTransactionItem
import com.stocksexchange.android.mappings.mapToTransactionItemList
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingFragment
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.utils.diffcallbacks.TransactionsDiffCallback
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.utils.handlers.BrowserHandler
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.core.decorators.CardViewItemDecorator
import com.stocksexchange.core.utils.extensions.ctx
import com.stocksexchange.core.utils.extensions.dimenInPx
import com.stocksexchange.core.utils.extensions.extract
import com.stocksexchange.core.utils.extensions.postActionDelayed
import com.stocksexchange.core.utils.listeners.ProgressBarListener
import kotlinx.android.synthetic.main.transactions_fragment_layout.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class TransactionsFragment : BaseListDataLoadingFragment<
    TransactionsPresenter,
    List<Transaction>,
    TransactionItem,
    TransactionsRecyclerViewAdapter
    >(), TransactionsContract.View {


    companion object {}


    override val mPresenter: TransactionsPresenter by inject { parametersOf(this) }

    var toolbarProgressBarListener: ProgressBarListener? = null




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.wasWithdrawalJustConfirmed = it.wasWithdrawalJustConfirmed
            mPresenter.wasWithdrawalJustCancelled = it.wasWithdrawalJustCancelled
            mPresenter.transactionParameters = it.transactionParameters
        }
    }


    override fun init() {
        super.init()

        initContentContainer()
    }


    override fun initAdapter(): TransactionsRecyclerViewAdapter {
        return TransactionsRecyclerViewAdapter(ctx, mItems).apply {
            setResources(TransactionResources.newInstance(
                context = ctx,
                stringProvider = mStringProvider,
                timeFormatter = get(),
                numberFormatter = get(),
                settings = getSettings()
            ))

            onTransactionAddressClickListener = { _, transactionItem, _ ->
                mPresenter.onTransactionAddressClicked(transactionItem.itemModel)
            }
            onTransactionExplorerIdClickListener = { _, transactionItem, _ ->
                mPresenter.onTransactionExplorerIdClicked(transactionItem.itemModel)
            }
            onLeftActionButtonClickListener = { _, transactionItem, _ ->
                mPresenter.onLeftActionButtonClicked(transactionItem.itemModel)
            }
            onRightActionButtonClickListener = { _, transactionItem, _ ->
                mPresenter.onRightActionButtonClicked(transactionItem.itemModel)
            }
        }
    }


    private fun initContentContainer() = with(mRootView.contentContainerFl) {
        ThemingUtil.Transactions.contentContainer(this, getAppTheme())
    }


    override fun addData(data: List<Transaction>) {
        val transactionItemList = data.mapToTransactionItemList().toMutableList()

        if(isDataSourceEmpty()) {
            mAdapter.items = transactionItemList
        } else {
            mAdapter.setItems(transactionItemList, TransactionsDiffCallback(mItems, transactionItemList))
        }

        mItems = transactionItemList
    }


    override fun showSecondaryProgressBar() {
        toolbarProgressBarListener?.showProgressBar()
    }


    override fun hideSecondaryProgressBar() {
        toolbarProgressBarListener?.hideProgressBar()
    }


    override fun updateItem(position: Int, item: Transaction) {
        mAdapter.updateItemWith(position, item.mapToTransactionItem())
    }


    override fun launchBrowser(url: String) {
        get<BrowserHandler>().launchBrowser(ctx, url, getAppTheme())
    }


    override fun getInfoViewIcon(infoViewIconProvider: InfoViewIconProvider): Drawable? {
        return infoViewIconProvider.getTransactionsIcon(mPresenter.transactionParameters)
    }


    override fun getRecyclerViewItemDecoration(): RecyclerView.ItemDecoration {
        return CardViewItemDecorator(
            dimenInPx(R.dimen.recycler_view_divider_size),
            dimenInPx(R.dimen.card_view_elevation)
        )
    }


    override fun postActionDelayed(delay: Long, action: () -> Unit) {
        mRootView.postActionDelayed(delay, action)
    }

    override fun clearAdapter() {
        mAdapter.clear()
    }


    override fun getMainView(): View = mRootView.recyclerView


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getItemPosition(item: Transaction): Int? {
        return mAdapter.getItemPosition(item)
    }


    override fun getContentLayoutResourceId(): Int = R.layout.transactions_fragment_layout


}