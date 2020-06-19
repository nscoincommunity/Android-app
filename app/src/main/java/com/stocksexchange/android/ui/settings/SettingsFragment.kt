package com.stocksexchange.android.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthurivanets.adapster.model.BaseItem
import com.stocksexchange.android.*
import com.stocksexchange.android.mappings.mapToFragSettingItem
import com.stocksexchange.android.mappings.mapToSettingItemList
import com.stocksexchange.android.model.*
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.auth.AuthenticationActivity
import com.stocksexchange.android.ui.auth.newInstance
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.utils.InAppUpdateHelper
import com.stocksexchange.android.ui.views.dialogs.DeviceMetricsDialog
import com.stocksexchange.android.ui.views.dialogs.FingerprintDialog
import com.stocksexchange.android.utils.managers.AppLockManager
import com.stocksexchange.core.decorators.DefaultSpacingItemDecorator
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.settings_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SettingsFragment : BaseFragment<SettingsPresenter>(), SettingsContract.View {


    override val mPresenter: SettingsPresenter by inject { parametersOf(this) }

    private var mItems: MutableList<BaseItem<*, *, *>> = mutableListOf()

    private lateinit var mInAppUpdateHelper: InAppUpdateHelper

    private lateinit var mAdapter: SettingsRecyclerViewAdapter

    private var mDeviceMetricsDialog: DeviceMetricsDialog? = null

    private var mFingerprintDialog: FingerprintDialog? = null

    private val mAppLockManager: AppLockManager by inject()




    override fun init() {
        super.init()

        initInAppUpdateHelper()
        initContentContainer()
        initToolbar()
        initRecyclerView()
    }


    private fun initInAppUpdateHelper() {
        mInAppUpdateHelper = InAppUpdateHelper(ctx, mInAppUpdateHelperListener).apply {
            registerWith(act)
        }
    }


    private fun initContentContainer() = with(mRootView.contentContainerRl) {
        ThemingUtil.Settings.contentContainer(this, getAppTheme())
    }


    private fun initToolbar() = with(mRootView.toolbar) {
        setTitleText(getStr(R.string.settings))

        setOnLeftButtonClickListener {
            mPresenter.onNavigateUpPressed()
        }

        ThemingUtil.Settings.toolbar(this, getAppTheme())
    }


    private fun initRecyclerView() = with(mRootView.recyclerView) {
        setHasFixedSize(true)
        disableChangeAnimations()
        layoutManager = LinearLayoutManager(ctx)
        addItemDecoration(DefaultSpacingItemDecorator(
            spacing = dimenInPx(R.dimen.profile_fragment_item_decorator_vertical_spacing),
            sideFlags = DefaultSpacingItemDecorator.SIDE_BOTTOM,
            itemExclusionPolicy = DefaultSpacingItemDecorator.LastItemExclusionPolicy()
        ))

        adapter = initAdapter()
    }


    private fun initAdapter(): SettingsRecyclerViewAdapter {
        return SettingsRecyclerViewAdapter(ctx, mItems).apply {
            setHasStableIds(true)
            setResources(SettingResources.newInstance(getSettings()))

            onSettingItemClickListener = { _, settingItem, _ ->
                mPresenter.onSettingItemClicked(settingItem.itemModel)
            }
            onSwitchClickListener = { _, settingItem, _, isChecked ->
                mPresenter.onSettingSwitchClicked(settingItem.itemModel, isChecked)
            }
            onSignOutClickListener = { _, settingItem, _ ->
                mPresenter.onSignOutClicked(settingItem.itemModel)
            }
        }.also {
            mAdapter = it
        }
    }


    override fun showProgressBar() = mRootView.toolbar.showProgressBar()


    override fun hideProgressBar() = mRootView.toolbar.hideProgressBar()


    override fun showFingerprintDialog() {
        FingerprintDialog(
            ctx,
            FingerprintDialog.Mode.SETUP,
            mFingerprintDialogListener
        ).apply {
            ThemingUtil.Settings.fingerprintDialog(this, getAppTheme())

            setSubtitleText(getStr(R.string.fingerprint_dialog_introduction_message))
            setButtonText(getStr(R.string.action_cancel))
            setButtonListener {
                mPresenter.onFingerprintDialogButtonClicked()
            }
            showSubtitle()
            startAuthenticationProcess()
        }.also {
            mFingerprintDialog = it
            mFingerprintDialog?.show()
        }
    }


    override fun hideFingerprintDialog() {
        mFingerprintDialog?.dismiss()
        mFingerprintDialog = null
    }


    override fun showDeviceMetricsDialog() {
        DeviceMetricsDialog(ctx).apply {
            val displayMetrics = resources.displayMetrics
            val density = displayMetrics.density

            setDeviceName("${Build.MODEL} (${Build.PRODUCT})")
            setNetworkGeneration(ctx.getNetworkGeneration().abbr)
            setDensity(density)
            setDensityInDp(displayMetrics.densityDpi)
            setScreenSize(ctx.getScreenSize().title)
            setScreenWidthInPx(displayMetrics.widthPixels)
            setScreenWidthInDp(displayMetrics.widthPixels / density)
            setScreenHeightInPx(displayMetrics.heightPixels)
            setScreenHeightInDp(displayMetrics.heightPixels / density)
            setSmallestWidthInDp(resources.configuration.smallestScreenWidthDp)

            ThemingUtil.Settings.deviceMetricsDialog(this, getAppTheme())
        }.also {
            mDeviceMetricsDialog = it
            mDeviceMetricsDialog?.show()
        }
    }


    override fun hideDeviceMetricsDialog() {
        mDeviceMetricsDialog?.dismiss()
        mDeviceMetricsDialog = null
    }


    override fun navigateToInboxScreen() {
        navigate(R.id.inboxDest)
    }


    override fun navigateToLanguageScreen() {
        navigate(R.id.languageDest)
    }


    override fun launchPinCodeChangeActivity() {
        startActivityForResult(AuthenticationActivity.newInstance(
            ctx,
            PinCodeMode.CHANGE,
            TransitionAnimations.HORIZONTAL_SLIDING_ANIMATIONS,
            getSettings().theme
        ), Constants.REQUEST_CODE_AUTHENTICATION_ACTIVITY)
    }


    override fun launchSecuritySettings() {
        startActivity(Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS))
    }


    override fun updateSettingWith(setting: Setting, notifyAboutChange: Boolean) {
        mAdapter.updateItemWith(
            mAdapter.getDataSetPositionForSetting(setting),
            setting.mapToFragSettingItem(),
            notifyAboutChange
        )
    }


    override fun updateLastInteractionTime() {
        mAppLockManager.updateLastInteractionTimestamp(act, System.currentTimeMillis())
    }


    override fun notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged()
    }


    override fun proceedWithInAppUpdate() {
        mInAppUpdateHelper.onNextStep()
    }


    override fun postActionDelayed(delay: Long, action: () -> Unit) {
        mRootView.postActionDelayed(delay, action)
    }


    override fun setScreenNotAsleep(isAwake: Boolean) = setScreenAwake(isAwake)


    override fun setItems(items: MutableList<Any>, notifyAboutChange: Boolean) {
        items.mapToSettingItemList().toMutableList().also {
            mItems = it
            mAdapter.setItems(it, notifyAboutChange)
        }
    }


    override fun isDataSetEmpty(): Boolean {
        return (mAdapter.itemCount == 0)
    }


    override fun getContentLayoutResourceId() = R.layout.settings_fragment_layout


    override fun getNavOptionsForDestination(id: Int): NavOptions {
        return mNavOptionsCreator.getSettingsNavOptions(id)
    }


    private val mInAppUpdateHelperListener: InAppUpdateHelper.Listener = object : InAppUpdateHelper.Listener {

        override fun onStateChanged(state: InAppUpdateHelper.State) {
            mPresenter.onInAppUpdateHelperStateChanged(state)
        }

    }


    private val mFingerprintDialogListener: FingerprintDialog.Listener = object : FingerprintDialog.Listener {

        override fun onSuccess() {
            mPresenter.onFingerprintUnlockConfirmed()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK) {
            return
        }

        when(requestCode) {

            Constants.REQUEST_CODE_AUTHENTICATION_ACTIVITY -> {
                mPresenter.onPinCodeChanged()
            }

        }
    }


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