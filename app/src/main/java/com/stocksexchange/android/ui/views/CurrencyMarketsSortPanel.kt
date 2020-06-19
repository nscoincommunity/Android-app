package com.stocksexchange.android.ui.views

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.stocksexchange.android.Constants.CLASS_LOADER
import com.stocksexchange.android.R
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Column.*
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Companion.DAILY_PRICE_CHANGE_ASCENDING_COMPARATOR
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Companion.DEFAULT
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Companion.LAST_PRICE_ASCENDING_COMPARATOR
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Companion.VOLUME_ASCENDING_COMPARATOR
import com.stocksexchange.android.model.comparators.CurrencyMarketComparator.Companion.NAME_ASCENDING_COMPARATOR
import com.stocksexchange.android.ui.views.base.containers.BaseConstraintLayoutView
import com.stocksexchange.core.utils.extensions.fetchProperState
import com.stocksexchange.core.utils.extensions.getParcelableOrThrow
import kotlinx.android.synthetic.main.currency_markets_sort_panel_layout.view.*

/**
 * A container view that holds functionality for sorting
 * currency markets.
 */
class CurrencyMarketsSortPanel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseConstraintLayoutView(context, attrs, defStyleAttr) {


    private var mSelectedTitleTv: SortPanelTextView? = null

    private var mTitlesArray: Array<SortPanelTextView> = arrayOf()

    private val mOnTitleClickListener: ((View) -> Unit) = { view ->
        if(mSelectedTitleTv?.id == view.id) {
            mSelectedTitleTv?.toggleComparator()
        } else {
            val newSelectedTitleTv = (view as SortPanelTextView)

            mSelectedTitleTv?.isSelected = false
            newSelectedTitleTv.isSelected = true

            mSelectedTitleTv = newSelectedTitleTv
        }

        onSortPanelTitleClickListener?.invoke(mSelectedTitleTv!!)
    }


    var onSortPanelTitleClickListener: ((SortPanelTextView) -> Unit)? = null




    override fun init() {
        initNameTitleTv()
        initVolumeTitleTv()
        initLastPriceTitleTv()
        initDailyPriceChangeTitleTv()
        initTitlesArray()
    }


    private fun initNameTitleTv() {
        initTitleTv(
            nameTitleTv,
            mStringProvider.getString(R.string.currency_markets_sort_panel_name_title),
            NAME_ASCENDING_COMPARATOR
        )
    }


    private fun initVolumeTitleTv() {
        initTitleTv(
            volumeTitleTv,
            mStringProvider.getString(R.string.currency_markets_sort_panel_volume_title),
            VOLUME_ASCENDING_COMPARATOR
        )
    }


    private fun initLastPriceTitleTv() {
        initTitleTv(
            lastPriceTitleTv,
            mStringProvider.getString(R.string.currency_markets_sort_panel_last_price_title),
            LAST_PRICE_ASCENDING_COMPARATOR
        )
    }


    private fun initDailyPriceChangeTitleTv() {
        initTitleTv(
            dailyPriceChangeTitleTv,
            mStringProvider.getString(R.string.currency_markets_sort_panel_daily_price_change_title),
            DAILY_PRICE_CHANGE_ASCENDING_COMPARATOR
        )
    }


    private fun initTitleTv(titleTv: SortPanelTextView, text: String,
                            comparator: CurrencyMarketComparator) = with(titleTv) {
        this.text = text
        this.comparator = comparator
        this.setOnClickListener(mOnTitleClickListener)
    }


    private fun initTitlesArray() {
        mTitlesArray = arrayOf(
            nameTitleTv,
            volumeTitleTv,
            lastPriceTitleTv,
            dailyPriceChangeTitleTv
        )
    }


    fun initComparator() {
        setComparator(DEFAULT)
    }


    fun setSelectedTitleTextColor(@ColorInt color: Int) {
        mTitlesArray.forEach {
            it.setSelectedTitleTextColor(color)
        }
    }


    fun setUnselectedTitleTextColor(@ColorInt color: Int) {
        mTitlesArray.forEach {
            it.setUnselectedTitleTextColor(color)
        }
    }


    fun setArrowDrawableColor(@ColorInt color: Int) {
        mTitlesArray.forEach {
            it.setArrowDrawableColor(color)
        }
    }


    fun setSeparatorColor(@ColorInt color: Int) {
        separatorTv.setTextColor(color)
    }


    /**
     * Sets a comparator and selects according TextView on the sort panel.
     *
     * @param comparator The comparator to set
     */
    fun setComparator(comparator: CurrencyMarketComparator) {
        if(mSelectedTitleTv?.comparator?.id == comparator.id) {
            return
        }

        getTitleTvForComparator(comparator).also {
            it.comparator = comparator

            mOnTitleClickListener.invoke(it)
        }
    }


    override fun getLayoutResourceId(): Int = R.layout.currency_markets_sort_panel_layout


    private fun getComparator(): CurrencyMarketComparator {
        return (mSelectedTitleTv?.comparator ?: DEFAULT)
    }


    private fun getTitleTvForComparator(comparator: CurrencyMarketComparator): SortPanelTextView {
        return when(comparator.column) {
            NAME -> nameTitleTv
            VOLUME -> volumeTitleTv
            LAST_PRICE -> lastPriceTitleTv
            DAILY_PRICE_CHANGE -> dailyPriceChangeTitleTv
        }
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state.fetchProperState())

        if(state is SavedState) {
            setComparator(state.comparator)
        }
    }


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).apply {
            comparator = getComparator()
        }
    }


    private class SavedState : BaseSavedState {

        companion object {

            private const val KEY_COMPARATOR = "comparator"


            @JvmField
            var CREATOR = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(parcel: Parcel): SavedState = SavedState(parcel)

                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)

            }

        }


        internal var comparator: CurrencyMarketComparator = DEFAULT


        constructor(parcel: Parcel): super(parcel) {
            parcel.readBundle(CLASS_LOADER)?.run {
                comparator = getParcelableOrThrow(KEY_COMPARATOR)
            }
        }


        constructor(superState: Parcelable?): super(superState)


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)

            parcel.writeBundle(Bundle(CLASS_LOADER).apply {
                putParcelable(KEY_COMPARATOR, comparator)
            })
        }

    }


}