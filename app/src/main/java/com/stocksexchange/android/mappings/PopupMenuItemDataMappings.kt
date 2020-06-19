package com.stocksexchange.android.mappings

import com.stocksexchange.android.ui.views.popupmenu.PopupMenuItem
import com.stocksexchange.android.ui.views.popupmenu.PopupMenuItemData

fun PopupMenuItemData.mapToPopupMenuItem(): PopupMenuItem {
    return PopupMenuItem(this)
}


fun List<PopupMenuItemData>.mapToPopupMenuItems(): List<PopupMenuItem> {
    return map { it.mapToPopupMenuItem() }
}