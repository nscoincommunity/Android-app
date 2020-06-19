package com.stocksexchange.android.ui.alertprice

import android.graphics.drawable.Drawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ProgressBar
import com.stocksexchange.android.R
import com.stocksexchange.android.mappings.mapToAlertPriceElementList
import com.stocksexchange.android.mappings.mapToAlertPriceItemList
import com.stocksexchange.api.model.rest.AlertPrice
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingFragment
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.currencymarketpreview.CurrencyMarketPreviewFragment
import com.stocksexchange.android.ui.currencymarketpreview.newArgs
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.utils.extensions.ctx
import kotlinx.android.synthetic.main.alert_price_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class AlertPriceFragment : BaseListDataLoadingFragment<
    AlertPricePresenter,
    List<AlertPrice>,
    AlertPriceItem,
    AlertPriceRecyclerViewAdapter
    >(), AlertPriceContract.View {


    override val mPresenter: AlertPricePresenter by inject { parametersOf(this) }

    private val mNumberFormatter: NumberFormatter by inject()




    override fun init() {
        super.init()

        initContentContainer()
        initToolbar()
        initSwipeRefreshLayout()
    }


    private fun initContentContainer() = with(mRootView.contentContainerFl) {
        ThemingUtil.AlertPrice.contentContainer(this, getAppTheme())
    }


    private fun initToolbar() = with(mRootView.toolbar) {
        setTitleText(mStringProvider.getPriceAlertsTitle())

        setOnLeftButtonClickListener {
            mPresenter.onNavigateUpPressed()
        }

        hideInboxButton()

        ThemingUtil.AlertPrice.toolbar(this, getAppTheme())
    }


    private fun initSwipeRefreshLayout() = with(mRootView.swipeRefreshLayout) {
        setOnRefreshListener {
            mPresenter.onRefreshData()
        }

        ThemingUtil.AlertPrice.swipeRefreshLayout(this, getAppTheme())
    }


    override fun initAdapter(): AlertPriceRecyclerViewAdapter {
        return AlertPriceRecyclerViewAdapter(ctx, mItems).apply {
            setResources(AlertPriceResources.newInstance(
                settings = getSettings(),
                numberFormatter = mNumberFormatter
            ))

            onItemMoreDeleteListener = { _, alertPriceItem, _ ->
                mPresenter.onItemMoreDeleted(alertPriceItem.itemModel)
            }

            onItemLessDeleteListener = { _, alertPriceItem, _ ->
                mPresenter.onItemLessDeleted(alertPriceItem.itemModel)
            }

            onItemClickListener = { _, alertPairItem, _ ->
                mPresenter.onItemClick(alertPairItem.itemModel.currencyPairId)
            }
        }
    }


    override fun addData(data: List<AlertPrice>) {
        var addedItemCount = 0
        val alertPriceItemList = data
            .mapToAlertPriceElementList()
            .mapToAlertPriceItemList()
            .toMutableList()

        alertPriceItemList.sortBy { it.getAlertPairItemName() }

        for (alertPriceItem in alertPriceItemList) {
            if (mAdapter.contains(alertPriceItem)) {
                mAdapter.updateItemWith(alertPriceItem)
            } else {
                mAdapter.addItem(alertPriceItem, false)
                addedItemCount++
            }
        }

        if (addedItemCount > 0) {
            mAdapter.notifyItemRangeChanged(mAdapter.itemCount, addedItemCount)
        }
    }


    override fun deleteItem(item: AlertPrice) {
        getAlertPairItemById(item.currencyPairId)?.let {
            if (it.deleteAlertPriceInPairItemById(item.id).isMoreLessEmpty) {
                getAlertPriceItemByPair(it.currencyPairId)?.let {
                    mAdapter.deleteItem(it)
                }
            } else {
                getAlertPriceItemByPair(it.currencyPairId)?.let {
                    mAdapter.updateItemWith(it)
                }
            }
        }
    }


    private fun getAlertPriceItemByPair(pairId: Int): AlertPriceItem? {
        var index = 0
        while (index < mAdapter.items.size) {
            if (mAdapter.items.get(index).getAlertPairItem().currencyPairId == pairId) {
                return mAdapter.items.get(index)
            }
            index++
        }

        return null
    }


    private fun getAlertPairItemById(pairId: Int): AlertPairItem? {
        var index = 0
        while (index < mAdapter.items.size) {
            val alertPairItem = mAdapter.items.get(index)
            if (alertPairItem.getAlertPairItemId() == pairId) {
                return alertPairItem.getAlertPairItem()
            }
            index++
        }

        return null
    }


    override fun navigateToCurrencyMarketPreviewScreen(currencyMarket: CurrencyMarket) {
        navigate(
            destinationId = R.id.currencyMarketPreviewDest,
            arguments = CurrencyMarketPreviewFragment.newArgs(currencyMarket)
        )
    }


    override fun showToolbarProgressBar() {
        mRootView.toolbar.showProgressBar()
    }


    override fun hideToolbarProgressBar() {
        mRootView.toolbar.hideProgressBar()
    }


    override fun hasItemDecoration(): Boolean = false


    override fun getInfoViewIcon(infoViewIconProvider: InfoViewIconProvider): Drawable? {
        return infoViewIconProvider.getAlertPriceIcon()
    }


    override fun getMainView(): View = mRootView.recyclerView


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getItemPosition(item: AlertPrice): Int? {
        return mAdapter.getItemPosition(item)
    }


    override fun getContentLayoutResourceId(): Int = R.layout.alert_price_fragment_layout


}