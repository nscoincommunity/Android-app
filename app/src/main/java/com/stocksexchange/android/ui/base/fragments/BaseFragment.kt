package com.stocksexchange.android.ui.base.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.stocksexchange.android.analytics.FirebaseEventLogger
import com.stocksexchange.android.model.MaterialDialogBuilder
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.extensions.buildMaterialDialog
import com.stocksexchange.android.utils.managers.RealTimeDataManager
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.ui.base.mvp.views.BaseView
import com.stocksexchange.android.utils.NavigationDeepLinkHandler
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.navigation.NavOptionsCreator
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.handlers.CoroutineHandler
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.interfaces.CanObserveNetworkStateChanges
import com.stocksexchange.core.utils.interfaces.Selectable
import com.stocksexchange.core.utils.listeners.OnBackPressListener
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * A base fragment that contains common functionality for all fragments.
 */
abstract class BaseFragment<P : BasePresenter<*, *>> : Fragment(), BaseView, Selectable,
    CanObserveNetworkStateChanges, OnBackPressListener, NavigationDeepLinkHandler.Navigator {


    companion object {

        private const val KEY_IS_INITIAL_SELECTION_SET = "is_initial_selection_set"
        private const val KEY_IS_SELECTED = "is_selected"
        private const val KEY_PRESENTER = "presenter"

    }

    protected abstract val mPresenter: P

    private var mIsInitialized: Boolean = false
    private var mIsInitialSelectionSet: Boolean = false
    private var mIsSelected: Boolean = false

    protected lateinit var mRootView: View

    private var mMaterialDialog: MaterialDialog? = null

    protected val mStringProvider: StringProvider by inject()
    protected val mCoroutineHandler: CoroutineHandler by inject()

    protected val mNavOptionsCreator: NavOptionsCreator by inject()
    private val mNavigationDeepLinkHandler: NavigationDeepLinkHandler by inject { parametersOf(this) }

    protected val mSessionManager: SessionManager by inject()

    private val mRealTimeDataManager: RealTimeDataManager by inject()
    private val mFirebaseEventLogger: FirebaseEventLogger by inject()

    private var mHandler: Handler = Handler(Looper.getMainLooper())




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!mIsInitialized && (savedInstanceState == null)) {
            arguments?.let(::onFetchExtrasInternal)
        } else {
            savedInstanceState?.let(::onRestoreStateInternal)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getContentLayoutResourceId(), container, false).also {
            mRootView = it
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preInit()
        init()
        postInit()

        mIsInitialized = true
    }


    /**
     * Called right after [LayoutInflater.inflate] method is called.
     * Can be useful for performing some tasks before views
     * initialization.
     */
    @CallSuper
    protected open fun preInit() {
        setInitialSelectionState()
    }


    private fun setInitialSelectionState() {
        if(mIsInitialSelectionSet) {
            return
        }

        val navController = findNavController()
        val startDestinationId = navController.graph.startDestination
        val currentDestinationId = navController.currentDestination?.id

        mIsSelected = (startDestinationId != currentDestinationId)
        mIsInitialSelectionSet = true
    }


    /**
     * Called right after [onRestoreState] method is called. Typically,
     * all views initialization should go here.
     */
    protected open fun init() {
        // Stub
    }


    /**
     * Called right after [init] method is called. Can be useful
     * for performing some tasks after views initialization.
     */
    @CallSuper
    protected open fun postInit() {
        incrementDataUpdateEventConsumerCount()
    }


    override fun showToast(message: String) {
        ctx.shortToast(message)
    }


    override fun showLongToast(message: String) {
        ctx.longToast(message)
    }


    override fun showMaterialDialog(builder: MaterialDialogBuilder) {
        mMaterialDialog = ctx.buildMaterialDialog(builder, getAppTheme())
        mMaterialDialog?.show()
    }


    override fun hideMaterialDialog() {
        mMaterialDialog?.dismiss()
        mMaterialDialog = null
    }


    private fun incrementDataUpdateEventConsumerCount() {
        if(canReceiveRealTimeDataUpdateEvent()) {
            mRealTimeDataManager.incrementDataUpdateEventConsumerCount()
        }
    }


    private fun decrementDataUpdateEventConsumerCount() {
        if(canReceiveRealTimeDataUpdateEvent()) {
            mRealTimeDataManager.decrementDataUpdateEventConsumerCount()
        }
    }


    /**
     * Navigates to the specified destination screen.
     */
    open fun navigate(@IdRes destinationId: Int,
                      arguments: Bundle? = null) {
        findNavController().navigate(
            destinationId,
            arguments,
            getNavOptionsForDestination(destinationId)
        )
    }


    /**
     * Navigates back to the previous fragment by popping the back stack.
     *
     * @return true if the stack was popped and the destination changed;
     * false otherwise
     */
    override fun navigateBack(): Boolean {
        return findNavController().popBackStack()
    }


    override fun navigateTo(destinationId: Int, destinationArgs: Bundle?) {
        navigate(destinationId, destinationArgs)
    }


    override fun setSelected(isSelected: Boolean, source: Selectable.Source) {
        val runnable = Runnable {
            if(isSelected == isSelected()) {
                return@Runnable
            }

            if(isSelected) {
                onSelected()
            } else {
                onUnselected()
            }
        }

        mHandler.postDelayed(
            runnable,
            (if(shouldDelaySelectionHandler(isSelected)) source.delay else 0L)
        )
    }


    protected open fun shouldDelaySelectionHandler(isSelected: Boolean): Boolean {
        return (!isInitialized() || isSelected)
    }


    /**
     * Should return a boolean value denoting whether this activity
     * contains data that is real-time dependant and thus should
     * receive real-time data update events.
     */
    protected open fun canReceiveRealTimeDataUpdateEvent(): Boolean = false


    /**
     * Checks whether this fragment has been initialized,
     * i.e. whether [onCreateView] has finished.
     *
     * @return true if initialization has finished; false otherwise
     */
    override fun isInitialized(): Boolean {
        return mIsInitialized
    }


    override fun isSelected(): Boolean {
        return mIsSelected
    }


    @LayoutRes
    protected abstract fun getContentLayoutResourceId(): Int


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


    /**
     * Retrieves navigation options when navigating to a particular destination.
     *
     * @param id The ID of the destination
     *
     * @return The navigation options
     */
    protected open fun getNavOptionsForDestination(@IdRes id: Int): NavOptions {
        return mNavOptionsCreator.getDefaultNavOptions(id)
    }


    protected fun getSettings(): Settings {
        return mSessionManager.getSettings()
    }


    protected fun getAppTheme(): Theme {
        return getSettings().theme
    }


    protected open fun getCurrentlyVisibleChildFragment(): Fragment? {
        return childFragmentManager.visibleFragment
    }


    override fun onStart() {
        super.onStart()

        mNavigationDeepLinkHandler.handleDeepLink()
    }


    override fun onResume() {
        super.onResume()

        mPresenter.start()
        mFirebaseEventLogger.onScreenOpened(act, javaClass.simpleName)
    }


    override fun onPause() {
        super.onPause()

        mPresenter.stop()

        if(isRemoving) {
            decrementDataUpdateEventConsumerCount()
        }
    }


    @CallSuper
    override fun onSelected() {
        if(isSelected()) {
            return
        }

        mIsSelected = true

        if(isInitialized()) {
            getCurrentlyVisibleChildFragment()?.handleSelectionEvent()
            mPresenter.onViewSelected()
        }
    }


    @CallSuper
    override fun onUnselected() {
        if(!isSelected()) {
            return
        }

        mIsSelected = false

        if(isInitialized()) {
            getCurrentlyVisibleChildFragment()?.handleDeselectionEvent()
            mPresenter.onViewUnselected()
        }
    }


    @CallSuper
    override fun onNetworkConnected() {
        if(isInitialized()) {
            mPresenter.onNetworkConnected()
            childFragmentManager.fragments.handleNetworkConnectionEvent()
        }
    }


    @CallSuper
    override fun onNetworkDisconnected() {
        if(isInitialized()) {
            mPresenter.onNetworkDisconnected()
            childFragmentManager.fragments.handleNetworkDisconnectionEvent()
        }
    }


    @CallSuper
    override fun onBackPressed(): Boolean {
        if(!isInitialized()) {
            return false
        }

        val isConsumedByPresenter = mPresenter.onBackPressed()
        val isConsumedByChildren = handleBackPressEvent()

        return (isConsumedByChildren || isConsumedByPresenter)
    }


    private fun handleBackPressEvent(): Boolean {
        return childFragmentManager.fragments.handleBackPressEvent()
    }


    private fun onFetchExtrasInternal(extras: Bundle) {
        mNavigationDeepLinkHandler.onFetchExtras(extras)

        onFetchExtras(extras)
    }


    /**
     * A callback for fetching extras.
     */
    protected open fun onFetchExtras(extras: Bundle) {
        // Stub
    }


    private fun onRestoreStateInternal(savedState: Bundle) = with(savedState) {
        mIsInitialSelectionSet = getBoolean(KEY_IS_INITIAL_SELECTION_SET, false)
        mIsSelected = getBoolean(KEY_IS_SELECTED, false)

        mPresenter.onRestoreState(getParcelableOrThrow(KEY_PRESENTER))
        mNavigationDeepLinkHandler.onRestoreState(this)

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

        mNavigationDeepLinkHandler.onSaveState(this)

        putParcelable(KEY_PRESENTER, presenterState)
        putBoolean(KEY_IS_INITIAL_SELECTION_SET, mIsInitialSelectionSet)
        putBoolean(KEY_IS_SELECTED, mIsSelected)

        onSaveState(this)
    }


    /**
     * A callback for saving state.
     */
    protected open fun onSaveState(savedState: Bundle) {
        // Stub
    }


    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()

        mIsInitialized = false

        onRecycle()
    }


    /**
     * A callback for recycling components.
     */
    @CallSuper
    protected open fun onRecycle() {
        // Stub
    }


    override fun onDestroy() {
        super.onDestroy()

        mPresenter.onRecycle()
    }


}