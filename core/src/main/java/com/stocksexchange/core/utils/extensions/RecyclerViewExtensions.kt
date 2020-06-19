package com.stocksexchange.core.utils.extensions

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.disableChangeAnimations() {
    if(itemAnimator is DefaultItemAnimator) {
        (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
    }
}


fun RecyclerView.disableAnimations() {
    if (itemAnimator != null) {
        itemAnimator = null
    }
}