package com.stocksexchange.android.model

import java.io.Serializable

data class LanguageItemModel(
    val language: Language,
    val isSelected: Boolean = false
) : Serializable {

    val code: String = language.code

    val nameStringId: Int = language.nameStringId

}