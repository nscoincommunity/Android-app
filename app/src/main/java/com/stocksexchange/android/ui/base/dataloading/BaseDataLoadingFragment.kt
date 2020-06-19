package com.stocksexchange.android.ui.base.dataloading

import android.graphics.drawable.Drawable
import androidx.annotation.CallSuper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.stocksexchange.android.Constants
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.core.utils.extensions.*
import org.koin.android.ext.android.get

/**
 * A base fragment that holds functionality related
 * to loading data.
 */
abstract class BaseDataLoadingFragment<P : BaseDataLoadingPresenter<*, *, Data, *>, Data> : BaseFragment<P>(),
    DataLoadingView<Data> {


    @CallSuper
    override fun init() {
        initProgressBar()
        initInfoView()
        initSwipeRefreshProgressBar()

        mRootView.onFetchScrollableViewInHierarchy {
            if(isInitialized()) {
                getInfoView().setBottomMargin(it.height)
                getProgressBar().setBottomMargin(it.height)
            }
        }
    }


    protected open fun initProgressBar() {
        val progressBar = getProgressBar()
        ThemingUtil.apply(progressBar, getAppTheme())

        adjustView(progressBar)
        hideProgressBar()
    }


    protected open fun initInfoView() {
        val infoView = getInfoView()
        infoView.setIcon(getInfoViewIcon(get()))

        adjustView(infoView)
        hideInfoView()

        ThemingUtil.apply(infoView, getAppTheme())
    }


    /**
     * A callback that may be used for adjusting particular
     * widgets (such as a progress bar, an info view, etc.).
     *
     * @param view The view to adjust
     */
    protected open fun adjustView(view: android.view.View) {
        // Override if needed
    }


    protected open fun initSwipeRefreshProgressBar() {
        with(getRefreshProgressBar()) {
            isEnabled = mPresenter.isRefreshProgressBarEnabled

            setOnRefreshListener {
                onRefreshData()
            }

            ThemingUtil.apply(this, getAppTheme())
        }
    }


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
        val mainView = getMainView()

        if(mainView.alpha != 0f) {
            mainView.alpha = 0f
        }
    }


    override fun showEmptyView(caption: String) {
        val infoView = getInfoView().apply {
            setCaption(caption)
        }

        if(!infoView.isVisible) {
            infoView.makeVisible()
        }
    }


    override fun showErrorView(caption: String) {
        val infoView = getInfoView().apply {
            setCaption(caption)
        }

        if(!infoView.isVisible) {
            infoView.makeVisible()
        }
    }


    override fun hideInfoView() {
        val infoView = getInfoView()

        if(!infoView.isGone) {
            infoView.makeGone()
        }
    }


    override fun showProgressBar() {
        val progressBar = getProgressBar()

        if(!progressBar.isVisible) {
            progressBar.makeVisible()
        }
    }


    override fun hideProgressBar() {
        val progressBar = getProgressBar()

        if(!progressBar.isGone) {
            progressBar.makeGone()
        }
    }


    override fun showRefreshProgressBar() {
        val swipeRefreshLayout = getRefreshProgressBar()

        if(!swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = true
        }
    }


    override fun hideRefreshProgressBar() {
        val swipeRefreshLayout = getRefreshProgressBar()

        if(swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }


    override fun enableRefreshProgressBar() {
        getRefreshProgressBar().enable()
    }


    override fun disableRefreshProgressBar() {
        getRefreshProgressBar().disable()
    }


    override fun isViewSelected(): Boolean {
        return isSelected()
    }


    override fun shouldDelaySelectionHandler(isSelected: Boolean): Boolean {
        return (super.shouldDelaySelectionHandler(isSelected) && isDataSourceEmpty())
    }


    protected abstract fun getInfoViewIcon(infoViewIconProvider: InfoViewIconProvider): Drawable?


    protected abstract fun getMainView(): android.view.View


    protected abstract fun getInfoView(): InfoView


    protected abstract fun getProgressBar(): ProgressBar


    protected abstract fun getRefreshProgressBar(): SwipeRefreshLayout


    /**
     * A callback that gets invoked when the user swipes to
     * refresh the data.
     */
    open fun onRefreshData() {
        mPresenter.onRefreshData()
    }


}