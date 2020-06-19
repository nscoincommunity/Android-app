package com.stocksexchange.android.ui.currencymarketpreview.views.base

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.tabs.TabLayout
import com.stocksexchange.android.Constants.CLASS_LOADER
import com.stocksexchange.android.model.ChartHighlight
import com.stocksexchange.android.ui.views.mapviews.SimpleMapView
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.listeners.adapters.OnTabSelectedListenerAdapter

/**
 * A base chart view that contains common functionality for trading charts.
 */
abstract class BaseTradingChartView<
    Data,
    DataItem,
    Params: Parcelable
> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseTradingView<Data, DataItem, Params>(context, attrs, defStyleAttr) {


    companion object {

        internal const val ATTRIBUTE_KEY_IS_CHART_ANIMATION_ENABLED = "is_chart_animation_enabled"
        internal const val ATTRIBUTE_KEY_CHART_INFO_FIELDS_DEFAULT_TEXT_COLOR = "chart_info_fields_default_text_color"
        internal const val ATTRIBUTE_KEY_CHART_AXIS_GRID_COLOR = "chart_axis_grid_color"
        internal const val ATTRIBUTE_KEY_CHART_HIGHLIGHTER_COLOR = "chart_highlighter_color"
        internal const val ATTRIBUTE_KEY_CHART_POSITIVE_COLOR = "chart_positive_color"
        internal const val ATTRIBUTE_KEY_CHART_NEGATIVE_COLOR = "chart_negative_color"
        internal const val ATTRIBUTE_KEY_CHART_NEUTRAL_COLOR = "chart_neutral_color"
        internal const val ATTRIBUTE_KEY_TAB_BACKGROUND_COLOR = "tab_background_color"
        internal const val ATTRIBUTE_KEY_TAB_BAR_TOP_PADDING = "tab_bar_top_padding"
        internal const val ATTRIBUTE_KEY_TAB_BAR_BOTTOM_PADDING = "tab_bar_bottom_padding"
        internal const val ATTRIBUTE_KEY_CHART_HEIGHT = "chart_height"
        internal const val ATTRIBUTE_KEY_MAIN_VIEW_TOP_PADDING = "main_view_top_padding"

        internal const val DEFAULT_IS_CHART_ANIMATION_ENABLED = true
        internal const val DEFAULT_CHART_INFO_FIELDS_TEXT_COLOR = Color.LTGRAY
        internal const val DEFAULT_CHART_AXIS_GRID_COLOR = Color.DKGRAY
        internal const val DEFAULT_CHART_HIGHLIGHTER_COLOR = Color.YELLOW
        internal const val DEFAULT_CHART_POSITIVE_COLOR = Color.GREEN
        internal const val DEFAULT_CHART_NEGATIVE_COLOR = Color.RED
        internal const val DEFAULT_CHART_NEUTRAL_COLOR = Color.BLUE
        internal const val DEFAULT_TAB_BACKGROUND_COLOR = Color.BLACK
        internal const val DEFAULT_TAB_BAR_TOP_PADDING = 0
        internal const val DEFAULT_TAB_BAR_BOTTOM_PADDING = 0

        internal const val DEFAULT_CHART_HEIGHT_IN_DP = 200
        internal const val DEFAULT_MAIN_VIEW_TOP_PADDING_IN_DP = 0

        internal const val DEFAULT_CHART_INFO_FIELD_VALUE = "-"

        internal const val CHART_HIGHLIGHTER_WIDTH = 0.5f

        private const val CHART_ANIMATION_DURATION = 300

    }


    protected var mIsTabSelectedProgrammatically: Boolean = false
    protected var mIsChartAnimationEnabled: Boolean = true

    protected var mChartInfoFieldsDefaultTextColor: Int = 0
    protected var mChartHighlighterColor: Int = 0
    protected var mChartPositiveColor: Int = 0
    protected var mChartNegativeColor: Int = 0
    protected var mChartNeutralColor: Int = 0

    protected var mSelectedTabPosition: Int = 0

    protected var mChartHighlight: ChartHighlight? = null

    var onChartTouchListener: OnChartTouchListener? = null




    override fun init() {
        super.init()

        initChartInfoFields()
        initChart()

        if(shouldInitTabs()) {
            initTabLayout()
        }
    }


    protected open fun initChartInfoFields() {
        getChartInfoFieldsArray().forEach {
            it.setValueTypefaceStyle(Typeface.NORMAL)
        }

        if(isDataEmpty()) {
            resetChartInfoFieldsValues()
        }
    }


    @CallSuper
    protected open fun initChart() {
        with(getChart()) {
            description.isEnabled = false
            setDrawGridBackground(false)
            isDoubleTapToZoomEnabled = false
            setPinchZoom(true)
            isScaleXEnabled = true
            isScaleYEnabled = false
            setNoDataText("")
            legend.isEnabled = false
            setViewPortOffsets(0f, 0f, 0f, 0f)
            setOnChartValueSelectedListener(mOnChartValueSelectedListener)
            setOnTouchListener(mOnChartTouchListener)

            initChartHorizontalAxis(xAxis)
            initChartLeftAxis(axisLeft)
            initChartRightAxis(axisRight)
        }
    }


    @CallSuper
    protected open fun initChartHorizontalAxis(axis: XAxis) {
        with(axis) {
            setDrawGridLines(true)
            setDrawAxisLine(false)
            setDrawLabels(false)
            position = XAxis.XAxisPosition.BOTTOM
        }
    }


    @CallSuper
    protected open fun initChartLeftAxis(axis: YAxis) {
        with(axis) {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setDrawLabels(false)
            setDrawZeroLine(false)
        }
    }


    @CallSuper
    protected open fun initChartRightAxis(axis: YAxis) {
        with(axis) {
            setDrawGridLines(true)
            setDrawAxisLine(false)
            setDrawLabels(false)
            setDrawZeroLine(false)
        }
    }


    @CallSuper
    protected open fun initTabLayout() {
        with(getTabLayout()) {
            addTabLayoutTabs()
            setSelectedTabPosition(mSelectedTabPosition, true)
            addOnTabSelectedListener(mOnTabSelectionListener)
        }
    }


    protected abstract fun addTabLayoutTabs()


    override fun applyAttributes() {
        super.applyAttributes()

        with(mAttributes) {
            setChartAnimationEnabled(get(ATTRIBUTE_KEY_IS_CHART_ANIMATION_ENABLED, DEFAULT_IS_CHART_ANIMATION_ENABLED))
            setChartInfoFieldsDefaultTextColor(get(ATTRIBUTE_KEY_CHART_INFO_FIELDS_DEFAULT_TEXT_COLOR, DEFAULT_CHART_INFO_FIELDS_TEXT_COLOR))
            setChartAxisGridColor(get(ATTRIBUTE_KEY_CHART_AXIS_GRID_COLOR, DEFAULT_CHART_AXIS_GRID_COLOR))
            setChartHighlighterColor(get(ATTRIBUTE_KEY_CHART_HIGHLIGHTER_COLOR, DEFAULT_CHART_HIGHLIGHTER_COLOR))
            setChartPositiveColor(get(ATTRIBUTE_KEY_CHART_POSITIVE_COLOR, DEFAULT_CHART_POSITIVE_COLOR))
            setChartNegativeColor(get(ATTRIBUTE_KEY_CHART_NEGATIVE_COLOR, DEFAULT_CHART_NEGATIVE_COLOR))
            setChartNeutralColor(get(ATTRIBUTE_KEY_CHART_NEUTRAL_COLOR, DEFAULT_CHART_NEUTRAL_COLOR))
            setTabBackgroundColor(get(ATTRIBUTE_KEY_TAB_BACKGROUND_COLOR, DEFAULT_TAB_BACKGROUND_COLOR))
            setTabBarTopPadding(get(ATTRIBUTE_KEY_TAB_BAR_TOP_PADDING, dpToPx(DEFAULT_TAB_BAR_TOP_PADDING)))
            setTabBarBottomPadding(get(ATTRIBUTE_KEY_TAB_BAR_BOTTOM_PADDING, dpToPx(DEFAULT_TAB_BAR_BOTTOM_PADDING)))
            setChartHeight(get(ATTRIBUTE_KEY_CHART_HEIGHT, dpToPx(DEFAULT_CHART_HEIGHT_IN_DP)))
            setMainViewTopPadding(get(ATTRIBUTE_KEY_MAIN_VIEW_TOP_PADDING, dpToPx(DEFAULT_MAIN_VIEW_TOP_PADDING_IN_DP)))
        }
    }


    protected fun updateChartInfoFields(newTitles: Array<String>, newValues: Array<String>) {
        if((newTitles.size != getChartInfoFieldsArray().size) ||
            (newValues.size != getChartInfoFieldsArray().size)) {
            return
        }

        getChartInfoFieldsArray().forEachIndexed { index, simpleMapView ->
            with(simpleMapView) {
                setTitleText(newTitles[index])
                setValueText(newValues[index])
            }
        }
    }


    protected open fun createHighlight(x: Float, dataSetIndex: Int): Highlight {
        return Highlight(x, dataSetIndex, -1)
    }


    protected fun clearChartHighlight() {
        mChartHighlight = null

        resetChartInfoFieldsValues()
        resetChartInfoFieldsTextColor()
    }


    private fun setSelectedTabPosition(selectedTabPosition: Int, isTabSelectedProgrammatically: Boolean) {
        mIsTabSelectedProgrammatically = isTabSelectedProgrammatically

        getTabLayout().getTabAt(selectedTabPosition)?.select()

        mIsTabSelectedProgrammatically = false
    }


    private fun resetChartInfoFieldsValues() {
        getChartInfoFieldsArray().forEach {
            it.setValueText(DEFAULT_CHART_INFO_FIELD_VALUE)
        }
    }


    private fun resetChartInfoFieldsTextColor() {
        setChartInfoFieldsTextColor(mChartInfoFieldsDefaultTextColor)
    }


    override fun showMainView(shouldAnimate: Boolean) {
        getMainView().makeVisible()

        if(shouldAnimate && mIsChartAnimationEnabled) {
            animateChart(CHART_ANIMATION_DURATION, Easing.Linear)

            // 25L as a compensation just to be sure that the callback
            // is invoked only after the animation has ended
            postDelayed(
                { onMainViewShowAnimationListener?.onAnimationEnd() },
                (CHART_ANIMATION_DURATION.toLong() + 25L)
            )

            onMainViewShowAnimationListener?.onAnimationStart()
        } else {
            onMainViewShowAnimationListener?.onAnimationEnd()
        }
    }


    /**
     * Should perform animation of the chart using the specified parameters.
     *
     * @param duration The duration of the animation
     * @param easingFunction The easing function for the animation, i.e. interpolator
     */
    protected abstract fun animateChart(duration: Int, easingFunction: Easing.EasingFunction)


    override fun hideMainView() {
        getMainView().makeInvisible()
    }


    fun setChartAnimationEnabled(isChartAnimationEnabled: Boolean) {
        mIsChartAnimationEnabled = isChartAnimationEnabled
    }


    fun setChartInfoFieldsDefaultTextColor(@ColorInt color: Int) {
        mChartInfoFieldsDefaultTextColor = color

        setChartInfoFieldsTextColor(color)
    }


    fun setChartInfoFieldsTextColor(@ColorInt color: Int) {
        getChartInfoFieldsArray().forEach {
            it.setTextColor(color)
        }
    }


    fun setChartAxisGridColor(@ColorInt color: Int) {
        with(getChart()) {
            xAxis.gridColor = color
            axisLeft.gridColor = color
            axisRight.gridColor = color
        }
    }


    @CallSuper
    open fun setChartHighlighterColor(@ColorInt color: Int) {
        mChartHighlighterColor = color
    }


    @CallSuper
    open fun setChartPositiveColor(@ColorInt color: Int) {
        mChartPositiveColor = color
    }


    @CallSuper
    open fun setChartNegativeColor(@ColorInt color: Int) {
        mChartNegativeColor = color
    }


    @CallSuper
    open fun setChartNeutralColor(@ColorInt color: Int) {
        mChartNeutralColor = color
    }


    fun setTabBackgroundColor(@ColorInt color: Int) {
        getTabLayout().setBackgroundColor(color)
    }


    fun setTabBarTopPadding(padding: Int) {
        getTabLayout().setTopPadding(padding)
    }


    fun setTabBarBottomPadding(padding: Int) {
        getTabLayout().setBottomPadding(padding)
    }


    fun setTabTextColors(@ColorInt normalColor: Int, @ColorInt selectedColor: Int) {
        getTabLayout().setTabTextColors(normalColor, selectedColor)
    }


    fun setChartHeight(height: Int) {
        getChart().setHeight(height)
    }


    fun setMainViewTopPadding(padding: Int) {
        getMainView().setTopPadding(padding)
    }


    private fun setChartHighlight(chartHighlight: ChartHighlight?) {
        mChartHighlight = chartHighlight
    }


    protected fun setChartInfoFieldsColors(vararg colors: Int) {
        if(colors.size != getChartInfoFieldsArray().size) {
            return
        }

        getChartInfoFieldsArray().forEachIndexed { index, simpleMapView ->
            simpleMapView.setTextColor(colors[index])
        }
    }


    protected fun setChartInfoFieldsTitles(vararg titles: String) {
        if(titles.size != getChartInfoFieldsArray().size) {
            return
        }

        getChartInfoFieldsArray().forEachIndexed { index, simpleMapView ->
            simpleMapView.setTitleText(titles[index])
        }
    }


    protected fun setChartInfoFieldsValues(vararg values: String) {
        if(values.size != getChartInfoFieldsArray().size) {
            return
        }

        getChartInfoFieldsArray().forEachIndexed { index, simpleMapView ->
            simpleMapView.setValueText(values[index])
        }
    }


    override fun clearData() {
        super.clearData()

        getChart().clear()
        clearChartHighlight()
    }


    protected open fun shouldInitTabs(): Boolean {
        return true
    }


    override fun isDataAlreadyBound(): Boolean {
        return getChart().run { data != null && data.entryCount > 0 }
    }


    protected abstract fun getChart(): BarLineChartBase<*>


    protected abstract fun getTabLayout(): TabLayout


    protected abstract fun getTabLayoutTabIndices(): List<Int>


    protected abstract fun getChartInfoFieldsArray(): Array<SimpleMapView>


    /**
     * A callback that gets invoked whenever a chart value has been
     * selected.
     *
     * @param entry The selected entry
     * @param highlight The highlight
     */
    @CallSuper
    protected open fun onValueSelected(entry: Entry, highlight: Highlight) {
        mChartHighlight = ChartHighlight(
            highlight.x,
            highlight.dataSetIndex
        )
    }


    /**
     * A callback that gets invoked whenever a current selected value
     * has been deselected.
     */
    protected open fun onNothingSelected() {
        clearChartHighlight()
    }


    @CallSuper
    protected open fun onTabSelected(tab: TabLayout.Tab) {
        mSelectedTabPosition = tab.position
    }


    private val mOnChartValueSelectedListener = object : OnChartValueSelectedListener {

        override fun onValueSelected(entry: Entry, highlight: Highlight) {
            this@BaseTradingChartView.onValueSelected(entry, highlight)
        }

        override fun onNothingSelected() {
            this@BaseTradingChartView.onNothingSelected()
        }

    }


    private val mOnChartTouchListener = OnTouchListener { _, event ->
        if(!isDataEmpty()) {
            when(event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    onChartTouchListener?.onChartPressed()
                }

                android.view.MotionEvent.ACTION_UP -> {
                    onChartTouchListener?.onChartReleased()
                }
            }
        }

        false
    }


    private val mOnTabSelectionListener = object : OnTabSelectedListenerAdapter {

        override fun onTabSelected(tab: TabLayout.Tab) {
            this@BaseTradingChartView.onTabSelected(tab)
        }

    }


    /**
     * A listener to invoke to get notified when a user interacts
     * with a chart.
     */
    interface OnChartTouchListener {

        fun onChartPressed() {
            // Stub
        }

        fun onChartReleased() {
            // Stub
        }

    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)

        if(state is BaseTradingChartState<*>) {
            setSelectedTabPosition(state.selectedTabPosition, true)
            setChartHighlight(state.chartHighlight)
        }
    }


    override fun onSaveInstanceState(savedState: BaseTradingSavedState<Params>) {
        super.onSaveInstanceState(savedState)

        if(savedState is BaseTradingChartState) {
            with(savedState) {
                selectedTabPosition = mSelectedTabPosition
                chartHighlight = mChartHighlight
            }
        }
    }


    abstract class BaseTradingChartState<Params: Parcelable> : BaseTradingSavedState<Params> {

        companion object {

            private const val KEY_SELECTED_TAB_POSITION = "selected_tab_position"
            private const val KEY_CHART_HIGHLIGHT = "chart_highlight"

        }


        internal var selectedTabPosition: Int = 0

        internal var chartHighlight: ChartHighlight? = null


        constructor(parcel: Parcel): super(parcel) {
            parcel.readBundle(CLASS_LOADER)?.run {
                selectedTabPosition = getInt(KEY_SELECTED_TAB_POSITION, 0)
                chartHighlight = getParcelable(KEY_CHART_HIGHLIGHT)
            }
        }


        constructor(superState: Parcelable?): super(superState)


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)

            parcel.writeBundle(Bundle(CLASS_LOADER).apply {
                putInt(KEY_SELECTED_TAB_POSITION, selectedTabPosition)
                putParcelable(KEY_CHART_HIGHLIGHT, chartHighlight)
            })
        }

    }


}