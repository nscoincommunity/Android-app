package com.stocksexchange.android.ui.base.fragments

import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.android.model.NavigationDeepLinkData
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.NavigationDeepLinkHandler
import com.stocksexchange.android.utils.extensions.containsAnyDestination
import com.stocksexchange.android.utils.extensions.getDestinationArgs
import com.stocksexchange.android.utils.listeners.OnSettingsChangeListener
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.interfaces.CanObserveNetworkStateChanges
import com.stocksexchange.core.utils.interfaces.Scrollable
import com.stocksexchange.core.utils.interfaces.Selectable
import com.stocksexchange.core.utils.listeners.BottomNavigationManager
import com.stocksexchange.core.utils.listeners.OnBackPressListener

class NavigationFragment : NavHostFragment(), Scrollable, Selectable,
    CanObserveNetworkStateChanges, OnBackPressListener, OnSettingsChangeListener {


    companion object {

        private const val KEY_GRAPH_ID = "android-support-nav:fragment:graphId"
        private const val KEY_START_DESTINATION_ARGS = "android-support-nav:fragment:startDestinationArgs"

        private val HIDDEN_KEYBOARD_DESTINATION_IDS = listOf(
            R.id.currencyMarketPreviewDest,
            R.id.tradeDest
        )


        fun newDeepLinkArgs(deepLinkData: NavigationDeepLinkData): Bundle {
            return bundleOf(NavigationDeepLinkHandler.KEY_NAVIGATION_DEEP_LINK_DATA to deepLinkData)
        }


        fun newInstance(
            @NavigationRes graphResId: Int,
            startDestinationArgs: Bundle?
        ) : NavigationFragment {
            return NavigationFragment().apply {
                arguments = bundleOf(
                    KEY_GRAPH_ID to graphResId,
                    KEY_START_DESTINATION_ARGS to startDestinationArgs
                )
            }
        }

    }


    var bottomNavigationManager: BottomNavigationManager? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        adjustStartDestinationArgsBundle()

        super.onCreate(savedInstanceState)

        findNavController().addOnDestinationChangedListener { _, _, _ ->
            if(shouldHideKeyboard(findNavController())) {
                hideBottomNavigation()
            } else {
                showBottomNavigation()
            }
        }
    }


    private fun adjustStartDestinationArgsBundle() {
        // Setting the class loader because sometimes it is null
        // and exceptions are thrown in those cases
        arguments?.apply {
            getBundle(KEY_START_DESTINATION_ARGS)?.classLoader = Constants.CLASS_LOADER
        }
    }


    private fun shouldHideKeyboard(navController: NavController): Boolean {
        return navController.containsAnyDestination(HIDDEN_KEYBOARD_DESTINATION_IDS)
    }


    private fun showBottomNavigation() = bottomNavigationManager?.showBottomNavigation()


    private fun hideBottomNavigation() = bottomNavigationManager?.hideBottomNavigation()


    override fun scrollToTop() {
        childFragmentManager.visibleFragment?.attemptToScrollUp()
    }


    override fun setSelected(isSelected: Boolean, source: Selectable.Source) {
        childFragmentManager.visibleFragment?.attemptToSelect(isSelected, source)
    }


    override fun isSelected(): Boolean = (childFragmentManager.visibleFragment != null)


    override fun onSelected() {}


    override fun onUnselected() {}


    override fun onNetworkConnected() {
        childFragmentManager.fragments.handleNetworkConnectionEvent()
    }


    override fun onNetworkDisconnected() {
        childFragmentManager.fragments.handleNetworkDisconnectionEvent()
    }


    override fun onBackPressed(): Boolean {
        return (childFragmentManager.visibleFragment?.handleBackPressEvent() ?: false)
    }


    override fun onSettingsChanged(newSettings: Settings) {
        val navController = findNavController()
        val currentDestinationId = (navController.currentDestination?.id ?: return)
        val currentDestinationArgs = navController.getDestinationArgs(currentDestinationId)

        // Recreating a fragment
        findNavController().navigate(
            currentDestinationId,
            currentDestinationArgs,
            NavOptions.Builder()
                .setPopUpTo(currentDestinationId, true)
                .build()
        )
    }


}