package com.stocksexchange.android.ui.views.formviews

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.TextWatcher
import android.text.method.KeyListener
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import com.stocksexchange.android.Constants
import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.ui.views.InputView
import com.stocksexchange.android.ui.views.base.containers.BaseLinearLayoutView
import com.stocksexchange.core.utils.extensions.fetchProperState

/**
 * A base view class that contains common functionality
 * for all types of forms.
 */
abstract class BaseFormView<IVT : Enum<*>> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseLinearLayoutView(context, attrs, defStyleAttr) {


    protected var mLabelViewsArray: Array<TextView> = arrayOf()

    protected var mInputViewsArray: Array<InputView> = arrayOf()




    override fun init() {
        super.init()

        orientation = VERTICAL

        initLabelViews()
        initInputViews()
    }


    @CallSuper
    protected open fun initLabelViews() {
        mLabelViewsArray = getLabelViewsArray()
    }


    @CallSuper
    protected open fun initInputViews() {
        mInputViewsArray = getInputViewsArray()
    }


    @CallSuper
    open fun enableInputViewsEditing() {
        mInputViewsArray.forEach {
            it.enableEditing()
        }
    }


    @CallSuper
    open fun disableInputViewsEditing() {
        mInputViewsArray.forEach {
            it.disableEditing()
        }
    }


    @CallSuper
    open fun clearInputViewText(inputViewType: IVT, shouldNotifyListeners: Boolean) {
        getInputViewForType(inputViewType).clearEtText(shouldNotifyListeners)
    }


    open fun setLabelTextColor(@ColorInt color: Int) {
        mLabelViewsArray.forEach {
            it.setTextColor(color)
        }
    }


    open fun setInputViewTextColor(@ColorInt color: Int) {
        mInputViewsArray.forEach {
            it.setEtTextColor(color)
        }
    }


    open fun setInputViewHintTextColor(@ColorInt color: Int) {
        mInputViewsArray.forEach {
            it.setEtHintTextColor(color)
        }
    }


    open fun setInputViewCursorColor(@ColorInt color: Int) {
        mInputViewsArray.forEach {
            it.setEtCursorColor(color)
        }
    }


    open fun setInputViewIconColor(@ColorInt color: Int) {
        mInputViewsArray.forEach {
            it.setIconColor(color)
        }
    }


    open fun setInputViewLabelTextColor(@ColorInt color: Int) {
        mInputViewsArray.forEach {
            it.setLabelTextColor(color)
        }
    }


    open fun setInputViewExtraViewContainerBackgroundColor(@ColorInt color: Int) {
        mInputViewsArray.forEach {
            it.setExtraViewContainerBackgroundColor(color)
        }
    }


    open fun setInputViewExtraViewContainerWidth(width: Int) {
        mInputViewsArray.forEach {
            it.setExtraViewContainerWidth(width)
        }
    }


    @CallSuper
    open fun setInputViewType(inputViewType: IVT, type: Int,
                              shouldNotifyListeners: Boolean = true) {
        getInputViewForType(inputViewType).setEtInputType(type, shouldNotifyListeners)
    }


    @CallSuper
    open fun setInputViewType(type: Int, shouldNotifyListeners: Boolean = true) {
        mInputViewsArray.forEach {
            it.setEtInputType(type, shouldNotifyListeners)
        }
    }


    @CallSuper
    open fun setInputViewState(inputViewType: IVT, state: InputViewState) {
        getInputViewForType(inputViewType).setState(state)
    }


    @CallSuper
    open fun setInputViewText(inputViewType: IVT, text: String,
                              shouldNotifyListeners: Boolean = true) {
        with(getInputViewForType(inputViewType)) {
            setEtText(text, shouldNotifyListeners)
            setEtSelection(text.length)
        }
    }


    @CallSuper
    open fun setInputViewHintText(inputViewType: IVT, text: String) {
        getInputViewForType(inputViewType).setEtHintText(text)
    }


    @CallSuper
    open fun setInputViewLabelText(inputViewType: IVT, text: String) {
        getInputViewForType(inputViewType).setLabelText(text)
    }


    @CallSuper
    open fun setInputViewKeyListener(inputViewType: IVT, keyListener: KeyListener) {
        getInputViewForType(inputViewType).setEtKeyListener(keyListener)
    }


    @CallSuper
    open fun setInputViewKeyListener(keyListener: KeyListener) {
        mInputViewsArray.forEach {
            it.setEtKeyListener(keyListener)
        }
    }


    @CallSuper
    open fun setOnInputViewIconClickListener(inputViewType: IVT, listener: ((View) -> Unit)) {
        getInputViewForType(inputViewType).setOnIconClickListener(listener)
    }


    @CallSuper
    open fun setOnInputViewLabelClickListener(inputViewType: IVT, listener: ((View) -> Unit)) {
        getInputViewForType(inputViewType).setOnLabelClickListener(listener)
    }


    @CallSuper
    open fun addInputViewTextWatcher(inputViewType: IVT, textWatcher: TextWatcher) {
        getInputViewForType(inputViewType).addTextWatcher(textWatcher)
    }


    open fun getInputViewText(inputViewType: IVT): String {
        return getInputViewForType(inputViewType).getContent()
    }


    protected abstract fun getInputViewForType(inputViewType: IVT): InputView


    protected abstract fun getLabelViewsArray(): Array<TextView>


    protected abstract fun getInputViewsArray(): Array<InputView>


    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>?) {
        dispatchFreezeSelfOnly(container)
    }


    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>?) {
        dispatchThawSelfOnly(container)
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state.fetchProperState())

        if(state is BaseState) {
            mInputViewsArray.forEachIndexed { index, inputView ->
                inputView.setEtText(state.inputViewsContentArray[index], true)
            }
        }
    }


    @CallSuper
    protected open fun onSaveInstanceState(savedState: BaseState) {
        with(savedState) {
            inputViewsContentArray = mInputViewsArray.map { it.getContent() }.toTypedArray()
        }
    }


    abstract class BaseState : BaseSavedState {

        companion object {

            private const val KEY_INPUT_VIEWS_CONTENT_ARRAY = "input_views_content_array"
            private const val KEY_CONTAINER = "container"

        }


        internal var inputViewsContentArray: Array<CharSequence> = arrayOf()

        internal var container: SparseArray<Parcelable>? = SparseArray()


        constructor(parcel: Parcel): super(parcel) {
            parcel.readBundle(Constants.CLASS_LOADER)?.run {
                inputViewsContentArray = (getCharSequenceArray(KEY_INPUT_VIEWS_CONTENT_ARRAY) ?: arrayOf())
                container = getSparseParcelableArray(KEY_CONTAINER)
            }
        }


        constructor(superState: Parcelable?): super(superState)


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)

            parcel.writeBundle(Bundle(Constants.CLASS_LOADER).apply {
                putCharSequenceArray(KEY_INPUT_VIEWS_CONTENT_ARRAY, inputViewsContentArray)
                putSparseParcelableArray(KEY_CONTAINER, container)
            })
        }

    }


}