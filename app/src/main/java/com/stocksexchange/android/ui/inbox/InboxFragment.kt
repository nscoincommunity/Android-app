package com.stocksexchange.android.ui.inbox

import android.graphics.drawable.Drawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ProgressBar
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.Inbox
import com.stocksexchange.android.mappings.mapToInboxItem
import com.stocksexchange.android.mappings.mapToInboxItemList
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingFragment
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.core.utils.extensions.ctx
import kotlinx.android.synthetic.main.inbox_fragment_layout.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class InboxFragment : BaseListDataLoadingFragment<
    InboxPresenter,
    List<Inbox>,
    InboxItem,
    InboxRecyclerViewAdapter
    >(), InboxContract.View {


    override val mPresenter: InboxPresenter by inject { parametersOf(this) }




    override fun init() {
        super.init()

        initContentContainer()
        initToolbar()
        initSwipeRefreshLayout()
    }


    private fun initContentContainer() = with(mRootView.contentContainerFl) {
        ThemingUtil.Inbox.contentContainer(this, getAppTheme())
    }


    private fun initToolbar() = with(mRootView.toolbar) {
        setTitleText(getStr(R.string.inbox_notification_report))

        setOnLeftButtonClickListener {
            mPresenter.onNavigateUpPressed()
        }

        ThemingUtil.Inbox.toolbar(this, getAppTheme())
    }


    private fun initSwipeRefreshLayout() = with(mRootView.swipeRefreshLayout) {
        setOnRefreshListener {
            mPresenter.onRefreshData()
        }

        ThemingUtil.Inbox.swipeRefreshLayout(this, getAppTheme())
    }


    override fun initAdapter(): InboxRecyclerViewAdapter {
        return InboxRecyclerViewAdapter(ctx, mItems).apply {
            setResources(InboxResources.newInstance(
                context = ctx,
                stringProvider = mStringProvider,
                timeFormatter = get(),
                numberFormatter = get(),
                settings = getSettings()
            ))

            onItemDeleteListener = { _, inboxItem, _ ->
                mPresenter.onItemDeleted(inboxItem.itemModel)
            }
            onItemClickListener = { _, inboxItem, _ ->
                mPresenter.onItemClicked(inboxItem.itemModel)
            }
        }
    }


    override fun addData(data: List<Inbox>) {
        var addedItemCount = 0
        val inboxItemList = data.mapToInboxItemList().toMutableList()

        for(inboxItem in inboxItemList) {
            if(mAdapter.contains(inboxItem)) {
                mAdapter.updateItemWith(inboxItem)
            } else {
                mAdapter.addItem(inboxItem, false)
                addedItemCount++
            }
        }

        if(addedItemCount > 0) {
            mAdapter.notifyItemRangeChanged(mAdapter.itemCount, addedItemCount)
        }

        updateInboxCountUnreadItem()
    }


    override fun addItemToTop(item: Inbox) {
        if(mAdapter.contains(item.mapToInboxItem())) {
            return
        }

        mAdapter.addItem(0, item.mapToInboxItem(), true)
        mAdapter.notifyItemRangeChanged(0, mAdapter.itemCount)
        mRootView.recyclerView.scrollToPosition(0)

        updateInboxCountUnreadItem()
    }


    override fun onPause() {
        super.onPause()

        mPresenter.setInboxAllRead()
    }


    override fun updateItem(item: Inbox) {
        mAdapter.updateItemWith(item.mapToInboxItem(), true)
        updateInboxCountUnreadItem()
    }


    override fun deleteItem(item: Inbox) {
        mAdapter.deleteItem(item.mapToInboxItem())
        updateInboxCountUnreadItem()
    }


    override fun getUnreadCountItemOnScreen(): Int {
        var count = 0;
        for (item in mItems) {
            if (!item.isShowed()) {
                count += 1
            }
        }

        return count
    }


    override fun updateInboxCountUnreadItem() {
        mRootView.toolbar.setInboxButtonCountMessage(getUnreadCountItemOnScreen())
    }


    override fun showToolbarProgressBar() = mRootView.toolbar.showProgressBar()


    override fun hideToolbarProgressBar() = mRootView.toolbar.hideProgressBar()


    override fun getInfoViewIcon(infoViewIconProvider: InfoViewIconProvider): Drawable? {
        return infoViewIconProvider.getInboxIcon()
    }


    override fun getMainView(): View = mRootView.recyclerView


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getItemPosition(item: Inbox): Int? {
        return mAdapter.getItemPosition(item)
    }


    override fun getContentLayoutResourceId(): Int = R.layout.inbox_fragment_layout


}