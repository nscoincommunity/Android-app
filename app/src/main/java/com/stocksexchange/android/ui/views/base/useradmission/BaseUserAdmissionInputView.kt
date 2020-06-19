package com.stocksexchange.android.ui.views.base.useradmission

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.core.view.children
import com.stocksexchange.android.Constants.CLASS_LOADER
import com.stocksexchange.android.ui.views.InputView
import com.stocksexchange.android.ui.views.base.containers.BaseLinearLayoutView
import com.stocksexchange.core.utils.extensions.fetchProperState
import com.stocksexchange.core.utils.interfaces.Clearable

/**
 * A base input view that has functionality common for all
 * user admission input views.
 */
@Suppress("LeakingThis")
abstract class BaseUserAdmissionInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseLinearLayoutView(context, attrs, defStyleAttr), Clearable {


    companion object {

        internal const val ATTRIBUTE_KEY_INPUT_VIEW_TEXT_COLOR = "input_view_text_color"
        internal const val ATTRIBUTE_KEY_INPUT_VIEW_HINT_TEXT_COLOR = "input_view_hint_text_color"
        internal const val ATTRIBUTE_KEY_INPUT_VIEW_CURSOR_COLOR = "input_view_cursor_color"
        internal const val ATTRIBUTE_KEY_HINT_TEXT_COLOR = "hint_text_color"
        internal const val ATTRIBUTE_KEY_HELP_BUTTON_TEXT_COLOR = "help_button_text_color"

        internal const val DEFAULT_INPUT_VIEW_TEXT_COLOR = Color.WHITE
        internal const val DEFAULT_INPUT_VIEW_HINT_TEXT_COLOR = Color.GRAY
        internal const val DEFAULT_INPUT_VIEW_CURSOR_COLOR = Color.WHITE
        internal const val DEFAULT_HINT_TEXT_COLOR = Color.WHITE
        internal const val DEFAULT_HELP_BUTTON_TEXT_COLOR = Color.WHITE

    }




    override fun init() {
        super.init()

        orientation = VERTICAL
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setInputViewTextColor(get(ATTRIBUTE_KEY_INPUT_VIEW_TEXT_COLOR, DEFAULT_INPUT_VIEW_TEXT_COLOR))
            setInputViewHintTextColor(get(ATTRIBUTE_KEY_INPUT_VIEW_HINT_TEXT_COLOR, DEFAULT_INPUT_VIEW_HINT_TEXT_COLOR))
            setInputViewCursorColor(get(ATTRIBUTE_KEY_INPUT_VIEW_CURSOR_COLOR, DEFAULT_INPUT_VIEW_CURSOR_COLOR))
            setHintTextColor(get(ATTRIBUTE_KEY_HINT_TEXT_COLOR, DEFAULT_HINT_TEXT_COLOR))
            setHelpButtonTextColor(get(ATTRIBUTE_KEY_HELP_BUTTON_TEXT_COLOR, DEFAULT_HELP_BUTTON_TEXT_COLOR))
        }
    }


    override fun clear() {
        getInputViewsArray().forEach {
            it.clearEtText(true)
        }
    }


    fun setInputViewTextColor(@ColorInt color: Int) {
        getInputViewsArray().forEach {
            it.setEtTextColor(color)
        }
    }


    fun setInputViewHintTextColor(@ColorInt color: Int) {
        getInputViewsArray().forEach {
            it.setEtHintTextColor(color)
        }
    }


    fun setInputViewCursorColor(@ColorInt color: Int) {
        getInputViewsArray().forEach {
            it.setEtCursorColor(color)
        }
    }


    fun setInputViewExtraViewContainerBackgroundColor(@ColorInt color: Int) {
        getInputViewsArray().forEach {
            it.setExtraViewContainerBackgroundColor(color)
        }
    }


    fun setHintTextColor(@ColorInt color: Int) {
        if(!hasHint()) {
            return
        }

        getHintTv()?.setTextColor(color)
    }


    fun setHelpButtonTextColor(@ColorInt color: Int) {
        if(!hasHelpButtons()) {
            return
        }

        getHelpButtonsArray().forEach {
            it.setTextColor(color)
        }
    }


    protected abstract fun hasHint(): Boolean


    protected abstract fun hasHelpButtons(): Boolean


    protected open fun getHintTv(): TextView? = null


    protected open fun getHelpButtonsArray(): Array<TextView> {
        return arrayOf()
    }


    protected abstract fun getInputViewsArray(): Array<InputView>


    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>?) {
        dispatchFreezeSelfOnly(container)
    }


    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>?) {
        dispatchThawSelfOnly(container)
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state.fetchProperState())

        if(state is BaseUserAdmissionInputViewState) {
            getInputViewsArray().forEachIndexed { index, inputView ->
                inputView.setEtText(state.inputViewsContentArray[index], false)
            }

            for(child in children) {
                child.restoreHierarchyState(state.container)
            }
        }
    }


    @CallSuper
    protected open fun onSaveInstanceState(savedState: BaseUserAdmissionInputViewState) {
        with(savedState) {
            inputViewsContentArray = getInputViewsArray().map { it.getContent() }.toTypedArray()

            for(child in children) {
                child.saveHierarchyState(container)
            }
        }
    }


    abstract class BaseUserAdmissionInputViewState : BaseSavedState {

        companion object {

            private const val KEY_INPUT_VIEWS_CONTENT_ARRAY = "input_views_content_array"
            private const val KEY_CONTAINER = "container"

        }


        internal var inputViewsContentArray: Array<CharSequence> = arrayOf()

        internal var container: SparseArray<Parcelable>? = SparseArray()


        constructor(parcel: Parcel): super(parcel) {
            parcel.readBundle(CLASS_LOADER)?.run {
                inputViewsContentArray = (getCharSequenceArray(KEY_INPUT_VIEWS_CONTENT_ARRAY) ?: arrayOf())
                container = getSparseParcelableArray(KEY_CONTAINER)
            }
        }


        constructor(superState: Parcelable?): super(superState)


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)

            parcel.writeBundle(Bundle(CLASS_LOADER).apply {
                putCharSequenceArray(KEY_INPUT_VIEWS_CONTENT_ARRAY, inputViewsContentArray)
                putSparseParcelableArray(KEY_CONTAINER, container)
            })
        }

    }


}