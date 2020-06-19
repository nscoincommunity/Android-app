package com.stocksexchange.android.ui.base.activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.afollestad.materialdialogs.MaterialDialog
import com.stocksexchange.android.analytics.FirebaseEventLogger
import com.stocksexchange.android.model.MaterialDialogBuilder
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.model.SystemWindowType
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.receivers.AppLockReceiver
import com.stocksexchange.android.receivers.NetworkStateReceiver
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.ui.base.mvp.views.BaseView
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.*
import com.stocksexchange.android.utils.handlers.IntercomHandler
import com.stocksexchange.android.utils.managers.AppLockManager
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.utils.extensions.*
import org.koin.android.ext.android.inject

/**
 * A base activity that contains common functionality for all activities.
 */
abstract class BaseActivity<P : BasePresenter<*, *>> : AppCompatActivity(), BaseView, NetworkStateReceiver.Listener,
    AppLockReceiver.Listener {


    companion object {

        private const val KEY_PRESENTER = "presenter"

    }


    private var mIsNetworkStateReceiverRegistered: Boolean = false
    private var mIsAppLockReceiverRegistered: Boolean = false
    private var mIsInitialized: Boolean = false

    private var mMaterialDialog: MaterialDialog? = null

    private var mNetworkStateReceiver: NetworkStateReceiver? = null
    private var mAppLockReceiver: AppLockReceiver? = null

    protected val mStringProvider: StringProvider by inject()
    private val mIntercomHandler: IntercomHandler by inject()

    private val mSessionManager: SessionManager by inject()
    private val mAppLockManager: AppLockManager by inject()

    private val mFirebaseEventLogger: FirebaseEventLogger by inject()

    protected abstract val mPresenter: P




    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!shouldInitActivity()) return

        preViewInflation()
        setContentView(getContentLayoutResourceId())
        preInit()
        onHandleState(savedInstanceState)
        init()
        postInit()

        mIsInitialized = true
    }


    /**
     * Called before [setContentView] is called. Can be useful
     * for performing some tasks before inflating views.
     */
    @CallSuper
    protected open fun preViewInflation() {
        if(shouldUpdateStringProviderLocale()) {
            updateStringProviderLocale()
        }
    }


    private fun onHandleState(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            intent.extras?.let(::onFetchExtras)
        } else {
            onRestoreStateInternal(savedInstanceState)
        }
    }


    /**
     * Called right after [setContentView] method is called.
     * Can be useful for performing some tasks before views
     * initialization.
     */
    @CallSuper
    protected open fun preInit() {
        // Stub
    }


    /**
     * Called right after [onRestoreState] method is called. Typically,
     * all views initialization should go here.
     */
    @CallSuper
    protected open fun init() {
        initSystemWindowColors()
        initScreenAwakeness()
        initIntercomPopups()
    }


    protected fun initSystemWindowColors() {
        for(type in SystemWindowType.values()) {
            if(shouldSetColorForSystemWindow(type)) {
                setSystemWindowColor(
                    color = getColorForSystemWindow(type),
                    type = type
                )
            }
        }
    }


    private fun initScreenAwakeness() {
        if(getSettings().shouldKeepScreenOn) {
            makeScreenAwake()
        }
    }


    private fun initIntercomPopups() {
        if(shouldShowIntercomInAppMessagePopups()) {
            mIntercomHandler.showInAppMessagePopups()
        } else {
            mIntercomHandler.hideInAppMessagePopups()
        }
    }


    /**
     * Called right after [init] method is called. Can be useful
     * for performing some tasks after views initialization.
     */
    @CallSuper
    protected open fun postInit() {
        overrideEnterTransition(getEnterTransitionAnimations())
    }


    private fun updateStringProviderLocale() {
        // Needs to be called to prevent using device's locale
        // when the application restarts (low memory, etc.)
        mStringProvider.setLocale(getSettings().language.locale)
    }


    override fun showToast(message: String) {
        shortToast(message)
    }


    override fun showLongToast(message: String) {
        longToast(message)
    }


    override fun showMaterialDialog(builder: MaterialDialogBuilder) {
        mMaterialDialog = buildMaterialDialog(builder, getAppTheme())
        mMaterialDialog?.show()
    }


    override fun hideMaterialDialog() {
        mMaterialDialog?.dismiss()
        mMaterialDialog = null
    }


    private fun registerNetworkStateReceiver() {
        if(!canObserveNetworkStateChanges() || ((mNetworkStateReceiver != null) && mIsNetworkStateReceiverRegistered)) {
            return
        }

        mNetworkStateReceiver = NetworkStateReceiver(this, this)
        registerReceiver(mNetworkStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        mIsNetworkStateReceiverRegistered = true
    }


    private fun unregisterNetworkStateReceiver() {
        if(!canObserveNetworkStateChanges() || ((mNetworkStateReceiver == null) && !mIsNetworkStateReceiverRegistered)) {
            return
        }

        unregisterReceiver(mNetworkStateReceiver)

        mIsNetworkStateReceiverRegistered = false
    }


    private fun registerAppLockReceiver() {
        if((mAppLockReceiver != null) && mIsAppLockReceiverRegistered) {
            return
        }

        mAppLockReceiver = AppLockReceiver(this)
        registerReceiver(mAppLockReceiver, IntentFilter(AppLockReceiver.ACTION_APP_LOCK))

        mIsAppLockReceiverRegistered = true
    }


    private fun unregisterAppLockReceiver() {
        if(!mIsAppLockReceiverRegistered || (mAppLockReceiver == null)) {
            return
        }

        unregisterReceiver(mAppLockReceiver)

        mIsAppLockReceiverRegistered = false
    }


    protected open fun canObserveNetworkStateChanges(): Boolean = false


    /**
     * Returns an ID of a layout that this activity is associated with.
     *
     * @return The ID of the layout
     */
    @LayoutRes
    protected abstract fun getContentLayoutResourceId(): Int


    protected open fun getColorForSystemWindow(type: SystemWindowType): Int {
        val generalTheme = getAppTheme().generalTheme

        return when(type) {
            SystemWindowType.STATUS_BAR,
            SystemWindowType.NAVIGATION_BAR -> generalTheme.primaryDarkColor
            SystemWindowType.RECENT_APPS_TOOLBAR -> generalTheme.primaryColor
        }
    }


    /**
     * A method for retrieving a string that also takes
     * into account the user's selected language.
     *
     * @param id The ID resource of the string
     *
     * @return The string
     */
    protected fun getStr(@StringRes id: Int): String {
        return mStringProvider.getString(id)
    }


    protected fun getSettings(): Settings {
        return mSessionManager.getSettings()
    }


    protected open fun getAppTheme(): Theme {
        return getSettings().theme
    }


    /**
     * Returns transition animations for the entering window.
     */
    protected open fun getEnterTransitionAnimations(): TransitionAnimations {
        return getTransitionAnimations()
    }


    /**
     * Returns transition animations for the exiting window.
     */
    protected open fun getExitTransitionAnimations(): TransitionAnimations {
        return getTransitionAnimations()
    }


    /**
     * Returns transition animations for both the entering and exiting windows.
     */
    protected open fun getTransitionAnimations(): TransitionAnimations {
        return TransitionAnimations.DEFAULT_ANIMATIONS
    }


    /**
     * Called before [preViewInflation] is called. Can be useful for
     * determining whether the activity should be initialized or not
     * for some reasons.
     *
     * @return true if activity should be initialized; false otherwise
     */
    protected open fun shouldInitActivity(): Boolean = true


    protected open fun shouldUpdateStringProviderLocale(): Boolean = true


    protected open fun shouldSetColorForSystemWindow(type: SystemWindowType): Boolean = true


    protected open fun shouldUpdateLastInteractionTime(): Boolean = true


    protected open fun shouldShowIntercomInAppMessagePopups(): Boolean = true


    /**
     * Checks whether this activity has been initialized,
     * i.e. whether [onCreate] method has finished.
     *
     * @return true if initialization has finished; false otherwise
     */
    override fun isInitialized(): Boolean {
        return mIsInitialized
    }


    override fun onResume() {
        super.onResume()

        mPresenter.start()

        registerNetworkStateReceiver()
        registerAppLockReceiver()
    }


    override fun onPause() {
        super.onPause()

        mPresenter.stop()

        unregisterNetworkStateReceiver()
        unregisterAppLockReceiver()
    }


    override fun onUserInteraction() {
        super.onUserInteraction()

        if(shouldUpdateLastInteractionTime()) {
            mAppLockManager.updateLastInteractionTimestamp(this, System.currentTimeMillis())
        }
    }


    @CallSuper
    override fun onConnected() {
        if(isInitialized()) {
            mPresenter.onNetworkConnected()
            supportFragmentManager.fragments.handleNetworkConnectionEvent()
        }
    }


    @CallSuper
    override fun onDisconnected() {
        if(isInitialized()) {
            mPresenter.onNetworkDisconnected()
            supportFragmentManager.fragments.handleNetworkDisconnectionEvent()
        }
    }


    @CallSuper
    override fun onAppLockRequested() {
        mAppLockManager.authenticate(this)
    }


    override fun navigateBack(): Boolean {
        finishActivity()
        return false
    }


    override fun onBackPressed() {
        if(!isInitialized()) {
            return
        }

        val isConsumedByPresenter = mPresenter.onBackPressed()
        val isConsumedByFragments = handleBackPressEvent()

        if(!isConsumedByFragments && !isConsumedByPresenter) {
            super.onBackPressed()
            overrideExitTransition(getExitTransitionAnimations())
        }
    }


    protected open fun handleBackPressEvent(): Boolean {
        return supportFragmentManager.fragments.handleBackPressEvent()
    }


    fun finishActivity() {
        finish()
        overrideExitTransition(getExitTransitionAnimations())
    }


    /**
     * A callback for fetching extras.
     */
    protected open fun onFetchExtras(extras: Bundle) {
        // Stub
    }


    private fun onRestoreStateInternal(savedState: Bundle) = with(savedState) {
        mPresenter.onRestoreState(getParcelableOrThrow(KEY_PRESENTER))

        onRestoreState(this)
    }


    /**
     * A callback for restoring state.
     */
    protected open fun onRestoreState(savedState: Bundle) {
        // Stub
    }


    final override fun onSaveInstanceState(outState: Bundle) {
        onSaveStateInternal(outState)

        super.onSaveInstanceState(outState)
    }


    private fun onSaveStateInternal(savedState: Bundle) = with(savedState) {
        val presenterState = SavedState().also { mPresenter.onSaveState(it) }

        putParcelable(KEY_PRESENTER, presenterState)

        onSaveState(this)
    }


    /**
     * A callback for saving state.
     */
    protected open fun onSaveState(savedState: Bundle) {
        // Stub
    }


    @CallSuper
    override fun onDestroy() {
        super.onDestroy()

        onRecycle()
    }


    /**
     * A callback for recycling components.
     */
    @CallSuper
    protected open fun onRecycle() {
        // Stub
    }


}