package com.stocksexchange.android.ui.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.KeyListener
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.stocksexchange.android.Constants.CLASS_LOADER
import com.stocksexchange.android.R
import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.ui.views.base.containers.BaseRelativeLayoutView
import com.stocksexchange.android.utils.extensions.*
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.input_view_layout.view.*

/**
 * A container view that acts as an EditText with extra
 * functionality like providing a label and an icon.
 */
@Suppress("LeakingThis")
class InputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseRelativeLayoutView(context, attrs, defStyleAttr) {


    companion object {

        private const val ATTRIBUTE_KEY_ET_TEXT_COLOR = "et_text_color"
        private const val ATTRIBUTE_KEY_ET_HINT_TEXT_COLOR = "et_hint_text_color"
        private const val ATTRIBUTE_KEY_ET_CURSOR_COLOR = "et_cursor_color"
        private const val ATTRIBUTE_KEY_ET_HINT_TEXT = "et_hint_text"
        private const val ATTRIBUTE_KEY_NORMAL_BACKGROUND = "normal_background"
        private const val ATTRIBUTE_KEY_ERRONEOUS_BACKGROUND = "erroneous_background"
        private const val ATTRIBUTE_KEY_ICON = "icon"
        private const val ATTRIBUTE_KEY_ICON_COLOR = "icon_color"
        private const val ATTRIBUTE_KEY_LABEL_TEXT = "label_text"
        private const val ATTRIBUTE_KEY_LABEL_TEXT_COLOR = "label_text_color"

        private const val DEFAULT_ET_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_ET_HINT_TEXT_COLOR = Color.GRAY
        private const val DEFAULT_ET_CURSOR_COLOR = Color.WHITE

        private const val DEFAULT_ET_HINT_TEXT = ""

        private const val DEFAULT_ICON_COLOR = Color.GRAY

        private const val DEFAULT_NORMAL_BACKGROUND = R.drawable.input_view_normal_background_drawable
        private const val DEFAULT_ERRONEOUS_BACKGROUND = R.drawable.input_view_erroneous_background_drawable

        private const val DEFAULT_LABEL_TEXT = ""
        private const val DEFAULT_LABEL_TEXT_COLOR = Color.WHITE

    }


    private var mState: InputViewState = InputViewState.NORMAL

