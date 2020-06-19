package com.stocksexchange.api.model.rest

import com.stocksexchange.api.R
import com.stocksexchange.api.utils.Constants

enum class VerificationType(
    val iconId: Int,
    val titleId: Int,
    val descriptionId: Int,
    val descriptionArgId: Int,
    val url: String,
    val iconTopMarginId: Int = R.dimen.verification_type_default_icon_top_margin,
    val iconBottomMarginId: Int = R.dimen.verification_type_default_icon_bottom_margin,
    val descriptionLineCount: Int = 4
) {

    FRACTAL(
        iconId = R.mipmap.ic_verification_fractal,
        titleId = R.string.verification_type_fractal_title,
        descriptionId = R.string.verification_type_fractal_description_template,
        descriptionArgId = R.string.verification_type_fractal_description_template_argument,
        url = Constants.STEX_VERIFICATION_TYPE_FRACTAL_URL
    ),
    SMART_ID(
        iconId = R.mipmap.ic_verification_smartid,
        titleId = R.string.verification_type_smart_id_title,
        descriptionId = R.string.verification_type_smart_id_description_template,
        descriptionArgId = R.string.verification_type_smart_id_description_template_argument,
        url = Constants.STEX_VERIFICATION_TYPE_SMART_ID_URL
    ),
    STEX(
        iconId = R.mipmap.ic_verification_stex,
        titleId = R.string.verification_type_stex_title,
        descriptionId = R.string.verification_type_stex_description_template,
        descriptionArgId = R.string.verification_type_stex_description_template_argument,
        url = Constants.STEX_VERIFICATION_TYPE_STEX_URL
    ),
    PRIVATBANK(
        iconId = R.mipmap.ic_verification_privatbank,
        titleId = R.string.verification_type_privatbank_title,
        descriptionId = R.string.verification_type_privatbank_description_template,
        descriptionArgId = R.string.verification_type_privatbank_description_template_argument,
        url = Constants.STEX_VERIFICATION_TYPE_PRIVATBANK_URL
    ),
    CRYPTONOMICA(
        iconId = R.mipmap.ic_verification_cryptonomica,
        titleId = R.string.verification_type_cryptonomica_title,
        descriptionId = R.string.verification_type_cryptonomica_description_template,
        descriptionArgId = R.string.verification_type_cryptonomica_description_template_argument,
        url = Constants.STEX_VERIFICATION_TYPE_CRYPTONOMICA_URL,
        iconTopMarginId = R.dimen.verification_type_cryptonomica_icon_top_margin,
        iconBottomMarginId = R.dimen.verification_type_cryptonomica_icon_bottom_margin,
        descriptionLineCount = 5
    );

}