package com.stocksexchange.android.ui.currencymarketpreview.views.chartviews

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.google.android.material.tabs.TabLayout
import com.stocksexchange.android.Constants.CLASS_LOADER
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.CandleStick
import com.stocksexchange.api.model.rest.PriceChartDataInterval
import com.stocksexchange.api.model.rest.parameters.PriceChartDataParameters
import com.stocksexchange.android.model.CandleStickStyle
import com.stocksexchange.android.model.CandleStickType
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.ui.views.mapviews.SimpleMapView
import com.stocksexchange.android.ui.currencymarketpreview.views.base.BaseTradingChartView
import com.stocksexchange.core.formatters.TimeFormatter
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.price_chart_view_layout.view.*
import org.koin.core.inject
import java.io.Serializable

/**
 * A chart view that displays candle sticks of a particular currency pair.
 */
class PriceChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseTradingChartView<List<CandleStick>, CandleStick, PriceChartDataParameters>(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_IS_CHART_ZOOM_IN_ENABLED = "is_chart_zoom_in_enabled"
        private const val ATTRIBUTE_KEY_CHART_VOLUME_BAR_COLOR = "chart_volume_bar_color"
        private const val ATTRIBUTE_KEY_CHART_CANDLE_STICK_SHADOW_COLOR = "chart_candle_stick_shadow_color"
        private const val ATTRIBUTE_KEY_MIN_CANDLE_STICK_COUNT_FOR_ZOOMING_IN = "min_candle_stick_count_for_zooming_in"

        private const val CHART_HIGHLIGHT_DATA_KEY_CS = "candle_stick"

        private const val CHART_HIGHLIGHT_DATA_KEY_CS_PAYLOAD_KEY_ILVS = "is_last_value_selected"
        private const val CHART_HIGHLIGHT_DATA_KEY_CS_PAYLOAD_KEY_CS = "candle_stick"

        private const val DEFAULT_IS_CHART_ZOOM_IN_ENABLED = true

        private const val DEFAULT_CHART_VOLUME_BAR_COLOR = Color.CYAN
        private const val DEFAULT_CHART_CANDLE_STICK_SHADOW_COLOR = Color.WHITE

        private const val DEFAULT_MIN_CANDLE_STICK_COUNT_FOR_ZOOMING_IN = 15

        private const val CHART_TRANSLATION_COMPENSATION = 2f
        private const val CHART_ZOOM_IN_Y_SCALE = 1f

        private const val CHART_SHADOW_WIDTH = 0.5f
        private const val CHART_HORIZONTAL_AXIS_COMPENSATION = 2f
        private const val CHART_VOLUME_BARS_COMPENSATION = 8f

        private const val CHART_CANDLE_DATA_SET_LABEL = "CandleDataSet"
        private const val CHART_VOLUME_DATA_SET_LABEL = "VolumeDataSet"

    }


    private var mIsChartZoomInEnabled: Boolean = true
    private var mWasChartZoomInPerformed: Boolean = false

    private var mChartVolumeBarColor: Int = 0
    private var mChartCandleStickShadowColor: Int = 0

    private var mMinCandleStickCountForZoomingIn: Int = 0

    private var mBullishCandleStickStyle: CandleStickStyle = CandleStickStyle.SOLID
    private var mBearishCandleStickStyle: CandleStickStyle = CandleStickStyle.SOLID

    private val mTimeFormatter: TimeFormatter by inject()

    var onChartIntervalChangeListener: OnChartIntervalChangeListener? = null




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.PriceChartView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_IS_CHART_ANIMATION_ENABLED, getBoolean(R.styleable.PriceChartView_isChartAnimationEnabled, DEFAULT_IS_CHART_ANIMATION_ENABLED))
                save(ATTRIBUTE_KEY_IS_CHART_ZOOM_IN_ENABLED, getBoolean(R.styleable.PriceChartView_isChartZoomInEnabled, DEFAULT_IS_CHART_ZOOM_IN_ENABLED))
                save(ATTRIBUTE_KEY_PROGRESS_BAR_COLOR, getColor(R.styleable.PriceChartView_progressBarColor, DEFAULT_PROGRESS_BAR_COLOR))
                save(ATTRIBUTE_KEY_INFO_VIEW_COLOR, getColor(R.styleable.PriceChartView_infoViewColor, DEFAULT_INFO_VIEW_COLOR))
                save(ATTRIBUTE_KEY_CHART_INFO_FIELDS_DEFAULT_TEXT_COLOR, getColor(R.styleable.PriceChartView_chartInfoFieldsDefaultTextColor, DEFAULT_CHART_INFO_FIELDS_TEXT_COLOR))
                save(ATTRIBUTE_KEY_CHART_AXIS_GRID_COLOR, getColor(R.styleable.PriceChartView_chartAxisGridColor, DEFAULT_CHART_AXIS_GRID_COLOR))
                save(ATTRIBUTE_KEY_CHART_HIGHLIGHTER_COLOR, getColor(R.styleable.PriceChartView_chartHighlighterColor, DEFAULT_CHART_HIGHLIGHTER_COLOR))
                save(ATTRIBUTE_KEY_CHART_POSITIVE_COLOR, getColor(R.styleable.PriceChartView_chartPositiveColor, DEFAULT_CHART_POSITIVE_COLOR))
                save(ATTRIBUTE_KEY_CHART_NEGATIVE_COLOR, getColor(R.styleable.PriceChartView_chartNegativeColor, DEFAULT_CHART_NEGATIVE_COLOR))
                save(ATTRIBUTE_KEY_CHART_NEUTRAL_COLOR, getColor(R.styleable.PriceChartView_chartNeutralColor, DEFAULT_CHART_NEUTRAL_COLOR))
                save(ATTRIBUTE_KEY_CHART_VOLUME_BAR_COLOR, getColor(R.styleable.PriceChartView_chartVolumeBarColor, DEFAULT_CHART_VOLUME_BAR_COLOR))
                save(ATTRIBUTE_KEY_CHART_CANDLE_STICK_SHADOW_COLOR, getColor(R.styleable.PriceChartView_chartCandleStickShadowColor, DEFAULT_CHART_CANDLE_STICK_SHADOW_COLOR))
                save(ATTRIBUTE_KEY_TAB_BACKGROUND_COLOR, getColor(R.styleable.PriceChartView_tabBackgroundColor, DEFAULT_TAB_BACKGROUND_COLOR))
                save(ATTRIBUTE_KEY_MIN_CANDLE_STICK_COUNT_FOR_ZOOMING_IN, getInteger(R.styleable.PriceChartView_minCandleStickCountForZoomingIn, DEFAULT_MIN_CANDLE_STICK_COUNT_FOR_ZOOMING_IN))
                save(ATTRIBUTE_KEY_CHART_HEIGHT, getDimensionPixelSize(R.styleable.PriceChartView_chartHeight, dpToPx(DEFAULT_CHART_HEIGHT_IN_DP)))
                save(ATTRIBUTE_KEY_MAIN_VIEW_TOP_PADDING, getDimensionPixelSize(R.styleable.PriceChartView_mainViewTopPadding, dpToPx(DEFAULT_MAIN_VIEW_TOP_PADDING_IN_DP)))
                save(ATTRIBUTE_KEY_TAB_BAR_TOP_PADDING, getDimensionPixelSize(R.styleable.PriceChartView_tabBarTopPadding, dpToPx(DEFAULT_TAB_BAR_TOP_PADDING)))
                save(ATTRIBUTE_KEY_TAB_BAR_BOTTOM_PADDING, getDimensionPixelSize(R.styleable.PriceChartView_tabBarBottomPadding, dpToPx(DEFAULT_TAB_BAR_BOTTOM_PADDING)))
                save(ATTRIBUTE_KEY_INFO_VIEW_ICON, (getDrawable(R.styleable.PriceChartView_infoViewIcon) ?: getCompatDrawable(getDefaultInfoViewIconResourceId())))
            }
        }
    }


    override fun initChartInfoFields() {
        super.initChartInfoFields()

        setChartInfoFieldsTitles(*getChartInfoFieldsTitlesArray())
    }


    override fun initChart() {
        super.initChart()

        with(chart) {
            drawOrder = arrayOf(
                CombinedChart.DrawOrder.BAR,
                CombinedChart.DrawOrder.CANDLE
            )
        }
    }


    override fun addTabLayoutTabs() {
        with(tabLayout) {
            for(interval in PriceChartDataInterval.values()) {
                addTab(newTab().setText(mStringProvider.getString(interval.stringId)), interval.ordinal)
            }
        }
    }


    override fun applyAttributes() {
        super.applyAttributes()

        with(mAttributes) {
            setChartZoomInEnabled(get(ATTRIBUTE_KEY_IS_CHART_ZOOM_IN_ENABLED, DEFAULT_IS_CHART_ZOOM_IN_ENABLED))
            setChartVolumeBarColor(get(ATTRIBUTE_KEY_CHART_VOLUME_BAR_COLOR, DEFAULT_CHART_VOLUME_BAR_COLOR))
            setChartCandleStickShadowColor(get(ATTRIBUTE_KEY_CHART_CANDLE_STICK_SHADOW_COLOR, DEFAULT_CHART_CANDLE_STICK_SHADOW_COLOR))
            setMinCandleStickCountForZoomingIn(get(ATTRIBUTE_KEY_MIN_CANDLE_STICK_COUNT_FOR_ZOOMING_IN, DEFAULT_MIN_CANDLE_STICK_COUNT_FOR_ZOOMING_IN))
        }
    }


    private fun calculateXValueToMoveChartTo(highlightXValue: Float, candleStickCount: Int): Float {
        // Since simply calling moveViewTo(...) with a highlightXValue shifts the chart
        // to the specified x value and cuts half of the candle stick, we need to manually
        // adjust the x value to move the chart to. We do this by checking if the x value
        // of the highlight is not equal to an index of some of the latest candle sticks (because
        // moving the chart to of the latest candle sticks does not have a problem of cutting half of
        // the candle stick) and then subtracting some constant to shift the chart more to the left.
        return if((highlightXValue.toInt() <= (candleStickCount - mMinCandleStickCountForZoomingIn))) {
            (highlightXValue - CHART_TRANSLATION_COMPENSATION)
        } else {
            highlightXValue
        }
    }


    private fun calculateHorizontalZoomInChartScale(candleStickCount: Int): Float {
        // Since the candle stick count can differ greatly based on the interval,
        // we calculate the scale by dividing the total count of candle sticks
        // by the minimal candle stick count necessary for zooming in. By doing this,
        // we can guarantee that the chart will display roughly the same number of
        // candle sticks when zoomed in regardless of the interval.
        return (candleStickCount.toFloat() / mMinCandleStickCountForZoomingIn)
    }


    private fun shouldZoomInTheChart(candleStickCount: Int): Boolean {
        return ((mIsChartZoomInEnabled) &&
                !mWasChartZoomInPerformed &&
                (candleStickCount > mMinCandleStickCountForZoomingIn))
    }


    private fun getHighlight(candleSticks: List<CandleStick>, candleData: CandleData,
                             candleDataSet: CandleDataSet): Highlight {
        val createDefaultHighlight: () -> Highlight = {
            createHighlight(
                candleSticks.lastIndex.toFloat(),
                candleData.getIndexOfDataSet(candleDataSet)
            )
        }

        if(mChartHighlight == null) {
            return createDefaultHighlight()
        }

        val chartHighlight = mChartHighlight!!

        if(!chartHighlight.hasKey() ||
            !chartHighlight.hasBundle() ||
            (chartHighlight.key != CHART_HIGHLIGHT_DATA_KEY_CS)) {
            return createDefaultHighlight()
        }

        val chartHighlightBundle = chartHighlight.bundle!!

        // Adding a class loader to prevent ClassNotFoundException
        // when unmarshalling data from the parcel
        if(chartHighlightBundle.classLoader == null) {
            chartHighlightBundle.classLoader = CLASS_LOADER
        }

        // Fetching a boolean value denoting whether in the previous data set
        // was selected the last value
        val wasLastValueSelected = chartHighlightBundle.getBoolean(
            CHART_HIGHLIGHT_DATA_KEY_CS_PAYLOAD_KEY_ILVS,
            true
        )

        // If the last value was selected in the previous data set,
        // then we continue the trend and select the last value again
        if(wasLastValueSelected) {
            return createDefaultHighlight()
        }

        val candleStick = chartHighlightBundle.getParcelable<CandleStick>(
            CHART_HIGHLIGHT_DATA_KEY_CS_PAYLOAD_KEY_CS
        ) ?: return createDefaultHighlight()
        val candleStickIndex = chartHighlight.x.toInt()

        if(candleSticks.getOrNull(candleStickIndex)?.timestamp != candleStick.timestamp) {
            for(index in candleSticks.indices) {
                if(candleSticks[index].timestamp != candleStick.timestamp) {
                    continue
                }

                return createHighlight(
                    index.toFloat(),
                    candleData.getIndexOfDataSet(candleDataSet)
                )
            }

            return createDefaultHighlight()
        }

        return mChartHighlight!!.toHighlight()
    }


    private fun getPaintStyleForType(type: CandleStickType): Paint.Style {
        val candleStickStyle = when(type) {
            CandleStickType.BULLISH -> mBullishCandleStickStyle
            CandleStickType.BEARISH -> mBearishCandleStickStyle
        }

        return when(candleStickStyle) {
            CandleStickStyle.SOLID -> Paint.Style.FILL
            CandleStickStyle.HOLLOW -> Paint.Style.STROKE
        }
    }


    private fun getPriceChartIntervalForTabPosition(tabPosition: Int): PriceChartDataInterval {
        return PriceChartDataInterval.values()
            .find { it.ordinal == tabPosition}
            ?: throw IllegalStateException("Please provide a price chart data interval for a tabPosition: $tabPosition")
    }
    

    private fun enumerateCandleDataSets(callback: (CandleDataSet) -> Unit) {
        if(isDataEmpty()) {
            return
        }

        chart.data?.candleData?.dataSets?.forEach {
            if(it is CandleDataSet) {
                callback(it)
            }
        }
    }


    private fun enumerateBarDataSets(callback: (BarDataSet) -> Unit) {
        if(isDataEmpty()) {
            return
        }

        chart.data?.barData?.dataSets?.forEach {
            if(it is BarDataSet) {
                callback(it)
            }
        }
    }


    private fun createCandleDataSet(candleEntries: MutableList<CandleEntry>): CandleDataSet {
        return CandleDataSet(candleEntries, CHART_CANDLE_DATA_SET_LABEL).apply {
            setDrawIcons(false)
            setDrawValues(false)

            axisDependency = YAxis.AxisDependency.RIGHT

            shadowWidth = CHART_SHADOW_WIDTH

            decreasingPaintStyle = getPaintStyleForType(CandleStickType.BEARISH)
            decreasingColor = mChartNegativeColor

            increasingPaintStyle = getPaintStyleForType(CandleStickType.BULLISH)
            increasingColor = mChartPositiveColor

            isHighlightEnabled = true
            highlightLineWidth = CHART_HIGHLIGHTER_WIDTH

            neutralColor = mChartNeutralColor
            shadowColor = mChartCandleStickShadowColor
            highLightColor = mChartHighlighterColor
        }
    }


    private fun createVolumeDataSet(volumeEntries: MutableList<BarEntry>): BarDataSet {
        return BarDataSet(volumeEntries, CHART_VOLUME_DATA_SET_LABEL).apply {
            setDrawIcons(false)
            setDrawValues(false)

            axisDependency = YAxis.AxisDependency.LEFT
            isHighlightEnabled = false

            color = mChartVolumeBarColor
        }
    }


    override fun bindData() {
        if(isDataEmpty()) {
            return
        }

        val data = mData!!
        val candleStickCount = data.size

        val candleEntries: MutableList<CandleEntry> = mutableListOf()
        val volumeEntries: MutableList<BarEntry> = mutableListOf()
        var adjustedIndex: Float
        var candleStick: CandleStick

        for(i in 0 until candleStickCount) {
            adjustedIndex = i.toFloat()
            candleStick = data[i]

            candleEntries.add(CandleEntry(
                adjustedIndex,
                candleStick.highPrice.toFloat(),
                candleStick.lowPrice.toFloat(),
                candleStick.openPrice.toFloat(),
                candleStick.closePrice.toFloat()
            ))

            volumeEntries.add(BarEntry(adjustedIndex, candleStick.volume.toFloat()))
        }

        with(chart) {
            val candleDataSet = createCandleDataSet(candleEntries)
            val volumeDataSet = createVolumeDataSet(volumeEntries)

            val candleData = CandleData(candleDataSet)
            val volumeData = BarData(volumeDataSet)

            val combinedData = CombinedData().apply {
                setData(candleData)
                setData(volumeData)
            }

            // Setting the data of the chart
            this.data = combinedData

            with(axisLeft) {
                // Setting the minimum and maximum value of the axis
                // to "separate" bars from candle sticks
                axisMinimum = 0f
                axisMaximum = (volumeData.yMax * CHART_VOLUME_BARS_COMPENSATION)
            }

            with(xAxis) {
                axisMinimum = (candleData.xMin - CHART_HORIZONTAL_AXIS_COMPENSATION)
                axisMaximum = (candleData.xMax + CHART_HORIZONTAL_AXIS_COMPENSATION)
            }

            // Highlighting the last candle stick
            val highlight = getHighlight(data, candleData, candleDataSet).apply {
                dataIndex = combinedData.getDataIndex(candleData)
            }

            highlightValue(highlight, true)

            // Locking the view to the x value of the highlight
            moveViewToX(calculateXValueToMoveChartTo(highlight.x, candleStickCount))

            // Zooming if necessary
            if(shouldZoomInTheChart(candleStickCount)) {
                mWasChartZoomInPerformed = true

                val candleEntry = candleEntries[highlight.x.toInt()]

                // Resetting the zoom
                fitScreen()

                // Actual zooming is performed here
                zoom(
                    calculateHorizontalZoomInChartScale(candleStickCount),
                    CHART_ZOOM_IN_Y_SCALE,
                    candleEntry.x,
                    candleEntry.y
                )
            }
        }
    }


    override fun clearData() {
        super.clearData()

        mWasChartZoomInPerformed = false
    }


    override fun animateChart(duration: Int, easingFunction: Easing.EasingFunction) {
        chart.animateX(duration, easingFunction)
    }


    override fun setChartHighlighterColor(@ColorInt color: Int) {
        super.setChartHighlighterColor(color)

        enumerateCandleDataSets {
            it.highLightColor = color
        }
    }


    override fun setChartPositiveColor(color: Int) {
        super.setChartPositiveColor(color)

        enumerateCandleDataSets {
            it.increasingColor = color
        }
    }


    override fun setChartNegativeColor(color: Int) {
        super.setChartNegativeColor(color)

        enumerateCandleDataSets {
            it.decreasingColor = color
        }
    }


    override fun setChartNeutralColor(@ColorInt color: Int) {
        super.setChartNeutralColor(color)

        enumerateCandleDataSets {
            it.neutralColor = color
        }
    }


    fun setChartZoomInEnabled(isChartZoomInEnabled: Boolean) {
        mIsChartZoomInEnabled = isChartZoomInEnabled
    }


    fun setChartVolumeBarColor(@ColorInt color: Int) {
        mChartVolumeBarColor = color

        enumerateBarDataSets {
            it.color = color
        }
    }


    fun setChartCandleStickShadowColor(@ColorInt color: Int) {
        mChartCandleStickShadowColor = color

        enumerateCandleDataSets {
            it.shadowColor = color
        }
    }


    fun setMinCandleStickCountForZoomingIn(count: Int) {
        mMinCandleStickCountForZoomingIn = count
    }


    fun setCurrencyPairId(currencyPairId: Int) {
        mDataParameters = mDataParameters.copy(currencyPairId = currencyPairId)
    }


    fun setBullishCandleStickStyle(style: CandleStickStyle) {
        mBullishCandleStickStyle = style

        enumerateCandleDataSets {
            it.increasingPaintStyle = getPaintStyleForType(CandleStickType.BULLISH)
        }
    }


    fun setBearishCandleStickStyle(style: CandleStickStyle) {
        mBearishCandleStickStyle = style

        enumerateCandleDataSets {
            it.decreasingPaintStyle = getPaintStyleForType(CandleStickType.BEARISH)
        }
    }


    override fun isDataEmpty(): Boolean {
        return (mData?.isEmpty() ?: true)
    }


    override fun getLayoutResourceId(): Int = R.layout.price_chart_view_layout


    override fun getDefaultParameters(): PriceChartDataParameters {
        return PriceChartDataParameters.getDefaultParameters()
    }


    override fun getProgressBar(): ProgressBar = progressBar


    override fun getInfoView(): InfoView = infoView


    override fun getMainView(): View = chartContainerLl


    override fun getChart(): BarLineChartBase<*> = chart


    override fun getTabLayout(): TabLayout = tabLayout


    override fun getTabLayoutTabIndices(): List<Int> {
        return PriceChartDataInterval.values().map { it.ordinal }
    }


    private fun getChartInfoFieldsTitlesArray(): Array<String> {
        return mStringProvider.getStringArray(
            R.string.chart_candle_stick_info_open,
            R.string.chart_candle_stick_info_high,
            R.string.chart_candle_stick_info_low,
            R.string.chart_candle_stick_info_close,
            R.string.chart_candle_stick_info_volume,
            R.string.chart_candle_stick_info_time
        )
    }


    override fun getChartInfoFieldsArray(): Array<SimpleMapView> {
        return arrayOf(
            openSmv, highSmv,
            lowSmv, closeSmv,
            volumeSmv, timeSmv
        )
    }


    override fun onValueSelected(entry: Entry, highlight: Highlight) {
        super.onValueSelected(entry, highlight)

        if(entry !is CandleEntry) {
            return
        }

        val entryIndex = entry.x.toInt()
        val isLastValueSelected = (mData!!.lastIndex == entryIndex)
        val candleStick = mData!![entryIndex]

        mChartHighlight?.updateKeyAndBundle(
            key = CHART_HIGHLIGHT_DATA_KEY_CS,
            bundle = Bundle(CLASS_LOADER).apply {
                putBoolean(CHART_HIGHLIGHT_DATA_KEY_CS_PAYLOAD_KEY_ILVS, isLastValueSelected)
                putParcelable(CHART_HIGHLIGHT_DATA_KEY_CS_PAYLOAD_KEY_CS, candleStick)
            }
        )

        setChartInfoFieldsTextColor(when {
            (candleStick.openPrice < candleStick.closePrice) -> mChartPositiveColor
            (candleStick.openPrice > candleStick.closePrice) -> mChartNegativeColor

            else -> mChartNeutralColor
        })

        setChartInfoFieldsValues(
            mNumberFormatter.formatFixedPrice(candleStick.openPrice),
            mNumberFormatter.formatFixedPrice(candleStick.highPrice),
            mNumberFormatter.formatFixedPrice(candleStick.lowPrice),
            mNumberFormatter.formatFixedPrice(candleStick.closePrice),
            mNumberFormatter.formatAmount(candleStick.volume),
            mTimeFormatter.formatDate(candleStick.timestamp)
        )
    }


    override fun onTabSelected(tab: TabLayout.Tab) {
        super.onTabSelected(tab)

        val oldInterval = mDataParameters.interval
        val newInterval = getPriceChartIntervalForTabPosition(tab.position)

        mDataParameters = mDataParameters.copy(
            interval = newInterval,
            startTimestamp = PriceChartDataParameters.calculateStartTimestamp(newInterval),
            endTimestamp = PriceChartDataParameters.calculateEndTimestamp()
        )

        if(!mIsTabSelectedProgrammatically) {
            onChartIntervalChangeListener?.onIntervalChanged(oldInterval, newInterval)
        }
    }


    interface OnChartIntervalChangeListener {

        fun onIntervalChanged(oldInterval: PriceChartDataInterval,
                              newInterval: PriceChartDataInterval)

    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)

        if(state is SavedState) {
            setData(state.data, true)
        }
    }


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).also {
            onSaveInstanceState(it)
        }
    }


    override fun onSaveInstanceState(savedState: BaseTradingSavedState<PriceChartDataParameters>) {
        super.onSaveInstanceState(savedState)

        if(savedState is SavedState) {
            with(savedState) {
                data = mData
            }
        }
    }


    private class SavedState : BaseTradingChartState<PriceChartDataParameters> {

        companion object {

            private const val KEY_DATA = "data"


            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(parcel: Parcel): SavedState = SavedState(parcel)

                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)

            }

        }


        internal var data: List<CandleStick>? = null


        @Suppress("UNCHECKED_CAST")
        constructor(parcel: Parcel): super(parcel) {
            parcel.readBundle(CLASS_LOADER)?.run {
                data = (getSerializable(KEY_DATA) as? List<CandleStick>)
            }
        }


        constructor(superState: Parcelable?): super(superState)


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)

            parcel.writeBundle(Bundle(CLASS_LOADER).apply {
                putSerializable(KEY_DATA, (data as? Serializable))
            })
        }

    }


}