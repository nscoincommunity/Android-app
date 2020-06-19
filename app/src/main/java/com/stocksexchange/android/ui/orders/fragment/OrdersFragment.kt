package com.stocksexchange.android.ui.orders.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.android.mappings.mapToOrderItem
import com.stocksexchange.android.mappings.mapToOrderItemList
import com.stocksexchange.android.model.OrderData
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingFragment
import com.stocksexchange.android.ui.currencymarketpreview.CurrencyMarketPreviewFragment
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.currencymarketpreview.newArgs
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.utils.handlers.BrowserHandler
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.api.model.rest.SortOrder
import com.stocksexchange.core.decorators.CardViewItemDecorator
import com.stocksexchange.core.utils.extensions.ctx
import com.stocksexchange.core.utils.extensions.dimenInPx
import com.stocksexchange.core.utils.extensions.extract
import com.stocksexchange.core.utils.extensions.postActionDelayed
import com.stocksexchange.core.utils.listeners.ProgressBarListener
import kotlinx.android.synthetic.main.orders_fragment_layout.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class OrdersFragment : BaseListDataLoadingFragment<
    OrdersPresenter,
    List<OrderData>,
    OrderItem,
    OrdersRecyclerViewAdapter
    >(), OrdersContract.View {


    companion object {}


    override val mPresenter: OrdersPresenter by inject { parametersOf(this) }

    var toolbarProgressBarListener: ProgressBarListener? = null




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.orderParameters = it.orderParameters
        }
    }


    override fun init() {
        super.init()

        initContentContainer()
    }


    override fun initAdapter(): OrdersRecyclerViewAdapter {
        return OrdersRecyclerViewAdapter(ctx, mItems).apply {
            setResources(OrderResources.newInstance(
                stringProvider = mStringProvider,
                numberFormatter = get(),
                timeFormatter = get(),
                settings = getSettings()
            ))
            setOrderLifecycleType(mPresenter.orderParameters.lifecycleType)
            setOrderSelectivityType(mPresenter.orderParameters.selectivityType)

            onMarketNameClickListener = { _, orderItem, _ ->
                mPresenter.onMarketNameClicked(orderItem.itemModel.currencyMarket)
            }
            onCancelBtnClickListener = { _, orderItem, _ ->
                mPresenter.onCancelButtonClicked(orderItem.itemModel)
            }
        }
    }


    private fun initContentContainer() = with(mRootView.contentContainerFl) {
        ThemingUtil.Orders.contentContainer(this, getAppTheme())
    }


    override fun showSecondaryProgressBar() {
        toolbarProgressBarListener?.showProgressBar()
    }


    override fun hideSecondaryProgressBar() {
        toolbarProgressBarListener?.hideProgressBar()
    }


    override fun addData(data: List<OrderData>) {
        var addedItemCount = 0
        val orderItemList = data.mapToOrderItemList().toMutableList()

        for(orderItem in orderItemList) {
            if(mAdapter.contains(orderItem)) {
                mAdapter.updateItemWith(orderItem)
            } else {
                mAdapter.addItem(orderItem, false)
                addedItemCount++
            }
        }

        if(addedItemCount > 0) {
            mAdapter.notifyItemRangeChanged(mAdapter.itemCount, addedItemCount)
        }
    }


    override fun addOrderChronologically(orderData: OrderData, sortOrder: SortOrder) {
        mAdapter.addItem(
            mAdapter.getChronologicalPositionForOrder(orderData.order, sortOrder),
            orderData.mapToOrderItem()
        )
    }


    override fun updateOrder(position: Int, orderData: OrderData) {
        mAdapter.updateItemWith(position, orderData.mapToOrderItem())
    }


    override fun containsOrder(orderData: OrderData): Boolean {
        return mAdapter.contains(orderData.mapToOrderItem())
    }


    override fun deleteOrder(orderData: OrderData) {
        mAdapter.deleteItem(orderData.mapToOrderItem())
    }


    override fun clearAdapter() {
        mAdapter.clear()
    }


    override fun launchBrowser(url: String) {
        get<BrowserHandler>().launchBrowser(ctx, url, getAppTheme())
    }


    override fun navigateToCurrencyMarketPreviewScreen(currencyMarket: CurrencyMarket) {
        navigate(
            destinationId = R.id.currencyMarketPreviewDest,
            arguments = CurrencyMarketPreviewFragment.newArgs(currencyMarket)
        )
    }


    override fun shouldDisableRVAnimations(): Boolean {
        return false
    }


    override fun getOrderPosition(orderData: OrderData): Int? {
        return mAdapter.getOrderPosition(orderData.order)
    }


    override fun getInfoViewIcon(infoViewIconProvider: InfoViewIconProvider): Drawable? {
        return infoViewIconProvider.getOrdersIcon(mPresenter.orderParameters)
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


    override fun getMainView(): View = mRootView.recyclerView


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getContentLayoutResourceId(): Int = R.layout.orders_fragment_layout


}