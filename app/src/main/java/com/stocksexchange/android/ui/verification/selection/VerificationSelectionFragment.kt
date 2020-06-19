package com.stocksexchange.android.ui.verification.selection

import androidx.recyclerview.widget.LinearLayoutManager
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.api.model.rest.VerificationType
import com.stocksexchange.android.mappings.mapToVerificationSelectionItemList
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.utils.handlers.BrowserHandler
import com.stocksexchange.core.decorators.CardViewItemDecorator
import com.stocksexchange.core.utils.extensions.ctx
import com.stocksexchange.core.utils.extensions.dimenInPx
import kotlinx.android.synthetic.main.verification_selection_fragment_layout.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class VerificationSelectionFragment : BaseFragment<VerificationSelectionPresenter>(), VerificationSelectionContract.View {


    override val mPresenter: VerificationSelectionPresenter by inject { parametersOf(this) }

    private lateinit var mAdapter: VerificationSelectionRecyclerViewAdapter




    override fun init() {
        super.init()

        initContentContainer()
        initToolbar()
        initRecyclerView()
    }


    private fun initContentContainer() = with(mRootView.contentContainerRl) {
        ThemingUtil.VerificationSelection.contentContainer(this, getAppTheme())
    }


    private fun initToolbar() = with(mRootView.toolbar) {
        setTitleText(mStringProvider.getString(R.string.verification))

        setOnLeftButtonClickListener {
            mPresenter.onNavigateUpPressed()
        }

        ThemingUtil.VerificationSelection.toolbar(this, getAppTheme())
    }


    private fun initRecyclerView() = with(mRootView.recyclerView) {
        layoutManager = LinearLayoutManager(ctx)
        addItemDecoration(CardViewItemDecorator(
            dimenInPx(R.dimen.recycler_view_divider_size),
            dimenInPx(R.dimen.card_view_elevation)
        ))
        setHasFixedSize(true)

        mAdapter = VerificationSelectionRecyclerViewAdapter(
            ctx,
            getRecyclerViewItems()
        )
        mAdapter.setResources(VerificationSelectionResources.newInstance(
            profileInfo = (mSessionManager.getProfileInfo() ?: ProfileInfo.STUB_PROFILE_INFO),
            settings = getSettings(),
            stringProvider = mStringProvider,
            numberFormatter = get()
        ))
        mAdapter.onItemClickListener = { _, item, _ ->
            mPresenter.onVerificationClicked(item.itemModel)
        }

        adapter = mAdapter
    }


    override fun launchBrowser(url: String) {
        get<BrowserHandler>().launchBrowser(ctx, url, getAppTheme())
    }


    override fun getContentLayoutResourceId(): Int = R.layout.verification_selection_fragment_layout


    private fun getRecyclerViewItems(): MutableList<VerificationSelectionItem> {
        return VerificationType.values()
            .toList()
            .mapToVerificationSelectionItemList()
            .toMutableList()
    }


}