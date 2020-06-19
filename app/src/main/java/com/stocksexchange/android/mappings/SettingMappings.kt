package com.stocksexchange.android.mappings

import com.arthurivanets.adapster.model.BaseItem
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingAccount
import com.stocksexchange.android.model.SettingSection
import com.stocksexchange.android.ui.settings.items.SettingAccountItem
import com.stocksexchange.android.ui.settings.items.SettingItem
import com.stocksexchange.android.ui.settings.items.SettingSectionItem

fun SettingSection.mapToSettingSectionItem(): SettingSectionItem {
    return SettingSectionItem(this)
}


fun Setting.mapToFragSettingItem(): SettingItem {
    return SettingItem(this)
}


fun SettingAccount.mapToSettingAccountItem(): SettingAccountItem {
    return SettingAccountItem(this)
}


fun List<Any>.mapToSettingItemList(): List<BaseItem<*, *, *>> {
    return map {
        when (it) {
            is SettingSection -> it.mapToSettingSectionItem()
            is Setting -> it.mapToFragSettingItem()
            is SettingAccount -> it.mapToSettingAccountItem()

            else -> throw IllegalStateException(
                "Please provide a mapping function for the ${it.javaClass.simpleName} class."
            )
        }
    }
}