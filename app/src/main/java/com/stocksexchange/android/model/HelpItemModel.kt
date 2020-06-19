package com.stocksexchange.android.model

import com.stocksexchange.android.R

enum class HelpItemModel(
    val iconId: Int,
    val titleId: Int,
    val subtitleId: Int
) {

    FAQ(
        iconId = R.mipmap.ic_faq,
        titleId = R.string.faq,
        subtitleId = R.string.help_item_faq_subtitle
    ),
    SUPPORT(
        iconId = R.mipmap.ic_support,
        titleId = R.string.support,
        subtitleId = R.string.help_item_support_subtitle
    )

}