    private var mNormalBackground: Drawable? = null
    private var mErroneousBackground: Drawable? = null




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.InputView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_ET_TEXT_COLOR, getColor(R.styleable.InputView_etTextColor, DEFAULT_ET_TEXT_COLOR))
                save(ATTRIBUTE_KEY_ET_HINT_TEXT_COLOR, getColor(R.styleable.InputView_etHintTextColor, DEFAULT_ET_HINT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_ET_CURSOR_COLOR, getColor(R.styleable.InputView_etCursorColor, DEFAULT_ET_CURSOR_COLOR))
                save(ATTRIBUTE_KEY_ET_HINT_TEXT, (getString(R.styleable.InputView_etHintText) ?: DEFAULT_ET_HINT_TEXT))
                save(ATTRIBUTE_KEY_NORMAL_BACKGROUND, (getDrawable(R.styleable.InputView_normalBackground)) ?: getCompatDrawable(DEFAULT_NORMAL_BACKGROUND))
                save(ATTRIBUTE_KEY_ERRONEOUS_BACKGROUND, (getDrawable(R.styleable.InputView_erroneousBackground)) ?: getCompatDrawable(DEFAULT_ERRONEOUS_BACKGROUND))
                save(ATTRIBUTE_KEY_ICON, getDrawable(R.styleable.InputView_icon))
                save(ATTRIBUTE_KEY_ICON_COLOR, getColor(R.styleable.InputView_iconColor, DEFAULT_ICON_COLOR))
                save(ATTRIBUTE_KEY_LABEL_TEXT, (getString(R.styleable.InputView_labelText) ?: DEFAULT_LABEL_TEXT))
                save(ATTRIBUTE_KEY_LABEL_TEXT_COLOR, getColor(R.styleable.InputView_labelTextColor, DEFAULT_LABEL_TEXT_COLOR))
            }
        }
    }


    override fun init() {
        super.init()

        // Disabling automatic state save & restore of the EditText since
        // on restore it calls setText(...) which in place calls all text
        // change listeners which is undesirable. The only reasonable way
        // to fix this is to manually save & restore its state
        editText.isSaveEnabled = false
    }


    override fun applyAttributes() {
        with(mAttributes) {
            setEtTextColor(get(ATTRIBUTE_KEY_ET_TEXT_COLOR, DEFAULT_ET_TEXT_COLOR))
            setEtHintTextColor(get(ATTRIBUTE_KEY_ET_HINT_TEXT_COLOR, DEFAULT_ET_HINT_TEXT_COLOR))
            setEtCursorColor(get(ATTRIBUTE_KEY_ET_CURSOR_COLOR, DEFAULT_ET_CURSOR_COLOR))
            setEtHintText(get(ATTRIBUTE_KEY_ET_HINT_TEXT, DEFAULT_ET_HINT_TEXT))
            setNormalBackground(get(ATTRIBUTE_KEY_NORMAL_BACKGROUND, getCompatDrawable(DEFAULT_NORMAL_BACKGROUND)))
            setErroneousBackground(get(ATTRIBUTE_KEY_ERRONEOUS_BACKGROUND, getCompatDrawable(DEFAULT_ERRONEOUS_BACKGROUND)))
            setIconDrawable(get(ATTRIBUTE_KEY_ICON, null))
            setIconColor(get(ATTRIBUTE_KEY_ICON_COLOR, DEFAULT_ICON_COLOR))
            setLabelText(get(ATTRIBUTE_KEY_LABEL_TEXT, DEFAULT_LABEL_TEXT))
            setLabelTextColor(get(ATTRIBUTE_KEY_LABEL_TEXT_COLOR, DEFAULT_LABEL_TEXT_COLOR))
        }
    }


    private fun resetStateIfNecessary() {
        if(mState == InputViewState.NORMAL) {
            return
        }

        mState = InputViewState.NORMAL
        setNormalBackground(mNormalBackground)
    }


    fun enableEditing() {
        editText.enableEditing()
    }


    fun disableEditing() {
        editText.disableEditing()
    }


    fun setIconVisible(isVisible: Boolean) {
        if(isVisible) {
            iconIv.makeVisible()
        } else {
            iconIv.makeGone()
        }
    }


    fun setLabelVisible(isVisible: Boolean) {
        if(isVisible) {
            labelTv.makeVisible()
        } else {
            labelTv.makeGone()
        }
    }


    fun setEtTextColor(@ColorInt color: Int) {
        editText.setTextColor(color)
    }


    fun setEtHintTextColor(@ColorInt color: Int) {
        editText.setHintTextColor(color)
    }


    fun setEtCursorColor(@ColorInt color: Int) {
        editText.setCursorDrawable(context.getCursorDrawable(color))
    }


    fun setIconColor(@ColorInt color: Int) {
        iconIv.setColor(color)
    }


    fun setLabelTextColor(@ColorInt color: Int) {
        labelTv.setTextColor(color)
    }


    fun setExtraViewContainerBackgroundColor(@ColorInt color: Int) {
        extraViewsContainerFl.background = context.getInputViewExtraViewBackground(color)
    }


    fun setExtraViewContainerWidth(width: Int) {
        extraViewsContainerFl.setWidth(width)
    }


    fun setEtSelection(index: Int) {
        editText.setSelection(index)
    }


    fun setEtInputType(type: Int, shouldNotifyListeners: Boolean = true) {
        resetStateIfNecessary()

        editText.setInputType(type, shouldNotifyListeners)
    }


    fun setState(state: InputViewState) {
        if(mState == state) {
            return
        }

        mState = state

        when(state) {
            InputViewState.NORMAL -> setNormalBackground(mNormalBackground)
            InputViewState.ERRONEOUS -> setErroneousBackground(mErroneousBackground)
        }
    }


    fun setEtHintText(text: String) {
        editText.hint = text
    }


    fun setLabelText(text: String) {
        labelTv.text = text
    }


    fun setEtText(text: CharSequence, shouldNotifyListeners: Boolean = true) {
        resetStateIfNecessary()

        editText.setText(text, shouldNotifyListeners)
    }


    /**
     * Sets a normal background to be used when the state is [InputViewState.NORMAL].
     *
     * @param backgroundDrawable The normal background drawable
     */
    fun setNormalBackground(backgroundDrawable: Drawable?) {
        mNormalBackground = backgroundDrawable

        if(mState == InputViewState.NORMAL) {
            background = backgroundDrawable
        }
    }


    /**
     * Sets an erroneous background to be used when the state is [InputViewState.ERRONEOUS].
     *
     * @param backgroundDrawable The erroneous background drawable
     */
    fun setErroneousBackground(backgroundDrawable: Drawable?) {
        mErroneousBackground = backgroundDrawable

        if(mState == InputViewState.ERRONEOUS) {
            background = backgroundDrawable
        }
    }


    fun setIconDrawable(drawable: Drawable?) {
        iconIv.setImageDrawable(drawable)
    }


    fun setEtFilters(filters: Array<InputFilter>) {
        editText.filters = filters
    }


    fun setEtKeyListener(keyListener: KeyListener) {
        editText.keyListener = keyListener
    }


    fun setOnIconClickListener(listener: ((View) -> Unit)) {
        extraViewsContainerFl.setOnClickListener(listener)
    }


    fun setOnLabelClickListener(listener: ((View) -> Unit)) {
        extraViewsContainerFl.setOnClickListener(listener)
    }


    fun addTextWatcher(textWatcher: TextWatcher) {
        editText.addTextChangedListener(textWatcher)
    }


    fun clearEtText(shouldNotifyListener: Boolean) {
        setEtText("", shouldNotifyListener)
    }


    override fun getLayoutResourceId(): Int = R.layout.input_view_layout


    fun getContent(): String = editText.getContent()


    fun getInputViewEt(): EditText = editText


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state.fetchProperState())

        if(state is SavedState) {
            setState(state.state)
        }
    }


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).apply {
            state = mState
        }
    }


    private class SavedState : BaseSavedState {

        companion object {

            private const val KEY_STATE = "state"


            @JvmField
            var CREATOR = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(parcel: Parcel): SavedState = SavedState(parcel)

                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)

            }

        }


        internal var state: InputViewState = InputViewState.NORMAL


        constructor(parcel: Parcel): super(parcel) {
            parcel.readBundle(CLASS_LOADER)?.run {
                state = getSerializableOrThrow(KEY_STATE)
            }
        }


        constructor(superState: Parcelable?): super(superState)


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)

            parcel.writeBundle(Bundle(CLASS_LOADER).apply {
                putSerializable(KEY_STATE, state)
            })
        }

    }


}