package com.stocksexchange.android.ui.transactioncreation.depositcreation.views

import android.content.Context
import android.graphics.Bitmap
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.text.toSpannable
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.base.containers.BaseLinearLayoutView
import com.stocksexchange.android.ui.views.mapviews.SimpleMapView
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible
import com.stocksexchange.core.utils.extensions.set
import com.stocksexchange.core.utils.text.CustomLinkMovementMethod
import com.stocksexchange.core.utils.text.SelectorSpan
import kotlinx.android.synthetic.main.deposit_creation_view_layout.view.*

/**
 * A view that acts as a container for information about
 * creating a particular deposit.
 */
class DepositCreationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseLinearLayoutView(context, attrs, defStyleAttr) {


    private var mParameterValueTextColor: Int = 0
    private var mParameterValuePressedBackgroundColor: Int = 0

    private var mSimpleMapViewsArray: Array<SimpleMapView> = arrayOf()

    lateinit var onAddressParamValueClickListener: ((String) -> Unit)
    lateinit var onExtraParamValueClickListener: ((String) -> Unit)




    override fun init() {
        super.init()

        orientation = VERTICAL

        initAddressParamValueTv()
        initExtraParamValueTv()
        initSimpleMapViews()
    }


    private fun initSimpleMapViews() {
        initDepositFeeSmv()
        initMinimumAmountSmv()

        mSimpleMapViewsArray = arrayOf(depositFeeSmv, minimumAmountSmv)
    }


    private fun initAddressParamValueTv() {
        addressParamValueTv.movementMethod = CustomLinkMovementMethod()
    }


    private fun initExtraParamValueTv() {
        extraParamValueTv.movementMethod = CustomLinkMovementMethod()
    }


    private fun initDepositFeeSmv() {
        depositFeeSmv.setTitleText(mStringProvider.getString(
            R.string.deposit_creation_fragment_deposit_fee_smv_title
        ))
    }


    private fun initMinimumAmountSmv() {
        minimumAmountSmv.setTitleText(mStringProvider.getString(
            R.string.deposit_creation_fragment_minimum_amount_smv_title
        ))
    }


    fun showExtraParamViews() {
        extraParamTitleTv.makeVisible()
        extraParamValueTv.makeVisible()
    }


    fun hideExtraParamViews() {
        extraParamTitleTv.makeGone()
        extraParamValueTv.makeGone()
    }


    fun setParameterValueTextColor(@ColorInt color: Int) {
        mParameterValueTextColor = color
    }


    fun setParameterValuePressedBackgroundColor(@ColorInt color: Int) {
        mParameterValuePressedBackgroundColor = color
    }


    fun setExtraParamTitleColor(@ColorInt color: Int) {
        extraParamTitleTv.setTextColor(color)
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


    fun setAddressParamValue(address: String) {
        addressParamValueTv.text = wrapParameterValueIntoSpannable(
            value = address,
            onClicked = onAddressParamValueClickListener
        )
    }


    fun setExtraParamTitle(title: String) {
        extraParamTitleTv.text = title
    }


    fun setExtraParamValue(value: String) {
        extraParamValueTv.text = wrapParameterValueIntoSpannable(
            value = value,
            onClicked = onExtraParamValueClickListener
        )
    }


    fun setDepositFeeValue(value: String) {
        depositFeeSmv.setValueText(value)
    }


    fun setMinimumAmountValue(value: String) {
        minimumAmountSmv.setValueText(value)
    }


    fun setAddressParamQrCodeImage(bitmap: Bitmap) {
        addressParamQrCodeIv.setImageBitmap(bitmap)
    }


    private fun wrapParameterValueIntoSpannable(value: String,
                                                onClicked: ((String) -> Unit)): Spannable {
        return value.toSpannable().apply {
            val selectorSpan = object : SelectorSpan(
                mParameterValueTextColor,
                mParameterValuePressedBackgroundColor
            ) {

                override fun onClick(widget: View) = onClicked(value)

            }
            val underlineSpan = UnderlineSpan()

            val startIndex = 0
            val endIndex = value.length
            val spans = arrayOf(selectorSpan, underlineSpan)

            for(span in spans) {
                this[startIndex, endIndex] = span
            }
        }
    }


    override fun getLayoutResourceId(): Int = R.layout.deposit_creation_view_layout


}