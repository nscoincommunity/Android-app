package com.stocksexchange.android.ui.profile

import androidx.recyclerview.widget.LinearLayoutManager
import com.stocksexchange.android.R
import com.stocksexchange.android.mappings.mapToProfileItemList
import com.stocksexchange.android.model.ProfileItemModel
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.ui.login.LoginActivity
import com.stocksexchange.android.ui.login.newInstance
import com.stocksexchange.android.ui.registration.RegistrationActivity
import com.stocksexchange.android.ui.registration.newInstance
import com.stocksexchange.core.decorators.DefaultSpacingItemDecorator
import com.stocksexchange.core.utils.extensions.ctx
import com.stocksexchange.core.utils.extensions.dimenInPx
import kotlinx.android.synthetic.main.profile_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ProfileFragment : BaseFragment<ProfilePresenter>(), ProfileContract.View {


    override val mPresenter: ProfilePresenter by inject { parametersOf(this) }

    private var mItems: MutableList<ProfileItem> = mutableListOf()

    private lateinit var mAdapter: ProfileRecyclerViewAdapter




    override fun init() {
        super.init()

        initContentContainer()
        initToolbar()
        initRecyclerView()
    }


    private fun initContentContainer() = with(mRootView.contentContainerRl) {
        ThemingUtil.Profile.contentContainer(this, getAppTheme())
    }


    private fun initToolbar() = with(mRootView.toolbar) {
        setTitleText(getStr(R.string.profile))

        ThemingUtil.Profile.toolbar(this, getAppTheme())
    }


    private fun initRecyclerView() = with(mRootView.recyclerView) {
        layoutManager = LinearLayoutManager(ctx)
        addItemDecoration(DefaultSpacingItemDecorator(
            spacing = dimenInPx(R.dimen.profile_fragment_item_decorator_vertical_spacing),
            sideFlags = DefaultSpacingItemDecorator.SIDE_BOTTOM,
            itemExclusionPolicy = DefaultSpacingItemDecorator.LastItemExclusionPolicy()
        ))
        setHasFixedSize(true)

        mAdapter = ProfileRecyclerViewAdapter(ctx, mItems).apply {
            setResources(ProfileItemResources.newInstance(
                settings = getSettings(),
                stringProvider = mStringProvider
            ))

            onItemClickListener = { _, item, _ ->
                mPresenter.onProfileItemClicked(item.itemModel)
            }
        }

        adapter = mAdapter
    }


    override fun launchLoginActivity() {
        startActivity(LoginActivity.newInstance(ctx))
    }


    override fun launchRegistrationActivity() {
        startActivity(RegistrationActivity.newInstance(ctx))
    }


    override fun navigateToSettingsScreen() {
        navigate(R.id.settingsDest)
    }


    override fun navigateToReferralScreen() {
        navigate(R.id.referralDest)
    }


    override fun navigateToVerificationScreen() {
        navigate(R.id.verificationDest)
    }


    override fun navigateToHelpScreen() {
        navigate(R.id.helpDest)
    }


    override fun navigateToAboutScreen() {
        navigate(R.id.aboutDest)
    }


    override fun setItems(items: List<ProfileItemModel>) {
        items.mapToProfileItemList().toMutableList().also {
            mItems = it
            mAdapter.items = it
        }
    }


    override fun isDataSetEmpty(): Boolean = (mAdapter.itemCount == 0)


    override fun getContentLayoutResourceId(): Int = R.layout.profile_fragment_layout


}