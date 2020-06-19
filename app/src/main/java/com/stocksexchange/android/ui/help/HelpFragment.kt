package com.stocksexchange.android.ui.help

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.stocksexchange.android.R
import com.stocksexchange.android.mappings.mapToHelpItemList
import com.stocksexchange.android.model.HelpItemModel
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.core.decorators.CardViewItemDecorator
import com.stocksexchange.core.model.NetworkInfo
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.help_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class HelpFragment : BaseFragment<HelpPresenter>(), HelpContract.View {


    override val mPresenter: HelpPresenter by inject { parametersOf(this) }

    private var mItems: MutableList<HelpItem> = mutableListOf()

    private lateinit var mAdapter: HelpRecyclerViewAdapter




    override fun init() {
        super.init()

        initContentContainer()
        initToolbar()
        initRecyclerView()
    }


    private fun initContentContainer() = with(mRootView.contentContainerRl) {
        ThemingUtil.Help.contentContainer(this, getAppTheme())
    }


    private fun initToolbar() = with(mRootView.toolbar) {
        setTitleText(getStr(R.string.help))

        setOnLeftButtonClickListener {
            mPresenter.onNavigateUpPressed()
        }

        ThemingUtil.Help.toolbar(this, getAppTheme())
    }


    private fun initRecyclerView() = with(mRootView.recyclerView) {
        layoutManager = LinearLayoutManager(ctx)
        addItemDecoration(CardViewItemDecorator(
            dimenInPx(R.dimen.recycler_view_divider_size),
            dimenInPx(R.dimen.card_view_elevation)
        ))
        setHasFixedSize(true)

        val resources = HelpItemResources.newInstance(
            settings = getSettings(),
            stringProvider = mStringProvider
        )

        mAdapter = HelpRecyclerViewAdapter(ctx, mItems)
        mAdapter.setResources(resources)
        mAdapter.onItemClickListener = { _, item, _ ->
            mPresenter.onItemClicked(item.itemModel)
        }

        adapter = mAdapter
    }


    override fun showProgressBar() {
        mRootView.toolbar.showProgressBar()
    }


    override fun hideProgressBar() {
        mRootView.toolbar.hideProgressBar()
    }


    override fun setItems(items: List<HelpItemModel>) {
        items.mapToHelpItemList().toMutableList().also {
            mItems = it
            mAdapter.items = it
        }
    }


    override fun isDataSetEmpty(): Boolean = (mAdapter.itemCount == 0)


    override fun getNetworkInfo(): NetworkInfo = ctx.getNetworkInfo()


    override fun getContentLayoutResourceId(): Int = R.layout.help_fragment_layout


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