package com.stocksexchange.android.ui.views.base.useradmission

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.InputType
import android.util.AttributeSet
import androidx.annotation.ColorInt
import com.stocksexchange.android.Constants.CLASS_LOADER
import com.stocksexchange.android.R
import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.ui.views.InputView
import com.stocksexchange.core.utils.extensions.getCompatDrawable
import com.stocksexchange.core.utils.extensions.setColor

/**
 * A base credentials view that contains common functionality
 * for all credentials views.
 */
abstract class BaseUserAdmissionCredentialsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseUserAdmissionInputView(context, attrs, defStyleAttr) {


    companion object {

        internal const val ATTRIBUTE_KEY_INPUT_VIEW_ICON_COLOR = "input_view_icon_color"
        internal const val ATTRIBUTE_KEY_PASSWORD_INPUT_VIEW_VISIBLE_ICON = "password_input_view_visible_icon"
        internal const val ATTRIBUTE_KEY_PASSWORD_INPUT_VIEW_HIDDEN_ICON = "password_input_view_hidden_icon"

        internal const val DEFAULT_INPUT_VIEW_ICON_COLOR = Color.GRAY

        internal const val DEFAULT_PASSWORD_INPUT_VIEW_VISIBLE_ICON = R.drawable.ic_eye_off
        internal const val DEFAULT_PASSWORD_INPUT_VIEW_HIDDEN_ICON = R.drawable.ic_eye_on

    }


    private var mIsPasswordVisible: Boolean = false

    private var mInputViewIconColor: Int = 0

    private var mPasswordInputViewVisibleIcon: Drawable? = null
    private var mPasswordInputViewHiddenIcon: Drawable? = null




    override fun init() {
        super.init()

        initEmailInput()
        initPasswordInput()
    }


    private fun initEmailInput() {
        with(getEmailIv()) {
            setEtInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            setEtHintText(getEmailInputHintText())
        }
    }


    private fun initPasswordInput() {
        with(getPasswordIv()) {
            setIconVisible(true)
            setEtHintText(getPasswordInputHintText())
            setOnIconClickListener {
                setPasswordVisible(!mIsPasswordVisible)
            }
        }

        updatePasswordVisibility()
    }


    override fun applyAttributes() {
        super.applyAttributes()

        with(mAttributes) {
            setInputViewIconColor(get(ATTRIBUTE_KEY_INPUT_VIEW_ICON_COLOR, DEFAULT_INPUT_VIEW_ICON_COLOR))
            setPasswordInputViewVisibleIcon(get(ATTRIBUTE_KEY_PASSWORD_INPUT_VIEW_VISIBLE_ICON, getCompatDrawable(DEFAULT_PASSWORD_INPUT_VIEW_VISIBLE_ICON)))
            setPasswordInputViewHiddenIcon(get(ATTRIBUTE_KEY_PASSWORD_INPUT_VIEW_HIDDEN_ICON, getCompatDrawable(DEFAULT_PASSWORD_INPUT_VIEW_HIDDEN_ICON)))
        }
    }


    private fun updatePasswordVisibility() {
        val drawable = if(mIsPasswordVisible) {
            mPasswordInputViewVisibleIcon
        } else {
            mPasswordInputViewHiddenIcon
        }
        val type = if(mIsPasswordVisible) {
            (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
        } else {
            (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        }

        with(getPasswordIv()) {
            setIconDrawable(drawable)
            setEtInputType(type, false)
            setEtSelection(getInputViewEt().length())
        }

        onPasswordVisibilityChanged(mIsPasswordVisible, drawable, type)
    }


    override fun clear() {
        super.clear()

        setPasswordVisible(false)
    }


    fun setPasswordVisible(isPasswordVisible: Boolean) {
        mIsPasswordVisible = isPasswordVisible
        updatePasswordVisibility()
    }


    open fun setInputViewIconColor(@ColorInt color: Int) {
        mInputViewIconColor = color

        mPasswordInputViewVisibleIcon?.setColor(color)
        mPasswordInputViewHiddenIcon?.setColor(color)

        updatePasswordVisibility()
    }


    fun setEmailInputViewState(state: InputViewState) {
        getEmailIv().setState(state)
    }


    fun setPasswordInputViewState(state: InputViewState) {
        getPasswordIv().setState(state)
    }


    fun setPasswordInputViewVisibleIcon(drawable: Drawable?) {
        mPasswordInputViewVisibleIcon = drawable?.apply {
            setColor(mInputViewIconColor)
        }

        if(mIsPasswordVisible) {
            updatePasswordVisibility()
        }
    }


    fun setPasswordInputViewHiddenIcon(drawable: Drawable?) {
        mPasswordInputViewHiddenIcon = drawable?.apply {
            setColor(mInputViewIconColor)
        }

        if(!mIsPasswordVisible) {
            updatePasswordVisibility()
        }
    }


    protected open fun getEmailInputHintText(): String {
        return mStringProvider.getString(R.string.enter_your_email)
    }


    protected open fun getPasswordInputHintText(): String {
        return mStringProvider.getString(R.string.enter_your_password)
    }


    fun getEmail(): String = getEmailIv().getContent()


    fun getPassword(): String = getPasswordIv().getContent()


    protected abstract fun getEmailIv(): InputView


    protected abstract fun getPasswordIv(): InputView


    /**
     * A callback that gets invoked whenever the password visibility changes.
     */
    protected open fun onPasswordVisibilityChanged(isPasswordVisible: Boolean,
                                                   newDrawable: Drawable?,
                                                   newType: Int) {
        // Stub
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)

        if(state is SavedState) {
            setPasswordVisible(state.isPasswordVisible)
        }
    }


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).also {
            onSaveInstanceState(it)
        }
    }


    override fun onSaveInstanceState(savedState: BaseUserAdmissionInputViewState) {
        super.onSaveInstanceState(savedState)

        if(savedState is SavedState) {
            with(savedState) {
                isPasswordVisible = mIsPasswordVisible
            }
        }
    }


    private class SavedState : BaseUserAdmissionInputViewState {

        companion object {

            private const val KEY_IS_PASSWORD_VISIBLE = "is_password_visible"


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


        internal var isPasswordVisible: Boolean = false


        constructor(parcel: Parcel): super(parcel) {
            parcel.readBundle(CLASS_LOADER)?.run {
                isPasswordVisible = getBoolean(KEY_IS_PASSWORD_VISIBLE, false)
            }
        }


        constructor(superState: Parcelable?): super(superState)


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)

            parcel.writeBundle(Bundle(CLASS_LOADER).apply {
                putBoolean(KEY_IS_PASSWORD_VISIBLE, isPasswordVisible)
            })
        }

    }


}