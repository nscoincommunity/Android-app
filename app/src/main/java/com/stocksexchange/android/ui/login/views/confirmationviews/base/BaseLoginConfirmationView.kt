package com.stocksexchange.android.ui.login.views.confirmationviews.base

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import com.stocksexchange.android.R
import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.ui.views.InputView
import com.stocksexchange.android.ui.views.base.useradmission.BaseUserAdmissionInputView
import com.stocksexchange.api.model.rest.SignInConfirmationType
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible
import com.stocksexchange.core.utils.listeners.QueryListener
import kotlinx.android.synthetic.main.login_confirmation_view.view.*

/**
 * A base view class that contains functionality for
 * login confirmation views (2Fa, email or SMS code).
 */
abstract class BaseLoginConfirmationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseUserAdmissionInputView(context, attrs, defStyleAttr) {


    var onCodeEnterListener: OnCodeEnterListener? = null




    override fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.LoginConfirmationView, defStyleAttr, 0) {
            with(mAttributes) {
                save(ATTRIBUTE_KEY_HINT_TEXT_COLOR, getColor(R.styleable.LoginConfirmationView_hintTextColor, DEFAULT_HINT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_HELP_BUTTON_TEXT_COLOR, getColor(R.styleable.LoginConfirmationView_helpButtonTextColor, DEFAULT_HELP_BUTTON_TEXT_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_TEXT_COLOR, getColor(R.styleable.LoginConfirmationView_inputViewTextColor, DEFAULT_INPUT_VIEW_TEXT_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_HINT_TEXT_COLOR, getColor(R.styleable.LoginConfirmationView_inputViewHintTextColor, DEFAULT_INPUT_VIEW_HINT_TEXT_COLOR))
                save(ATTRIBUTE_KEY_INPUT_VIEW_CURSOR_COLOR, getColor(R.styleable.LoginConfirmationView_inputViewCursorColor, DEFAULT_INPUT_VIEW_CURSOR_COLOR))
            }
        }
    }


    override fun init() {
        super.init()

        codeIv.setEtHintText(mStringProvider.getString(R.string.enter_code))
        codeIv.addTextWatcher(QueryListener(object : QueryListener.Callback {

            override fun onQueryEntered(query: String) {
                if(query.isBlank() || (query.length != getCodeLength())) {
                    return
                }

                onCodeEnterListener?.onCodeEntered(codeIv.getInputViewEt(), getConfirmationType(), query)
            }

        }))

        setHintText(getHintText())
        setInputViewType(getInputViewType())
        setLabelText(getInputLabelText())

        if(hasHelpButtons()) {
            helpBtnTv.makeVisible()

            setHelpButtonText(getHelpButtonText())
        } else {
            helpBtnTv.makeGone()
        }
    }


    fun setInputViewType(type: Int) {
        codeIv.setEtInputType(type)
    }


    fun setCodeInputViewState(state: InputViewState) = codeIv.setState(state)


    fun setHintText(text: String) {
        hintTv.text = text
    }


    fun setLabelText(text: String) {
        codeLabelTv.text = text
    }


    fun setHelpButtonText(text: String) {
        helpBtnTv.text = text
    }


    fun setOnHelpButtonClickListener(listener: ((SignInConfirmationType, View) -> Unit)) {
        helpBtnTv.setOnClickListener {
            listener(getConfirmationType(), it)
        }
    }


    fun setTextColor(@ColorInt color: Int) {
        hintTv.setTextColor(color)
        codeLabelTv.setTextColor(color)
        helpBtnTv.setTextColor(color)
    }


    override fun hasHint(): Boolean = true


    override fun getLayoutResourceId(): Int = R.layout.login_confirmation_view


    protected abstract fun getCodeLength(): Int


    protected abstract fun getInputViewType(): Int


    protected abstract fun getConfirmationType(): SignInConfirmationType


    protected abstract fun getHintText(): String


    protected abstract fun getInputLabelText(): String


    protected open fun getHelpButtonText(): String {
        return ""
    }


    fun getCode(): String = codeIv.getContent()


    override fun getHintTv(): TextView? = hintTv


    override fun getHelpButtonsArray(): Array<TextView> = arrayOf(helpBtnTv)


    override fun getInputViewsArray(): Array<InputView> = arrayOf(codeIv)


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).also {
            onSaveInstanceState(it)
        }
    }


    interface OnCodeEnterListener {

        /**
         * A callback to invoke when each character of the code is entered
         * in the input view.
         *
         * @param editText The edit text
         * @param type The confirmation type
         * @param code The code itself
         */
        fun onCodeEntered(editText: EditText, type: SignInConfirmationType, code: String)

    }


    private class SavedState : BaseUserAdmissionInputViewState {

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