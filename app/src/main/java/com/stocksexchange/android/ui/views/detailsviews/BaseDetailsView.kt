package com.stocksexchange.android.ui.views.detailsviews

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import com.stocksexchange.android.Constants.CLASS_LOADER
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.ui.views.base.containers.BaseFrameLayoutView
import com.stocksexchange.android.ui.views.base.interfaces.AdvancedView
import com.stocksexchange.android.ui.views.mapviews.DottedMapView
import com.stocksexchange.core.utils.extensions.fetchProperState
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible
import com.stocksexchange.core.utils.extensions.setColor
import com.stocksexchange.core.utils.listeners.OnMainViewShowAnimationListener

/**
 * A base view class that contains common functionality for
 * all details views.
 */
@Suppress("LeakingThis")
abstract class BaseDetailsView<Data : Parcelable> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseFrameLayoutView(context, attrs, defStyleAttr), AdvancedView<Data> {


    companion object {

        internal const val ATTRIBUTE_KEY_ITEM_TITLE_COLOR = "item_title_color"
        internal const val ATTRIBUTE_KEY_ITEM_VALUE_COLOR = "item_value_color"
        internal const val ATTRIBUTE_KEY_ITEM_SEPARATOR_COLOR = "item_separator_color"
        internal const val ATTRIBUTE_KEY_PROGRESS_BAR_COLOR = "progress_bar_color"
        internal const val ATTRIBUTE_KEY_INFO_VIEW_COLOR = "info_view_color"

        internal const val DEFAULT_ITEM_TITLE_COLOR = Color.LTGRAY
        internal const val DEFAULT_ITEM_VALUE_COLOR = Color.LTGRAY
        internal const val DEFAULT_ITEM_SEPARATOR_COLOR = Color.LTGRAY

        internal const val DEFAULT_PROGRESS_BAR_COLOR = Color.GREEN
        internal const val DEFAULT_INFO_VIEW_COLOR = Color.GRAY

        internal const val MAIN_VIEW_ANIMATION_DURATION = 300L

    }


    protected var mData: Data? = null

    private lateinit var mDottedMapViews: Array<DottedMapView>

    var onMainViewShowAnimationListener: OnMainViewShowAnimationListener<*>? = null




    override fun init() {
        super<BaseFrameLayoutView>.init()

        if(hasProgressBar()) {
            initProgressBar()
        }

        if(hasInfoView()) {
            initInfoView()
        }

        initDottedMapViews()
    }


    protected open fun initProgressBar() {
        hideProgressBar()
    }


    protected open fun initInfoView() {
        hideInfoView()
    }


    protected open fun initDottedMapViews() {
        mDottedMapViews = getDottedMapViewsArray()

        initDottedMapViewsTitles()
    }


