package com.stocksexchange.android.ui.views.dialogs

import android.content.Context
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.dialogs.base.BaseMaterialDialog
import com.stocksexchange.android.ui.views.mapviews.SimpleMapView

/**
 * A dialog used for showing device specific metrics.
 */
class DeviceMetricsDialog(context: Context) : BaseMaterialDialog(context) {


    private lateinit var mDeviceNameSmv: SimpleMapView
    private lateinit var mNetworkGenerationSmv: SimpleMapView
    private lateinit var mDensitySmv: SimpleMapView
    private lateinit var mDensityInDpSmv: SimpleMapView
    private lateinit var mScreenSizeSmv: SimpleMapView
    private lateinit var mScreenWidthInPxSmv: SimpleMapView
    private lateinit var mScreenWidthInDpSmv: SimpleMapView
    private lateinit var mScreenHeightInPxSmv: SimpleMapView
    private lateinit var mScreenHeightInDpSmv: SimpleMapView
    private lateinit var mSmallestWidthInDpSmv: SimpleMapView
    
    private lateinit var mContentContainerLl: LinearLayout

    private lateinit var mSimpleMapViewsArray: Array<SimpleMapView>




    override fun initViews() {
        initSimpleMapViews()
        initContentContainer()
        initSimpleMapViewsArray()
    }


    private fun initSimpleMapViews() {
        mDeviceNameSmv = findViewById(R.id.deviceNameSmv)
        mNetworkGenerationSmv = findViewById(R.id.networkGenerationSmv)
        mDensitySmv = findViewById(R.id.densitySmv)
        mDensityInDpSmv = findViewById(R.id.densityInDpSmv)
        mScreenSizeSmv = findViewById(R.id.screenSizeSmv)
        mScreenWidthInPxSmv = findViewById(R.id.screenWidthInPxSmv)
        mScreenWidthInDpSmv = findViewById(R.id.screenWidthInDpSmv)
        mScreenHeightInPxSmv = findViewById(R.id.screenHeightInPxSmv)
        mScreenHeightInDpSmv = findViewById(R.id.screenHeightInDpSmv)
        mSmallestWidthInDpSmv = findViewById(R.id.smallestWidthInDpSmv)
    }


    private fun initContentContainer() {
        mContentContainerLl = findViewById(R.id.contentContainerLl)
    }


    private fun initSimpleMapViewsArray() {
        mSimpleMapViewsArray = arrayOf(
            mDeviceNameSmv, mNetworkGenerationSmv,
            mDensitySmv, mDensityInDpSmv,
            mScreenSizeSmv, mScreenWidthInPxSmv,
            mScreenWidthInDpSmv, mScreenHeightInPxSmv,
            mScreenHeightInDpSmv, mSmallestWidthInDpSmv
        )
    }


    fun setDeviceName(deviceName: String) {
        mDeviceNameSmv.setValueText(deviceName)
    }


    fun setNetworkGeneration(generation: String) {
        mNetworkGenerationSmv.setValueText(generation)
    }


    fun setDensity(density: Float) {
        mDensitySmv.setValueText(density.toString())
    }


    fun setDensityInDp(densityInDp: Int) {
        mDensityInDpSmv.setValueText(densityInDp.toString())
    }


    fun setScreenSize(screenSize: String) {
        mScreenSizeSmv.setValueText(screenSize)
    }


    fun setScreenWidthInPx(widthInPx: Int) {
        mScreenWidthInPxSmv.setValueText(widthInPx.toString())
    }


    fun setScreenWidthInDp(widthInDp: Float) {
        mScreenWidthInDpSmv.setValueText(widthInDp.toString())
    }


    fun setScreenHeightInPx(heightInPx: Int) {
        mScreenHeightInPxSmv.setValueText(heightInPx.toString())
    }


    fun setScreenHeightInDp(heightInDp: Float) {
        mScreenHeightInDpSmv.setValueText(heightInDp.toString())
    }


    fun setSmallestWidthInDp(smallestWidthInDp: Int) {
        mSmallestWidthInDpSmv.setValueText(smallestWidthInDp.toString())
    }


    fun setBackgroundColor(@ColorInt color: Int) {
        mContentContainerLl.setBackgroundColor(color)
    }


    fun setTitlesColor(@ColorInt color: Int) {
        mSimpleMapViewsArray.forEach {
            it.setTitleColor(color)
        }
    }

    
    fun setValuesColor(@ColorInt color: Int) {
        mSimpleMapViewsArray.forEach {
            it.setValueColor(color)
        }
    }


    override fun getLayoutResourceId(): Int = R.layout.device_metrics_dialog_layout


}