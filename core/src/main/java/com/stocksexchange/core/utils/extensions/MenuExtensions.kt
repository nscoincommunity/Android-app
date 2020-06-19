package com.stocksexchange.core.utils.extensions

import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.MenuItem


fun Menu.addItem(id: Int, title: String, icon: Drawable?): MenuItem {
    return add(Menu.NONE, id, Menu.NONE, title).apply {
        this.icon = icon
    }
}