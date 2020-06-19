package com.stocksexchange.android.ui.transactioncreation.withdrawalcreation.views

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.view.children
import com.stocksexchange.android.R
import com.stocksexchange.android.model.WithdrawalInputView
import com.stocksexchange.android.ui.views.InputView
import com.stocksexchange.android.ui.views.formviews.BaseFormView
import com.stocksexchange.android.ui.views.mapviews.SimpleMapView
import com.stocksexchange.core.utils.extensions.appendColonCharacter
import com.stocksexchange.core.utils.extensions.getCompatDrawable
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible
import kotlinx.android.synthetic.main.withdrawal_creation_view_layout.view.*

/**
 * A view containing necessary fields to fill out in order to perform
 * a withdrawal operation.
 */
class WithdrawalCreationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseFormView<WithdrawalInputView>(context, attrs, defStyleAttr) {


    private var mSimpleMapViewsArray: Array<SimpleMapView> = arrayOf()




    override fun init() {
        super.init()

        initAddressInputView()
        initAmountInputView()
        initSimpleMapViews()
    }


    override fun initLabelViews() {
        super.initLabelViews()

        addressLabelTv.text = mStringProvider.getString(R.string.address)
        amountLabelTv.text = mStringProvider.getString(R.string.amount)
    }


    override fun initInputViews() {
        super.initInputViews()

        addressIv.setEtHintText(mStringProvider.getString(R.string.required))
        amountIv.setEtHintText(mStringProvider.getString(R.string.required))
    }


    private fun initAddressInputView() {
        with(addressIv) {
            setIconVisible(true)
            setIconDrawable(getCompatDrawable(R.drawable.ic_qr_scan))
        }
    }


    private fun initAmountInputView() {
        with(amountIv) {
            setLabelVisible(true)
            setLabelText(mStringProvider.getString(R.string.all))
        }
    }


    private fun initSimpleMapViews() {
        mSimpleMapViewsArray = arrayOf(minAmountSmv, feeSmv, finalAmountSmv)

        val minimumAmountStr = mStringProvider.getString(R.string.withdrawal_creation_view_minimum_amount)
        val feeStr = mStringProvider.getString(R.string.withdrawal_creation_view_fee)
        val finalAmountStr = mStringProvider.getString(R.string.withdrawal_creation_view_final_amount)

        minAmountSmv.setTitleText(minimumAmountStr.appendColonCharacter())
        feeSmv.setTitleText(feeStr.appendColonCharacter())
        finalAmountSmv.setTitleText(finalAmountStr.appendColonCharacter())
    }


    fun showExtraInputView() {
        extraLabelTv.makeVisible()
        extraIv.makeVisible()
    }


    fun hideExtraInputView() {
        extraLabelTv.makeGone()
        extraIv.makeGone()
    }


    override fun setInputViewExtraViewContainerBackgroundColor(@ColorInt color: Int) {
        addressIv.setExtraViewContainerBackgroundColor(color)
        amountIv.setExtraViewContainerBackgroundColor(color)
    }


    override fun setInputViewExtraViewContainerWidth(width: Int) {
        addressIv.setExtraViewContainerWidth(width)
        amountIv.setExtraViewContainerWidth(width)
    }


    fun setSimpleMapViewTitleColor(@ColorInt color: Int) {
        mSimpleMapViewsArray.forEach {
            it.setTitleColor(color)
        }
    }


    fun setSimpleMapViewValueColor(@ColorInt color: Int) {
        mSimpleMapViewsArray.forEach {
            it.setValueColor(color)
        }
    }


    fun setExtraLabelText(text: String) {
        extraLabelTv.text = text
    }


    fun setMinAmountValueText(text: String) {
        minAmountSmv.setValueText(text)
    }


    fun setFeeValueText(text: String) {
        feeSmv.setValueText(text)
    }


    fun setFinalAmountValueText(text: String) {
        finalAmountSmv.setValueText(text)
    }


    override fun getLayoutResourceId(): Int = R.layout.withdrawal_creation_view_layout


    override fun getInputViewForType(inputViewType: WithdrawalInputView): InputView {
        return when(inputViewType) {
            WithdrawalInputView.ADDRESS -> addressIv
            WithdrawalInputView.EXTRA -> extraIv
            WithdrawalInputView.AMOUNT -> amountIv
        }
    }


    override fun getLabelViewsArray(): Array<TextView> {
        return arrayOf(addressLabelTv, extraLabelTv, amountLabelTv)
    }


    override fun getInputViewsArray(): Array<InputView> {
        return arrayOf(addressIv, extraIv, amountIv)
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)

        if(state is SavedState) {
            for(child in children) {
                child.restoreHierarchyState(state.container)
            }
        }
    }


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).also {
            onSaveInstanceState(it)
        }
    }


    override fun onSaveInstanceState(savedState: BaseState) {
        super.onSaveInstanceState(savedState)

        if(savedState is SavedState) {
            for(child in children) {
                child.saveHierarchyState(savedState.container)
            }
        }
    }


    private class SavedState : BaseState {

        companion object {

            @JvmField
            var CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(parcel: Parcel): SavedState {
                    return SavedState(parcel)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }

            }

        }


        constructor(parcel: Parcel): super(parcel)

        constructor(superState: Parcelable?): super(superState)

    }


}