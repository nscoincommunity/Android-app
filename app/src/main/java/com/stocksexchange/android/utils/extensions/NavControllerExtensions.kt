package com.stocksexchange.android.utils.extensions

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController

fun NavController.containsDestination(@IdRes id: Int): Boolean {
    return try {
        getBackStackEntry(id)
        true
    } catch(exception: Exception) {
        false
    }
}


fun NavController.containsAnyDestination(@IdRes ids: List<Int>): Boolean {
    return ids.any { containsDestination(it) }
}


fun NavController.getDestinationArgs(@IdRes id: Int): Bundle? {
    return try {
        getBackStackEntry(id).arguments
    } catch(exception: Exception) {
        null
    }
}