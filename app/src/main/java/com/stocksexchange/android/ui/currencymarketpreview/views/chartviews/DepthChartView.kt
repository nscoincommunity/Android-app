package com.stocksexchange.android.ui.currencymarketpreview.views.chartviews

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.withStyledAttributes
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.ChartHighlighter
import com.github.mikephil.charting.highlight.Highlight
import com.google.android.material.tabs.TabLayout
import com.stocksexchange.android.Constants.CLASS_LOADER
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.Orderbook
import com.stocksexchange.api.model.rest.OrderbookOrder
import com.stocksexchange.api.model.rest.parameters.OrderbookParameters
import com.stocksexchange.android.model.DepthChartLineStyle
import com.stocksexchange.android.model.DepthChartTab
import com.stocksexchange.android.model.OrderbookOrderType
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.ui.views.mapviews.SimpleMapView
import com.stocksexchange.android.ui.currencymarketpreview.views.base.BaseTradingChartView
import com.stocksexchange.core.utils.extensions.dpToPx
import com.stocksexchange.core.utils.extensions.getCompatDrawable
import kotlinx.android.synthetic.main.depth_chart_view_layout.view.*
import kotlin.math.abs

/**
 * A chart view that displays depth of an orderbook of a particular currency pair.
 */
class DepthChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseTradingChartView<Orderbook, OrderbookOrder, OrderbookParameters>(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_DEPTH_LEVEL = "depth_level"

        private const val CHART_HIGHLIGHT_DATA_KEY_SE = "spread_entry"
        private const val CHART_HIGHLIGHT_DATA_KEY_OO = "orderbook_order"

        private const val CHART_HIGHLIGHT_DATA_KEY_OO_PAYLOAD_KEY_OO = "orderbook_order"
        private const val CHART_HIGHLIGHT_DATA_KEY_OO_PAYLOAD_KEY_OOT = "orderbook_order_type"

        private const val DEFAULT_DEPTH_LEVEL = 15

        private const val SPREAD_ENTRY_VOLUME = 0f

        private const val CHART_DATA_SET_LINE_WIDTH = 1.5f

        private const val CHART_BUY_DATA_SET_LABEL = "BuyDataSet"
        private const val CHART_SELL_DATA_SET_LABEL = "SellDataSet"

    }


    private var mIsBidSpreadEntryAdded: Boolean = false
    private var mIsAskSpreadEntryAdded: Boolean = false

    private var mDepthLevel: Int = 0

    private var mPriceChartInfoString: String = ""
    private var mAmountChartInfoString: String = ""
    private var mVolumeChartInfoString: String = ""
    private var mHighestBidChartInfoString: String = ""
    private var mLowestAskChartInfoString: String = ""
    private var mSpreadChartInfoString: String = ""

    private var mLineStyle: DepthChartLineStyle = DepthChartLineStyle.LINEAR

    private var mTabs: List<DepthChartTab> = emptyList()

    private val mBuyOrderVolumes: MutableList<Double> = mutableListOf()
    private val mSellOrderVolumes: MutableList<Double> = mutableListOf()

    private var mBuyOrderbookOrders: List<OrderbookOrder> = emptyList()
    private var mSellOrderbookOrders: List<OrderbookOrder> = emptyList()

    var onTabSelectedListener: ((DepthChartTab) -> Unit)? = null




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.DepthChartView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_IS_CHART_ANIMATION_ENABLED, getBoolean(R.styleable.DepthChartView_isChartAnimationEnabled, DEFAULT_IS_CHART_ANIMATION_ENABLED))
                save(ATTRIBUTE_KEY_PROGRESS_BAR_COLOR, getColor(R.styleable.DepthChartView_progressBarColor, DEFAULT_PROGRESS_BAR_COLOR))
                save(ATTRIBUTE_KEY_INFO_VIEW_COLOR, getColor(R.styleable.DepthChartView_infoViewColor, DEFAULT_INFO_VIEW_COLOR))
                save(ATTRIBUTE_KEY_CHART_INFO_FIELDS_DEFAULT_TEXT_COLOR, getColor(R.styleable.DepthChartView_chartInfoFieldsDefaultTextColor, DEFAULT_CHART_INFO_FIELDS_TEXT_COLOR))
                save(ATTRIBUTE_KEY_CHART_AXIS_GRID_COLOR, getColor(R.styleable.DepthChartView_chartAxisGridColor, DEFAULT_CHART_AXIS_GRID_COLOR))
                save(ATTRIBUTE_KEY_CHART_HIGHLIGHTER_COLOR, getColor(R.styleable.DepthChartView_chartHighlighterColor, DEFAULT_CHART_HIGHLIGHTER_COLOR))
                save(ATTRIBUTE_KEY_CHART_POSITIVE_COLOR, getColor(R.styleable.DepthChartView_chartPositiveColor, DEFAULT_CHART_POSITIVE_COLOR))
                save(ATTRIBUTE_KEY_CHART_NEGATIVE_COLOR, getColor(R.styleable.DepthChartView_chartNegativeColor, DEFAULT_CHART_NEGATIVE_COLOR))
                save(ATTRIBUTE_KEY_CHART_NEUTRAL_COLOR, getColor(R.styleable.DepthChartView_chartNeutralColor, DEFAULT_CHART_NEUTRAL_COLOR))
                save(ATTRIBUTE_KEY_TAB_BACKGROUND_COLOR, getColor(R.styleable.DepthChartView_tabBackgroundColor, DEFAULT_TAB_BACKGROUND_COLOR))
                save(ATTRIBUTE_KEY_DEPTH_LEVEL, getInteger(R.styleable.DepthChartView_depthLevel, DEFAULT_DEPTH_LEVEL))
                save(ATTRIBUTE_KEY_CHART_HEIGHT, getDimensionPixelSize(R.styleable.DepthChartView_chartHeight, dpToPx(DEFAULT_CHART_HEIGHT_IN_DP)))
                save(ATTRIBUTE_KEY_MAIN_VIEW_TOP_PADDING, getDimensionPixelSize(R.styleable.DepthChartView_mainViewTopPadding, dpToPx(DEFAULT_MAIN_VIEW_TOP_PADDING_IN_DP)))
                save(ATTRIBUTE_KEY_TAB_BAR_TOP_PADDING, getDimensionPixelSize(R.styleable.DepthChartView_tabBarTopPadding, dpToPx(DEFAULT_TAB_BAR_TOP_PADDING)))
                save(ATTRIBUTE_KEY_TAB_BAR_BOTTOM_PADDING, getDimensionPixelSize(R.styleable.DepthChartView_tabBarBottomPadding, dpToPx(DEFAULT_TAB_BAR_BOTTOM_PADDING)))
                save(ATTRIBUTE_KEY_INFO_VIEW_ICON, (getDrawable(R.styleable.DepthChartView_infoViewIcon) ?: getCompatDrawable(getDefaultInfoViewIconResourceId())))
            }
        }
    }


    override fun init() {
        super.init()

        initStrings()
    }


    override fun initChart() {
        super.initChart()

        with(chart) {
            setHighlighter(mDepthChartHighlighter)
        }
    }


    private fun initStrings() {
        mPriceChartInfoString = mStringProvider.getString(R.string.depth_chart_info_price)
        mAmountChartInfoString = mStringProvider.getString(R.string.depth_chart_info_amount)
        mVolumeChartInfoString = mStringProvider.getString(R.string.depth_chart_info_volume)
        mHighestBidChartInfoString = mStringProvider.getString(R.string.depth_chart_info_highest_bid)
        mLowestAskChartInfoString = mStringProvider.getString(R.string.depth_chart_info_lowest_ask)
        mSpreadChartInfoString = mStringProvider.getString(R.string.depth_chart_info_spread)
    }


    override fun addTabLayoutTabs() {
        with(tabLayout) {
            for(tab in mTabs) {
                addTab(newTab().setText(tab.getTitle(mStringProvider)), tab.position)
            }
        }

    }


    override fun applyAttributes() {
        super.applyAttributes()

        with(mAttributes) {
            setDepthLevel(get(ATTRIBUTE_KEY_DEPTH_LEVEL, DEFAULT_DEPTH_LEVEL))
        }
    }


    private fun clearTabs() {
        with(tabLayout) {
            removeAllTabs()
            clearOnTabSelectedListeners()
        }
    }


    private fun createBuyDataSet(buyEntries: MutableList<Entry>): LineDataSet {
        return createLineDataSet(buyEntries, CHART_BUY_DATA_SET_LABEL).apply {
            color = mChartPositiveColor
            fillColor = mChartPositiveColor
            axisDependency = YAxis.AxisDependency.LEFT
        }
    }


    private fun createSellDataSet(sellEntries: MutableList<Entry>): LineDataSet {
        return createLineDataSet(sellEntries, CHART_SELL_DATA_SET_LABEL).apply {
            color = mChartNegativeColor
            fillColor = mChartNegativeColor
            axisDependency = YAxis.AxisDependency.RIGHT
        }
    }


    private fun createLineDataSet(entries: MutableList<Entry>, label: String): LineDataSet {
        return LineDataSet(entries, label).apply {
            setDrawValues(false)
            setDrawIcons(false)
            setDrawCircleHole(false)
            setDrawCircles(false)
            setDrawFilled(true)

            lineWidth = CHART_DATA_SET_LINE_WIDTH
            mode = getLineDataSetMode()

            isHighlightEnabled = true
            highlightLineWidth = CHART_HIGHLIGHTER_WIDTH
            highLightColor = mChartHighlighterColor
        }
    }


    private fun enumerateLineDataSets(callback: (LineDataSet) -> Unit) {
        if(isDataEmpty()) {
            return
        }

        chart.data?.dataSets?.forEach {
            if(it is LineDataSet) {
                callback(it)
            }
        }
    }


    private fun convertXValueToOrderbookOrderIndex(type: OrderbookOrderType,
                                                   x: Int): Int {
        return when(type) {
            OrderbookOrderType.BID -> (mBuyOrderbookOrders.lastIndex - x)
            OrderbookOrderType.ASK -> (x - mBuyOrderbookOrders.size - if(mIsAskSpreadEntryAdded) 1 else 0)
        }
    }


    private fun convertOrderbookOrderIndexToXValue(type: OrderbookOrderType,
                                                   index: Int): Float {
        return when(type) {
            OrderbookOrderType.BID -> (mBuyOrderbookOrders.lastIndex - index).toFloat()
            OrderbookOrderType.ASK -> (mBuyOrderbookOrders.size + index + if(mIsAskSpreadEntryAdded) 1 else 0).toFloat()
        }
    }


    private fun getSpreadEntryXValue(): Float {
        return mBuyOrderbookOrders.size.toFloat()
    }


    private fun getHorizontalAxisCompensation(itemCount: Int): Float {
        return if(mTabs.isEmpty()) {
            (itemCount / (DEFAULT_DEPTH_LEVEL.toFloat() * 2f))
        } else {
            (itemCount / (mTabs[0].level.toFloat() * 2f))
        }
    }


    private fun getLineDataSetMode(): LineDataSet.Mode {
        return when(mLineStyle) {
            DepthChartLineStyle.LINEAR -> LineDataSet.Mode.LINEAR
            DepthChartLineStyle.STEPPED -> LineDataSet.Mode.STEPPED
        }
    }


    private fun getLineDataSetByLabel(label: String): LineDataSet? {
        if(isDataEmpty()) {
            return null
        }

        return chart.data?.getDataSetByLabel(label, false)?.let {
            if(it is LineDataSet) {
                it
            } else {
                null
            }
        }
    }


    private fun getHighlight(orderbook: Orderbook, buyDataSet: LineDataSet,
                             sellDataSet: LineDataSet, chartData: LineData): Highlight? {
        val createDefaultHighlight: () -> Highlight? = {
            if(mIsBidSpreadEntryAdded || mIsAskSpreadEntryAdded) {
                createHighlight(
                    getSpreadEntryXValue(),
                    chartData.getIndexOfDataSet(if(mIsBidSpreadEntryAdded) {
                        buyDataSet
                    } else {
                        sellDataSet
                    })
                )
            } else {
                null
            }
        }

        if(mChartHighlight == null) {
            return createDefaultHighlight()
        }

        val chartHighlight = mChartHighlight!!
        val entryIndex = chartHighlight.x.toInt()
        val dataSet = chartData.getDataSetByIndex(chartHighlight.dataSetIndex)

        if((dataSet == null)) {
            return createDefaultHighlight()
        }

        if(!chartHighlight.hasKey()) {
            return createDefaultHighlight()
        }

        val dataKey = chartHighlight.key!!

        if((dataKey != CHART_HIGHLIGHT_DATA_KEY_SE) &&
            (dataKey != CHART_HIGHLIGHT_DATA_KEY_OO)) {
            return createDefaultHighlight()
        }

        if(dataKey == CHART_HIGHLIGHT_DATA_KEY_SE) {
            if((buyDataSet.values.getOrNull(entryIndex)?.y != SPREAD_ENTRY_VOLUME) &&
                (sellDataSet.values.getOrNull(entryIndex)?.y != SPREAD_ENTRY_VOLUME)) {
                return createDefaultHighlight()
            }
        } else {
            val order = chartHighlight.bundle?.run {
                // Adding a class loader to prevent ClassNotFoundException
                // when unmarshalling data from the parcel
                if(classLoader == null) {
                    classLoader = CLASS_LOADER
                }

                getParcelable(CHART_HIGHLIGHT_DATA_KEY_OO_PAYLOAD_KEY_OO) as? OrderbookOrder
            } ?: return createDefaultHighlight()
            val orderType = chartHighlight.bundle?.run {
                getSerializable(CHART_HIGHLIGHT_DATA_KEY_OO_PAYLOAD_KEY_OOT) as? OrderbookOrderType
            } ?: return createDefaultHighlight()
            val orderIndex = convertXValueToOrderbookOrderIndex(orderType, entryIndex)
            val orders = when(orderType) {
                OrderbookOrderType.BID -> orderbook.buyOrders
                OrderbookOrderType.ASK -> orderbook.sellOrders
            }

            if(orders.getOrNull(orderIndex)?.price != order.price) {
                var newOrderIndex: Int? = null
                var closestOrderPrice: Double? = null
                var tempPrice: Double
                var loopOrder: OrderbookOrder

                for(index in orders.indices) {
                    loopOrder = orders[index]

                    if(loopOrder.price != order.price) {
                        // Calculating the closest order by price to prevent
                        // resetting the highlight to the spread entry
                        tempPrice = abs(order.price - loopOrder.price)

                        if(closestOrderPrice == null || (tempPrice < closestOrderPrice)) {
                            newOrderIndex = index
                            closestOrderPrice = tempPrice
                        }
                    } else {
                        newOrderIndex = index
                        break
                    }
                }

                return if(newOrderIndex != null) {
                    createHighlight(
                        convertOrderbookOrderIndexToXValue(orderType, newOrderIndex),
                        chartData.getIndexOfDataSet(when(orderType) {
                            OrderbookOrderType.BID -> buyDataSet
                            OrderbookOrderType.ASK -> sellDataSet
                        })
                    )
                } else {
                    createDefaultHighlight()
                }
            }
        }

        return chartHighlight.toHighlight()
    }


    private fun getOrderbookDataChartInfoColorsArray(type: OrderbookOrderType): IntArray {
        val color = when(type) {
            OrderbookOrderType.BID -> mChartPositiveColor
            OrderbookOrderType.ASK -> mChartNegativeColor
        }

        return intArrayOf(color, color, color)
    }


    private fun getOrderbookDataChartInfoTitlesArray(): Array<String> {
        return arrayOf(
            mPriceChartInfoString,
            mAmountChartInfoString,
            mVolumeChartInfoString
        )
    }


    private fun getOrderbookDataChartInfoValuesArray(price: Double, amount: Double,
                                                     volume: Double): Array<String> {
        return arrayOf(
            mNumberFormatter.formatFixedPrice(price),
            mNumberFormatter.formatAmount(amount),
            mNumberFormatter.formatAmount(volume)
        )
    }


    private fun getSpreadChartInfoColorsArray(): IntArray {
        return intArrayOf(
            mChartPositiveColor,
            mChartNegativeColor,
            mChartNeutralColor
        )
    }


    private fun getSpreadChartInfoTitlesArray(): Array<String> {
        return arrayOf(
            mHighestBidChartInfoString,
            mLowestAskChartInfoString,
            mSpreadChartInfoString
        )
    }


    private fun getSpreadChartInfoValuesArray(highestBidOrderPrice: Double?,
                                              lowestAskOrderPrice: Double?): Array<String> {
        return arrayOf(
            if(highestBidOrderPrice == null) {
                DEFAULT_CHART_INFO_FIELD_VALUE
            } else {
                mNumberFormatter.formatFixedPrice(highestBidOrderPrice)
            },
            if(lowestAskOrderPrice == null) {
                DEFAULT_CHART_INFO_FIELD_VALUE
            } else {
                mNumberFormatter.formatFixedPrice(lowestAskOrderPrice)
            },
            if((highestBidOrderPrice == null) || (lowestAskOrderPrice == null)) {
                DEFAULT_CHART_INFO_FIELD_VALUE
            } else {
                val spread = (lowestAskOrderPrice - highestBidOrderPrice)
                val percentSpread = ((spread / lowestAskOrderPrice) * 100.0)

                "${mNumberFormatter.formatFixedPrice(spread)} (${mNumberFormatter.formatPercentSpread(percentSpread)})"
            }
        )
    }


    private fun getDefaultChartInfoValuesArray(): Array<String> {
        return arrayOf(
            DEFAULT_CHART_INFO_FIELD_VALUE,
            DEFAULT_CHART_INFO_FIELD_VALUE,
            DEFAULT_CHART_INFO_FIELD_VALUE
        )
    }


    override fun truncateData(data: Orderbook?): Orderbook? {
        return data?.truncate(mDepthLevel, mDepthLevel)
    }


    override fun bindData() {
        if(isDataEmpty()) {
            return
        }

        val data = mData!!

        var buyEntries: MutableList<Entry> = mutableListOf()
        val sellEntries: MutableList<Entry> = mutableListOf()

        mIsBidSpreadEntryAdded = false
        mIsAskSpreadEntryAdded = false

        mBuyOrderVolumes.clear()
        mSellOrderVolumes.clear()

        mBuyOrderbookOrders = data.buyOrders
        mSellOrderbookOrders = data.sellOrders

        var buyOrderVolume = 0.0
        var sellOrderVolume = 0.0

        // Spread entry for bids
        if(mBuyOrderbookOrders.isNotEmpty()) {
            buyEntries.add(Entry(
                getSpreadEntryXValue(),
                SPREAD_ENTRY_VOLUME,
                OrderbookOrderType.BID
            ))

            mIsBidSpreadEntryAdded = true
        }

        for(i in 0 until mBuyOrderbookOrders.size) {
            buyOrderVolume += (mBuyOrderbookOrders.getOrNull(i)?.amount ?: 0.0)
            mBuyOrderVolumes.add(i, buyOrderVolume)

            buyEntries.add(Entry(
                convertOrderbookOrderIndexToXValue(OrderbookOrderType.BID, i),
                mBuyOrderVolumes[i].toFloat(),
                OrderbookOrderType.BID
            ))
        }

        buyEntries = buyEntries.reversed().toMutableList()

        // Spread entry for asks
        if(mSellOrderbookOrders.isNotEmpty()) {
            sellEntries.add(Entry(
                getSpreadEntryXValue(),
                SPREAD_ENTRY_VOLUME,
                OrderbookOrderType.ASK
            ))

            mIsAskSpreadEntryAdded = true
        }

        for(i in 0 until mSellOrderbookOrders.size) {
            sellOrderVolume += (mSellOrderbookOrders.getOrNull(i)?.amount ?: 0.0)
            mSellOrderVolumes.add(i, sellOrderVolume)

            sellEntries.add(Entry(
                convertOrderbookOrderIndexToXValue(OrderbookOrderType.ASK, i),
                mSellOrderVolumes[i].toFloat(),
                OrderbookOrderType.ASK
            ))
        }

        with(chart) {
            val buyDataSet = createBuyDataSet(buyEntries)
            val sellDataSet = createSellDataSet(sellEntries)
            val chartData = LineData(buyDataSet, sellDataSet)

            // Setting the data of the chart
            this.data = chartData

            with(xAxis) {
                val compensation = getHorizontalAxisCompensation(
                    mBuyOrderbookOrders.size + mSellOrderbookOrders.size
                )

                axisMinimum = (chartData.xMin - compensation)
                axisMaximum = (chartData.xMax + compensation)
            }

            // Highlighting the value based on the market state
            val highlight = getHighlight(data, buyDataSet, sellDataSet, chartData)

            if(highlight != null) {
                highlightValue(highlight, true)
            } else {
                updateChartInfoFields(
                    getSpreadChartInfoTitlesArray(),
                    getDefaultChartInfoValuesArray()
                )
            }
        }
    }


    override fun clearData() {
        super.clearData()

        mIsBidSpreadEntryAdded = false
        mIsAskSpreadEntryAdded = false

        mBuyOrderVolumes.clear()
        mSellOrderVolumes.clear()

        mBuyOrderbookOrders = emptyList()
        mSellOrderbookOrders = emptyList()
    }


    override fun animateChart(duration: Int, easingFunction: Easing.EasingFunction) {
        chart.animateY(duration, easingFunction)
    }


    /**
     * Sets a depth level of the chart. The level denotes
     * the number of orders taken from both the buy and sell
     * sides.
     *
     * @param depthLevel The depth level to set
     */
    fun setDepthLevel(depthLevel: Int) {
        mDepthLevel = depthLevel
        mTabs = DepthChartTab.getDepthChartTabsForDepthLevel(depthLevel)

        clearTabs()
        initTabLayout()
    }


    fun setLineStyle(style: DepthChartLineStyle) {
        mLineStyle = style
    }


    override fun setChartHighlighterColor(color: Int) {
        super.setChartHighlighterColor(color)

        enumerateLineDataSets {
            it.highLightColor = color
        }
    }


    override fun setChartPositiveColor(color: Int) {
        super.setChartPositiveColor(color)

        getLineDataSetByLabel(CHART_BUY_DATA_SET_LABEL)?.fillColor = color
    }


    override fun setChartNegativeColor(color: Int) {
        super.setChartNegativeColor(color)

        getLineDataSetByLabel(CHART_SELL_DATA_SET_LABEL)?.fillColor = color
    }


    fun setCurrencyPairId(currencyPairId: Int) {
        mDataParameters = mDataParameters.copy(currencyPairId = currencyPairId)
    }


    override fun isDataEmpty(): Boolean {
        return (mData?.isEmpty ?: true)
    }


    override fun shouldInitTabs(): Boolean = mTabs.isNotEmpty()


    override fun getLayoutResourceId(): Int = R.layout.depth_chart_view_layout


    override fun getDefaultParameters(): OrderbookParameters {
        return OrderbookParameters.getDefaultParameters()
    }


    override fun getProgressBar(): ProgressBar = progressBar


    override fun getInfoView(): InfoView = infoView


    override fun getMainView(): View = chartContainerLl


    override fun getChart(): BarLineChartBase<*> = chart


    override fun getTabLayout(): TabLayout = tabLayout


    override fun getTabLayoutTabIndices(): List<Int> {
        val indices: MutableList<Int> = mutableListOf()

        mTabs.forEach {
            indices.add(it.position)
        }

        return indices
    }


    override fun getChartInfoFieldsArray(): Array<SimpleMapView> {
        return arrayOf(firstSmv, secondSmv, thirdSmv)
    }


    override fun onValueSelected(entry: Entry, highlight: Highlight) {
        super.onValueSelected(entry, highlight)

        val entryIndex = entry.x.toInt()

        val colors: IntArray
        val titles: Array<String>
        val values: Array<String>

        // Checking for spread entries
        if((entry.y == SPREAD_ENTRY_VOLUME) && (mIsBidSpreadEntryAdded || mIsAskSpreadEntryAdded)) {
            val highestBidOrder = if(mBuyOrderbookOrders.isEmpty()) {
                null
            } else {
                mBuyOrderbookOrders.first()
            }
            val lowestAskOrder = if(mSellOrderbookOrders.isEmpty()) {
                null
            } else {
                mSellOrderbookOrders.first()
            }

            mChartHighlight?.key = CHART_HIGHLIGHT_DATA_KEY_SE

            colors = getSpreadChartInfoColorsArray()
            titles = getSpreadChartInfoTitlesArray()
            values = getSpreadChartInfoValuesArray(
                highestBidOrder?.price,
                lowestAskOrder?.price
            )
        } else {
            val orderbookOrderType = (entry.data as OrderbookOrderType)
            val orderbookOrderIndex = convertXValueToOrderbookOrderIndex(
                orderbookOrderType,
                entryIndex
            )

            val orderbookOrderVolume: Double
            val orderbookOrder: OrderbookOrder

            when(orderbookOrderType) {

                OrderbookOrderType.BID -> {
                    orderbookOrder = mBuyOrderbookOrders[orderbookOrderIndex]
                    orderbookOrderVolume = mBuyOrderVolumes[orderbookOrderIndex]
                }

                OrderbookOrderType.ASK -> {
                    orderbookOrder = mSellOrderbookOrders[orderbookOrderIndex]
                    orderbookOrderVolume = mSellOrderVolumes[orderbookOrderIndex]
                }

            }

            mChartHighlight?.updateKeyAndBundle(
                key = CHART_HIGHLIGHT_DATA_KEY_OO,
                bundle = Bundle(CLASS_LOADER).apply {
                    putParcelable(CHART_HIGHLIGHT_DATA_KEY_OO_PAYLOAD_KEY_OO, orderbookOrder)
                    putSerializable(CHART_HIGHLIGHT_DATA_KEY_OO_PAYLOAD_KEY_OOT, orderbookOrderType)
                }
            )

            colors = getOrderbookDataChartInfoColorsArray(orderbookOrderType)
            titles = getOrderbookDataChartInfoTitlesArray()
            values = getOrderbookDataChartInfoValuesArray(
                orderbookOrder.price,
                orderbookOrder.amount,
                orderbookOrderVolume
            )
        }

        setChartInfoFieldsColors(*colors)
        updateChartInfoFields(titles, values)
    }


    override fun onTabSelected(tab: TabLayout.Tab) {
        super.onTabSelected(tab)

        val newTab = mTabs[mSelectedTabPosition]

        mDepthLevel = newTab.level

        if(!mIsTabSelectedProgrammatically) {
            onTabSelectedListener?.invoke(newTab)
        }
    }


    private val mDepthChartHighlighter = object : ChartHighlighter<LineChart>(chart) {

        override fun getHighlightForX(xVal: Float, x: Float, y: Float): Highlight? {
            val closestValues = getHighlightsAtXValue(xVal, x, y)

            if(closestValues.isEmpty()) {
                return null
            }

            val leftAxisMinDist = getMinimumDistance(closestValues, x, YAxis.AxisDependency.LEFT)
            val rightAxisMinDist = getMinimumDistance(closestValues, x, YAxis.AxisDependency.RIGHT)

            val axis = if(leftAxisMinDist < rightAxisMinDist) {
                YAxis.AxisDependency.LEFT
            } else {
                YAxis.AxisDependency.RIGHT
            }

            return getClosestHighlightByPixel(closestValues, x, y, axis, mChart.maxHighlightDistance)
        }


        override fun getHighlightPos(highlight: Highlight): Float {
            return highlight.xPx
        }


        override fun getDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
            return abs(x1 - x2)
        }

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


    override fun onSaveInstanceState(savedState: BaseTradingSavedState<OrderbookParameters>) {
        super.onSaveInstanceState(savedState)

        if(savedState is SavedState) {
            with(savedState) {
                data = mData
            }
        }
    }


    private class SavedState : BaseTradingChartState<OrderbookParameters> {

        companion object {

            private const val KEY_DATA = "data"


            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(parcel: Parcel): SavedState = SavedState(parcel)

                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)

            }

        }


        internal var data: Orderbook? = null


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