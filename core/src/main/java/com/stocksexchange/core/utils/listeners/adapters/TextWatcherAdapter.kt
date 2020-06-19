package com.stocksexchange.core.utils.listeners.adapters

import android.text.Editable
import android.text.TextWatcher

interface TextWatcherAdapter : TextWatcher {


    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        // Stub
    }


    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        // Stub
    }


    override fun afterTextChanged(s: Editable) {
        // Stub
    }


}