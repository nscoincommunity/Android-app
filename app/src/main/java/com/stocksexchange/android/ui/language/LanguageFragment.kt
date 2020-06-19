package com.stocksexchange.android.ui.language

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.stocksexchange.android.*
import com.stocksexchange.android.mappings.mapToLanguageItemList
import com.stocksexchange.android.model.*
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.core.decorators.DefaultSpacingItemDecorator
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.language_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class LanguageFragment : BaseFragment<LanguagePresenter>(), LanguageContract.View {


    override val mPresenter: LanguagePresenter by inject { parametersOf(this) }

    private var mItems: MutableList<LanguageItem> = mutableListOf()

    private lateinit var mAdapter: LanguageRecyclerViewAdapter




    override fun init() {
        super.init()

        initContentContainer()
        initToolbar()
        initRecyclerView()
    }


    private fun initContentContainer() = with(mRootView.contentContainerRl) {
        ThemingUtil.LanguageSelection.contentContainer(this, getAppTheme())
    }


    private fun initToolbar() = with(mRootView.toolbar) {
        setTitleText(getStr(R.string.language))
        setToolbarRightButtonVisible(mPresenter.isToolbarRightButtonVisible)

        setOnLeftButtonClickListener {
            mPresenter.onNavigateUpPressed()
        }
        setOnRightButtonClickListener {
            mPresenter.onToolbarRightButtonClicked()
        }

        ThemingUtil.LanguageSelection.toolbar(this, getAppTheme())
    }


    private fun initRecyclerView() = with(mRootView.recyclerView) {
        disableChangeAnimations()
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(ctx)
        addItemDecoration(DefaultSpacingItemDecorator(
            spacing = dimenInPx(R.dimen.profile_fragment_item_decorator_vertical_spacing),
            sideFlags = DefaultSpacingItemDecorator.SIDE_BOTTOM,
            itemExclusionPolicy = DefaultSpacingItemDecorator.LastItemExclusionPolicy()
        ))

        adapter = initAdapter()
    }


    private fun initAdapter(): LanguageRecyclerViewAdapter {
        return LanguageRecyclerViewAdapter(ctx, mItems).apply {
            setHasStableIds(true)
            setResources(LanguageResources.newInstance(getSettings(), mStringProvider))

            onLanguageItemClickListener = { _, languageItem, _ ->
                mPresenter.onLanguageItemClicked(languageItem.itemModel)
            }
        }.also {
            mAdapter = it
        }
    }


    override fun setToolbarRightButtonVisible(isVisible: Boolean) {
        if(isVisible) {
            mRootView.toolbar.showRightButton()
        } else {
            mRootView.toolbar.hideRightButton()
        }
    }


    override fun setItems(items: MutableList<LanguageItemModel>) {
        items.mapToLanguageItemList().toMutableList().also {
            mItems = it
            mAdapter.items = it
        }
    }


    override fun isDataSetEmpty(): Boolean {
        return (mAdapter.itemCount == 0)
    }


    override fun getContentLayoutResourceId() = R.layout.language_fragment_layout


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreState(savedState: Bundle) {
        super.onRestoreState(savedState)

        savedState.extract(fragmentStateExtractor).also {
            mItems = it.items
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        savedState.saveState(FragmentState(
            items = mItems
        ))
    }

}