package com.stocksexchange.android.ui.base.listdataloading

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthurivanets.adapster.listeners.OnDatasetChangeListener
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.recyclerview.BaseRecyclerViewAdapter
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingFragment
import com.stocksexchange.cache.ObjectCache
import com.stocksexchange.core.decorators.DefaultSpacingItemDecorator
import com.stocksexchange.core.utils.extensions.ctx
import com.stocksexchange.core.utils.extensions.dimenInPx
import com.stocksexchange.core.utils.extensions.disableAnimations
import com.stocksexchange.core.utils.interfaces.Searchable
import com.stocksexchange.core.utils.listeners.RecyclerViewScrollListener
import com.stocksexchange.core.utils.listeners.adapters.OnDatasetChangeListenerAdapter
import com.stocksexchange.core.utils.listeners.adapters.RecyclerViewStateListenerAdapter

/**
 * A base fragment that holds functionality related
 * to loading list data.
 */
abstract class BaseListDataLoadingFragment<PR, DA, IM, AD> : BaseDataLoadingFragment<PR, DA>(),
    ListDataLoadingView<DA>,
    Searchable where
        PR : BaseListDataLoadingPresenter<*, *, DA, *>,
        IM : BaseItem<*, *, *>,
        AD : BaseRecyclerViewAdapter<IM, *> {


    companion object {

        private const val KEY_ITEMS = "items"

    }


    /**
     * List data items.
     */
    protected var mItems: MutableList<IM> = mutableListOf()

    /**
     * A RecyclerView adapter.
     */
    protected lateinit var mAdapter: AD




    override fun init() {
        super.init()

        initRecyclerView()
    }


    open fun initRecyclerView() {
        val recyclerView = (getMainView() as RecyclerView)

        if(shouldDisableRVAnimations()) {
            recyclerView.disableAnimations()
        }

        recyclerView.addOnScrollListener(RecyclerViewScrollListener(mOnRecyclerViewScrollListener))
        recyclerView.layoutManager = LinearLayoutManager(ctx)

        if(hasItemDecoration()) {
            recyclerView.addItemDecoration(getRecyclerViewItemDecoration())
        }

        mAdapter = initAdapter()
        mAdapter.addOnDatasetChangeListener(mOnDataSetChangeListener)

        recyclerView.adapter = mAdapter

        adjustView(recyclerView)
    }


    protected abstract fun initAdapter(): AD


    override fun showMainView() {
        val mainView = getMainView()

        mainView.alpha = 0f
        mainView
            .animate()
            .alpha(1f)
            .setInterpolator(LinearInterpolator())
            .setDuration(Constants.MAIN_VIEW_ANIMATION_DURATION)
            .start()
    }


    override fun hideMainView() {
        mAdapter.clear()
    }


    override fun scrollToTop() {
        (getMainView() as RecyclerView).scrollToPosition(0)
    }


    override fun isDataSourceEmpty(): Boolean {
        return (getDataSetSize() == 0)
    }


    protected open fun hasItemDecoration(): Boolean = true


    protected open fun shouldDisableRVAnimations(): Boolean = true


    override fun getDataSetSize(): Int {
        return (if(::mAdapter.isInitialized) mAdapter.itemCount else 0)
    }


    protected open fun getDataCacheKey(): String {
        return "${mPresenter}_$KEY_ITEMS"
    }


    protected open fun getRecyclerViewItemDecoration(): RecyclerView.ItemDecoration {
        return DefaultSpacingItemDecorator(
            spacing = dimenInPx(R.dimen.recycler_view_default_item_spacing),
            sideFlags = DefaultSpacingItemDecorator.SIDE_BOTTOM,
            itemExclusionPolicy = DefaultSpacingItemDecorator.LastItemExclusionPolicy()
        )
    }


    override fun onStart() {
        super.onStart()

        // Clearing the cache of data since sometimes onSaveState
        // is called when its counterpart onRestoreState is not
        if(ObjectCache.contains(getDataCacheKey())) {
            ObjectCache.remove(getDataCacheKey())
        }
    }


    override fun onPerformSearch(query: String) {
        mPresenter.onPerformSearch(query)
    }


    override fun onCancelSearch() {
        mPresenter.onCancelDataLoading()
    }


    private val mOnRecyclerViewScrollListener: RecyclerViewScrollListener.StateListener =
        object : RecyclerViewStateListenerAdapter {

            override fun onBottomReached(recyclerView: RecyclerView, reachedCompletely: Boolean) {
                mPresenter.onBottomReached(reachedCompletely)
            }

        }


    private val mOnDataSetChangeListener: OnDatasetChangeListener<MutableList<IM>, IM> =
        object : OnDatasetChangeListenerAdapter<MutableList<IM>, IM> {

            override fun onDatasetSizeChanged(oldSize: Int, newSize: Int) {
                mPresenter.onDataSetSizeChanged(newSize)
            }

        }


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreState(savedState: Bundle) {
        super.onRestoreState(savedState)

        mItems = (ObjectCache.remove(getDataCacheKey(), mutableListOf<IM>()) as MutableList<IM>)
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        ObjectCache.put(getDataCacheKey(), mItems)
    }


}