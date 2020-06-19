package com.stocksexchange.android.ui.dashboard

import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.events.SettingsEvent
import com.stocksexchange.android.events.UserEvent
import com.stocksexchange.android.model.DashboardArgs
import com.stocksexchange.android.model.DashboardBottomMenuItem
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.model.VerificationPromptDescriptionType
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.core.utils.interfaces.Selectable
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DashboardPresenter(
    view: DashboardContract.View,
    model: StubModel,
    private val sessionManager: SessionManager
) : BasePresenter<DashboardContract.View, StubModel>(view, model),
    DashboardContract.ActionListener {


    lateinit var dashboardArgs: DashboardArgs

    var isBottomNavigationVisible: Boolean = true




    override fun start() {
        super.start()

        if(shouldLaunchVerificationPromptActivity()) {
            launchVerificationPromptActivity()
        }
    }


    private fun shouldLaunchVerificationPromptActivity(): Boolean {
        val isRelease = !BuildConfig.DEBUG
        val isUserSignedIn = sessionManager.isUserSignedIn()
        val isUserNotVerified = (sessionManager.getProfileInfo()?.isVerified() == false)
        val wasVerificationPromptNotDisplayed = !sessionManager.wasVerificationPromptDisplayed()

        return (isUserSignedIn && isUserNotVerified && wasVerificationPromptNotDisplayed)
    }


    private fun launchVerificationPromptActivity() {
        sessionManager.setVerificationPromptDisplayed(true)

        val profileInfo = sessionManager.getProfileInfo()
        val areWithdrawalsAllowed = (profileInfo?.areWithdrawalsAllowed == true)
        val descriptionType = if(areWithdrawalsAllowed) {
            VerificationPromptDescriptionType.LONG
        } else {
            VerificationPromptDescriptionType.SHORT
        }

        mView.launchVerificationPromptActivity(descriptionType)
    }


    override fun onNavigationItemSelected(id: Int): Boolean {
        val userRequiredPages = DashboardBottomMenuItem.values().filter { it.requiresUser }

        for(userRequiredPage in userRequiredPages) {
            if((id == userRequiredPage.id) && !sessionManager.isUserSignedIn()) {
                mView.launchLoginActivity(DashboardArgs(selectedBottomMenuItem = userRequiredPage))
                return false
            }
        }

        val newSelectedBottomMenuItem = DashboardBottomMenuItem.values().firstOrNull { it.id == id }

        if(newSelectedBottomMenuItem != null) {
            dashboardArgs = dashboardArgs.copy(selectedBottomMenuItem = newSelectedBottomMenuItem)
        }

        val viewPagerNewItemPosition = convertBottomNavItemIdToViewPagerItemPosition(id)
        val viewPagerCurrentItemPosition = mView.getViewPagerCurrentItemPosition()

        if(viewPagerNewItemPosition != viewPagerCurrentItemPosition) {
            mView.setPageSelected(viewPagerCurrentItemPosition, false, Selectable.Source.MENU)
            mView.setViewPagerCurrentItemPosition(viewPagerNewItemPosition, false)
            mView.setPageSelected(viewPagerNewItemPosition, true, Selectable.Source.MENU)
        }

        return true
    }


    override fun onNavigationItemReselected(id: Int) {
        val viewPagerItemPosition = convertBottomNavItemIdToViewPagerItemPosition(id)

        if(!mView.isViewPagerAdapterEmpty()) {
            mView.setPageSelected(viewPagerItemPosition, true, Selectable.Source.MENU)
            mView.scrollPageToTop(viewPagerItemPosition)
        }
    }


    private fun convertBottomNavItemIdToViewPagerItemPosition(id: Int): Int {
        return DashboardBottomMenuItem.values()
            .firstOrNull { it.id == id }?.ordinal
            ?: DashboardBottomMenuItem.BALANCE.ordinal
    }


    private fun restartActivity(dashboardArgs: DashboardArgs) {
        mView.restartActivity(dashboardArgs)
    }


    private fun restartActivityAnew() {
        restartActivity(DashboardArgs())
    }


    override fun onShowBottomNavigation() {
        if(!isBottomNavigationVisible) {
            isBottomNavigationVisible = true

            mView.showBottomNavigation()
        }
    }


    override fun onHideBottomNavigation() {
        if(isBottomNavigationVisible) {
            isBottomNavigationVisible = false

            mView.hideBottomNavigation()
        }
    }


    @Suppress("NON_EXHAUSTIVE_WHEN")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: SettingsEvent) {
        if(event.isOriginatedFrom(this)) {
            return
        }

        val newSettings = event.attachment

        when(event.action) {
            SettingsEvent.Action.LANGUAGE_CHANGED -> onLanguageChanged()
            SettingsEvent.Action.FIAT_CURRENCY_CHANGED -> onFiatCurrencyChanged(newSettings)
            SettingsEvent.Action.THEME_CHANGED -> onThemeChanged(newSettings)
            SettingsEvent.Action.DEFAULTS_RESTORED -> onDefaultsRestored()
            SettingsEvent.Action.GROUPING_STATE_CHANGED -> onGroupingStateChanged(newSettings)
            SettingsEvent.Action.GROUPING_SEPARATOR_CHANGED -> onGroupingSeparatorChanged(newSettings)
            SettingsEvent.Action.DECIMAL_SEPARATOR_CHANGED -> onDecimalSeparatorChanged(newSettings)
        }
    }


    private fun onLanguageChanged() {
        restartActivityAnew()
    }


    private fun onFiatCurrencyChanged(newSettings: Settings) {
        notifyPagesAboutSettingsChange(newSettings)
    }


    private fun onThemeChanged(newSettings: Settings) {
        mView.applyNewTheme(newSettings.theme)

        notifyPagesAboutSettingsChange(newSettings)
    }


    private fun onDefaultsRestored() {
        restartActivityAnew()
    }


    private fun onGroupingStateChanged(newSettings: Settings) {
        notifyPagesAboutSettingsChange(newSettings)
    }


    private fun onGroupingSeparatorChanged(newSettings: Settings) {
        notifyPagesAboutSettingsChange(newSettings)
    }


    private fun onDecimalSeparatorChanged(newSettings: Settings) {
        notifyPagesAboutSettingsChange(newSettings)
    }


    private fun notifyPagesAboutSettingsChange(newSettings: Settings) {
        val pagePositions = DashboardBottomMenuItem.values()
            .filter { it != DashboardBottomMenuItem.PROFILE }
            .map { it.ordinal }

        for(pagePosition in pagePositions) {
            mView.notifyPageAboutSettingsChange(pagePosition, newSettings)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: UserEvent) {
        if(event.isOriginatedFrom(this)) {
            return
        }

        when(event.action) {
            UserEvent.Action.SIGNED_OUT -> onUserSignedOut()
        }
    }


    private fun onUserSignedOut() {
        restartActivity(DashboardArgs(dashboardArgs.selectedBottomMenuItem))
    }


    override fun canReceiveEvents(): Boolean = true


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            isBottomNavigationVisible = it.isBottomNavigationVisible
            dashboardArgs = it.dashboardArgs
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            isBottomNavigationVisible = isBottomNavigationVisible,
            dashboardArgs = dashboardArgs
        ))
    }


}