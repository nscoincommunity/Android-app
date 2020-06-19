package com.stocksexchange.android.ui.views

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.stocksexchange.android.R

/**
 * An implementation of an EditText widget with advanced
 * support for text change listeners.
 */
class AdvancedEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle // Without this default value, EditText behaves like a TextView
) : AppCompatEditText(context, attrs, defStyleAttr) {


    private val mListeners: MutableList<TextWatcher> = mutableListOf()




    fun setInputType(type: Int, shouldNotifyListeners: Boolean = true) {
        performTask(shouldNotifyListeners) {
            super.setInputType(type)
        }
    }


    fun setText(text: CharSequence, shouldNotifyListeners: Boolean = true) {
        performTask(shouldNotifyListeners) {
            super.setText(text)
        }
    }


    override fun addTextChangedListener(listener: TextWatcher) {
        mListeners.add(listener)

        super.addTextChangedListener(listener)
    }


    override fun removeTextChangedListener(listener: TextWatcher) {
        if(mListeners.isEmpty()) {
            return
        }

        val index = mListeners.indexOf(listener)

        if(index >= 0) {
            mListeners.removeAt(index)
        }

        super.removeTextChangedListener(listener)
    }


    fun clearTextChangedListeners() {
        if(mListeners.isEmpty()) {
            return
        }

        for(listener in mListeners) {
            super.removeTextChangedListener(listener)
        }

        mListeners.clear()
    }


    private fun performTask(shouldNotifyListeners: Boolean, task: (() -> Unit)) {
        if(shouldNotifyListeners || mListeners.isEmpty()) {
            task()
            return
        }

        // Copying listeners
        val listeners = mutableListOf<TextWatcher>().apply {
            addAll(mListeners)
        }

        // Clearing listeners
        clearTextChangedListeners()

        // Performing a task
        task()

        // Restoring listeners
        for(listener in listeners) {
            addTextChangedListener(listener)
        }
    }


}