    private fun initDottedMapViewsTitles() {
        val titlesArray = getDottedMapViewsTitlesArray()

        mDottedMapViews.forEachIndexed { index, dottedMapView ->
            dottedMapView.setTitleText(titlesArray[index])
        }
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setItemTitleColor(get(ATTRIBUTE_KEY_ITEM_TITLE_COLOR, DEFAULT_ITEM_TITLE_COLOR))
            setItemValueColor(get(ATTRIBUTE_KEY_ITEM_VALUE_COLOR, DEFAULT_ITEM_VALUE_COLOR))
            setItemSeparatorColor(get(ATTRIBUTE_KEY_ITEM_SEPARATOR_COLOR, DEFAULT_ITEM_SEPARATOR_COLOR))
            setProgressBarColor(get(ATTRIBUTE_KEY_PROGRESS_BAR_COLOR, DEFAULT_PROGRESS_BAR_COLOR))
            setInfoViewColor(get(ATTRIBUTE_KEY_INFO_VIEW_COLOR, DEFAULT_INFO_VIEW_COLOR))
        }
    }


    override fun showMainView(shouldAnimate: Boolean) {
        if(shouldAnimate) {
            getMainView().alpha = 0f
            getMainView()
                .animate()
                .alpha(1f)
                .setInterpolator(LinearInterpolator())
                .setDuration(MAIN_VIEW_ANIMATION_DURATION)
                .setListener(onMainViewShowAnimationListener)
                .start()
        } else {
            getMainView().alpha = 1f
        }
    }


    override fun hideMainView() {
        getMainView().animate().cancel()

        if(getMainView().alpha != 0f) {
            getMainView().alpha = 0f
        }
    }


    override fun showProgressBar() {
        if(!hasProgressBar()) {
            return
        }

        getProgressBar()?.makeVisible()
    }


    override fun hideProgressBar() {
        if(!hasProgressBar()) {
            return
        }

        getProgressBar()?.makeGone()
    }


    override fun showEmptyView(caption: String) {
        if(!hasInfoView()) {
            return
        }

        getInfoView()?.apply {
            setCaption(caption)
            makeVisible()
        }
    }


    override fun showErrorView(caption: String) {
        if(!hasInfoView()) {
            return
        }

        getInfoView()?.apply {
            setCaption(caption)
            makeVisible()
        }
    }


    override fun hideInfoView() {
        if(!hasInfoView()) {
            return
        }

        getInfoView()?.makeGone()
    }


    fun setItemTitleColor(@ColorInt color: Int) {
        mDottedMapViews.forEach {
            it.setTitleColor(color)
        }
    }


    fun setItemValueColor(@ColorInt color: Int) {
        mDottedMapViews.forEach {
            it.setValueColor(color)
        }
    }


    fun setItemSeparatorColor(@ColorInt color: Int) {
        mDottedMapViews.forEach {
            it.setSeparatorColor(color)
        }
    }


    fun setProgressBarColor(@ColorInt color: Int) {
        if(!hasProgressBar()) {
            return
        }

        getProgressBar()?.setColor(color)
    }


    fun setInfoViewCaptionColor(@ColorInt color: Int) {
        if(!hasInfoView()) {
            return
        }

        getInfoView()?.setCaptionColor(color)
    }


    fun setInfoViewIconColor(@ColorInt color: Int) {
        if(!hasInfoView()) {
            return
        }

        getInfoView()?.setIconColor(color)
    }


    fun setInfoViewColor(@ColorInt color: Int) {
        if(!hasInfoView()) {
            return
        }

        getInfoView()?.setColor(color)
    }


    fun setInfoViewIcon(drawable: Drawable?) {
        if(!hasInfoView()) {
            return
        }

        getInfoView()?.setIcon(drawable)
    }


    override fun setData(data: Data?, shouldBindData: Boolean) {
        mData = data

        if(shouldBindData) {
            bindData()
        }
    }


    open fun updateData(data: Data) {
        setData(data, true)
    }


    override fun clearData() {
        mData = null
    }


    override fun isDataEmpty(): Boolean {
        return (mData == null)
    }


    protected open fun hasProgressBar(): Boolean = false


    protected open fun hasInfoView(): Boolean = false


    /**
     * Should return a reference to an instance of [ProgressBar]
     * if this view has one. Returns null by default.
     *
     * @return The reference to the instance of [ProgressBar] or null
     * if the view does not have one
     */
    protected open fun getProgressBar(): ProgressBar? = null


    /**
     * Should return a reference to an informational view
     * if this view has one. Returns null by default.
     *
     * @return The reference to the informational view or null
     * if the view does not have one
     */
    protected open fun getInfoView(): InfoView? = null


    protected abstract fun getMainView(): View


    protected abstract fun getDottedMapViewsTitlesArray(): Array<String>


    protected abstract fun getDottedMapViewsArray(): Array<DottedMapView>


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state.fetchProperState())

        if(state is BaseDetailsSavedState<*>) {
            setData((state.data as Data?), true)
        }
    }


    @CallSuper
    protected open fun onSaveInstanceState(savedState: BaseDetailsSavedState<Data>) {
        with(savedState) {
            data = mData
        }
    }


    abstract class BaseDetailsSavedState<Data : Parcelable> : BaseSavedState {

        companion object {

            private const val KEY_DATA = "data"

        }


        internal var data: Data? = null


        constructor(parcel: Parcel): super(parcel) {
            parcel.readBundle(CLASS_LOADER)?.run {
                data = getParcelable(KEY_DATA)
            }
        }


        constructor(superState: Parcelable?): super(superState)


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)

            parcel.writeBundle(Bundle(CLASS_LOADER).apply {
                putParcelable(KEY_DATA, data)
            })
        }

    }


}