package com.stocksexchange.core.utils.extensions

import android.view.View
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView


fun ScrollView.scrollToTop() {
    post { fullScroll(View.FOCUS_UP) }
}


fun ScrollView.scrollToBottom() {
    post { fullScroll(View.FOCUS_DOWN) }
}


fun NestedScrollView.scrollToTop() {
    post { fullScroll(View.FOCUS_UP) }
}


fun NestedScrollView.scrollToBottom() {
    post { fullScroll(View.FOCUS_DOWN) }
}