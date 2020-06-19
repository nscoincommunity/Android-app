package com.stocksexchange.android.mappings

import com.stocksexchange.android.model.ProfileItemModel
import com.stocksexchange.android.ui.profile.ProfileItem

fun ProfileItemModel.mapToProfileItem(): ProfileItem {
    return ProfileItem(this)
}


fun List<ProfileItemModel>.mapToProfileItemList(): List<ProfileItem> {
    return map { it.mapToProfileItem() }
}