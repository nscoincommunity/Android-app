package com.stocksexchange.core.utils.extensions

import com.stocksexchange.core.utils.interfaces.CanObserveNetworkStateChanges
import com.stocksexchange.core.utils.interfaces.Scrollable
import com.stocksexchange.core.utils.interfaces.Selectable
import com.stocksexchange.core.utils.interfaces.Sortable
import com.stocksexchange.core.utils.listeners.OnBackPressListener


fun Any.attemptToScrollUp() {
    if(this is Scrollable) {
        this.scrollToTop()
    }
}


fun Any.attemptToSort(payload: Any) {
    if(this is Sortable) {
        this.sort(payload)
    }
}


fun Any.attemptToSelect(isSelected: Boolean, source: Selectable.Source) {
    if(this is Selectable) {
        this.setSelected(isSelected, source)
    }
}


fun Any.handleSelectionEvent() {
    if(this is Selectable) {
        this.onSelected()
    }
}


fun Any.handleDeselectionEvent() {
    if(this is Selectable) {
        this.onUnselected()
    }
}


fun Any.handleBackPressEvent(): Boolean {
    return ((this is OnBackPressListener) && this.onBackPressed())
}


fun Any.handleNetworkConnectionEvent() {
    if(this is CanObserveNetworkStateChanges) {
        this.onNetworkConnected()
    }
}


fun Any.handleNetworkDisconnectionEvent() {
    if(this is CanObserveNetworkStateChanges) {
        this.onNetworkDisconnected()
    }
}


fun List<Any>.attemptToScrollUp() {
    for(item in this) {
        item.attemptToScrollUp()
    }
}


fun List<Any>.attemptToSort(payload: Any) {
    for(item in this) {
        item.attemptToSort(payload)
    }
}


fun List<Any>.handleSelectionEvent() {
    for(item in this) {
        item.handleSelectionEvent()
    }
}


fun List<Any>.handleDeselectionEvent() {
    for(item in this) {
        item.handleDeselectionEvent()
    }
}


fun List<Any>.handleBackPressEvent(): Boolean {
    for(item in this) {
        if(item.handleBackPressEvent()) {
            return true
        }
    }

    return false
}


fun List<Any>.handleNetworkConnectionEvent() {
    for(item in this) {
        item.handleNetworkConnectionEvent()
    }
}


fun List<Any>.handleNetworkDisconnectionEvent() {
    for(item in this) {
        item.handleNetworkDisconnectionEvent()
    }
}