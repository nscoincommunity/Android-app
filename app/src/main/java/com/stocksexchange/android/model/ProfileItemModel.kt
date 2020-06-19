package com.stocksexchange.android.model

import androidx.annotation.StringRes
import com.stocksexchange.android.R

enum class ProfileItemModel(
    @StringRes val titleId: Int,
    @StringRes val descriptionId: Int
) {


    LOGIN(
        titleId = R.string.profile_fragment_item_login_title,
        descriptionId = R.string.profile_fragment_item_login_description
    ),
    REGISTRATION(
        titleId = R.string.profile_fragment_item_registration_title,
        descriptionId = R.string.profile_fragment_item_registration_description
    ),
    SETTINGS(
        titleId = R.string.profile_fragment_item_settings_title,
        descriptionId = R.string.profile_fragment_item_settings_description
    ),
    REFERRAL_PROGRAM(
        titleId = R.string.profile_fragment_item_referral_program_title,
        descriptionId = R.string.profile_fragment_item_referral_program_description
    ),
    VERIFICATION(
        titleId = R.string.profile_fragment_item_verification_title,
        descriptionId = R.string.profile_fragment_item_verification_description
    ),
    SUPPORT_CENTER(
        titleId = R.string.profile_fragment_item_support_center_title,
        descriptionId = R.string.profile_fragment_item_support_center_description
    ),
    ABOUT(
        titleId = R.string.profile_fragment_item_about_title,
        descriptionId = R.string.profile_fragment_item_about_description
    )


}