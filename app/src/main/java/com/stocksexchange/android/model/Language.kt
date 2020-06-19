package com.stocksexchange.android.model

import com.stocksexchange.android.R
import java.util.*

enum class Language(
    val code: String,
    val locale: Locale,
    val nameStringId: Int,
    val imageDrawableId: Int
) {

    ENGLISH(
        code = "en",
        locale = Locale("en"),
        nameStringId = R.string.language_name_english,
        imageDrawableId = R.drawable.ic_flag_english
    ),

    RUSSIAN(
        code = "ru",
        locale = Locale("ru"),
        nameStringId = R.string.language_name_russian,
        imageDrawableId = R.drawable.ic_flag_russian
    ),

    UKRAINIAN(
        code = "uk",
        locale = Locale("uk"),
        nameStringId = R.string.language_name_ukrainian,
        imageDrawableId = R.drawable.ic_flag_ukrainian
    )

}