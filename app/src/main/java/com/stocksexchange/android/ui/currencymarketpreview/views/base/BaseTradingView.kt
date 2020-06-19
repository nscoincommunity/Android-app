package com.stocksexchange.android.ui.currencymarketpreview.views.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.core.view.updateLayoutParams
import com.stocksexchange.android.Constants.CLASS_LOADER
import com.stocksexchange.android.R
import com.stocksexchange.android.model.DataActionItem
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.ui.views.base.containers.BaseRelativeLayoutView
import com.stocksexchange.android.ui.views.base.interfaces.AdvancedView
import com.stocksexchange.android.utils.extensions.*
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.listeners.OnMainViewShowAnimationListener
import org.koin.core.inject

/**
 * A base view that contains lots of functionality related to trading.
 */
@Suppress("LeakingThis")
abstract class BaseTradingView<Data, DataItem, Params: Parcelable> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseRelativeLayoutView(context, attrs, defStyleAttr), AdvancedView<Data> {


    companion object {

        internal const val ATTRIBUTE_KEY_PROGRESS_BAR_COLOR = "progress_bar_color"
        internal const val ATTRIBUTE_KEY_INFO_VIEW_COLOR = "info_view_color"
        internal const val ATTRIBUTE_KEY_INFO_VIEW_ICON = "info_view_icon"

        internal const val DEFAULT_PROGRESS_BAR_COLOR = Color.GREEN
        internal const val DEFAULT_INFO_VIEW_COLOR = Color.GRAY

        private const val MAIN_VIEW_ANIMATION_DURATION = 300L

    }


    protected var mDataParameters: Params = getDefaultParameters()

    protected var mData: Data? = null

    protected val mNumberFormatter: NumberFormatter by inject()

    var onMainViewShowAnimationListener: OnMainViewShowAnimationListener<*>? = null




    override fun init() {
        initProgressBar()
        initInfoView()
    }


    protected open fun initProgressBar() {
        hideProgressBar()
    }


    protected open fun initInfoView() {
        hideInfoView()
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setProgressBarColor(get(ATTRIBUTE_KEY_PROGRESS_BAR_COLOR, DEFAULT_PROGRESS_BAR_COLOR))
            setInfoViewColor(get(ATTRIBUTE_KEY_INFO_VIEW_COLOR, DEFAULT_INFO_VIEW_COLOR))
            setInfoViewIcon(get(ATTRIBUTE_KEY_INFO_VIEW_ICON, getCompatDrawable(getDefaultInfoViewIconResourceId())))
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
        getProgressBar().makeVisible()
    }


    override fun hideProgressBar() {
        getProgressBar().makeGone()
    }


    override fun showEmptyView(caption: String) {
        setInfoViewCaption(caption)
        getInfoView().makeVisible()
    }


    override fun showErrorView(caption: String) {
        setInfoViewCaption(caption)
        getInfoView().makeVisible()
    }


    override fun hideInfoView() {
        getInfoView().makeGone()
    }


    fun setProgressBarColor(@ColorInt color: Int) {
        getProgressBar().setColor(color)
    }


    fun setInfoViewIconColor(@ColorInt color: Int) {
        getInfoView().setIconColor(color)
    }


    fun setInfoViewCaptionColor(@ColorInt color: Int) {
        getInfoView().setCaptionColor(color)
    }


    fun setInfoViewColor(@ColorInt color: Int) {
        getInfoView().setColor(color)
    }


    fun setProgressBarSize(size: Int) {
        getProgressBar().setSize(size)
    }


    fun setInfoViewIconSize(size: Int) {
        getInfoView().setIconSize(size)
    }


    fun setInfoViewVerticalDistance(distance: Int) {
        getInfoView().setViewsVerticalDistance(distance)
    }


    fun setInfoViewCaptionTextSize(textSize: Float, unit: Int = TypedValue.COMPLEX_UNIT_PX) {
        getInfoView().setCaptionTextSize(textSize, unit)
    }


    fun setProgressBarPosition(position: ProgressBarPosition) {
        getProgressBar().updateLayoutParams<LayoutParams> {
            // Clearing any previous set rules
            removeRule(ALIGN_PARENT_TOP)
            removeRule(CENTER_IN_PARENT)
            removeRule(ALIGN_PARENT_BOTTOM)

            // Setting the new one
            addRule(when(position) {
                ProgressBarPosition.TOP -> ALIGN_PARENT_TOP
                ProgressBarPosition.CENTER -> CENTER_IN_PARENT
                ProgressBarPosition.BOTTOM -> ALIGN_PARENT_BOTTOM
            })
        }
    }


    fun setInfoViewCaption(text: String) {
        getInfoView().setCaption(text)
    }


    fun setInfoViewIcon(drawable: Drawable?) {
        getInfoView().setIcon(drawable)
    }


    private fun setDataParameters(dataParameters: Params) {
        mDataParameters = dataParameters
    }


    override fun setData(data: Data?, shouldBindData: Boolean) {
        mData = truncateData(data)

        if(isDataEmpty() && isDataAlreadyBound()) {
            clearData()
        }

        if(shouldBindData) {
            bindData()
        }
    }


    /**
     * Updates the data. By default calls [setData] method
     * and binds the new data.
     *
     * Should be overridden to provide custom implementation.
     *
     * @param data The updated data
     * @param dataActionItems The data action items that have changed
     */
    open fun updateData(data: Data, dataActionItems: List<DataActionItem<DataItem>>) {
        setData(data, true)
    }


    /**
     * Truncates the passed in data to the appropriate size.
     *
     * @param data The data to truncate
     *
     * @return The truncated data
     */
    protected open fun truncateData(data: Data?): Data? = data


    override fun clearData() {
        mData = null
    }


    /**
     * Returns a boolean value denoting whether the data has already been
     * bound to the main view, meaning whether [bindData] has already
     * run.
     *
     * @return true if already bound; false otherwise
     */
    abstract fun isDataAlreadyBound(): Boolean


    protected open fun getDefaultInfoViewIconResourceId(): Int {
        return R.mipmap.ic_information_stub
    }


    fun getDataParameters(): Params {
        return mDataParameters
    }


    protected abstract fun getDefaultParameters(): Params


    protected abstract fun getProgressBar(): ProgressBar


    protected abstract fun getInfoView(): InfoView


    protected abstract fun getMainView(): View


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state.fetchProperState())

        if(state is BaseTradingSavedState<*>) {
            setDataParameters(state.dataParameters as Params)
        }
    }


    @CallSuper
    protected open fun onSaveInstanceState(savedState: BaseTradingSavedState<Params>) {
        with(savedState) {
            dataParameters = mDataParameters
        }
    }


    /**
     * An enumeration of all possible positions that a progress
     * bar can have in this view.
     */
    enum class ProgressBarPosition {

        TOP,
        CENTER,
        BOTTOM

    }


    abstract class BaseTradingSavedState<Params: Parcelable> : BaseSavedState {

        companion object {

            private const val KEY_DATA_PARAMETERS = "data_parameters"

        }


        internal lateinit var dataParameters: Params


        constructor(parcel: Parcel): super(parcel) {
            parcel.readBundle(CLASS_LOADER)?.run {
                dataParameters = getParcelableOrThrow(KEY_DATA_PARAMETERS)
            }
        }


        constructor(superState: Parcelable?): super(superState)


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)

            parcel.writeBundle(Bundle(CLASS_LOADER).apply {
                putParcelable(KEY_DATA_PARAMETERS, dataParameters)
            })
        }

    }


}