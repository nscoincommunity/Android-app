package com.stocksexchange.core.managers

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.stocksexchange.core.utils.extensions.getInputMethodManager
import com.stocksexchange.core.utils.interfaces.Recyclable

/**
 * Responsible for showing and hiding the keyboard on the screen.
 */
class KeyboardManager(context: Context) : Recyclable {


    private var isRecycled: Boolean = false

    private var inputMethodManager: InputMethodManager? = context.getInputMethodManager()




    /**
     * Requests that the keyboard be shown to the user.
     *
     * @param view The currently focused view which would
     * like to receive soft keyboard input.
     */
    fun showKeyboard(view: View?) {
        if(view != null) {
            inputMethodManager?.showSoftInput(view, 0)
        }
    }


    /**
     * Requests that the keyboard be hidden from the user.
     *
     * @param view The currently focused view
     */
    fun hideKeyboard(view: View?) {
        if(view != null) {
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    override fun recycle() {
        if(isRecycled) {
            return
        }

        inputMethodManager = null
        isRecycled = true
    }


}