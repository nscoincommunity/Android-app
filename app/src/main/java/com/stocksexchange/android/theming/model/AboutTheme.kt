package com.stocksexchange.android.theming.model

import java.io.Serializable

data class AboutTheme(
    val refButtonVisitWebsiteBackgroundColor: Int,
    val refButtonVisitWebsiteTextColor: Int,
    val refButtonTermsOfUseBackgroundColor: Int,
    val refButtonTermsOfUseTextColor: Int,
    val refButtonPrivacyPolicyBackgroundColor: Int,
    val refButtonPrivacyPolicyTextColor: Int,
    val refButtonCandyLinkBackgroundColor: Int,
    val refButtonCandyLinkTextColor: Int,
    val socialMediaButtonColor: Int
) : Serializable {


    companion object {

        val STUB = AboutTheme(
            refButtonVisitWebsiteBackgroundColor = -1,
            refButtonVisitWebsiteTextColor = -1,
            refButtonTermsOfUseBackgroundColor = -1,
            refButtonTermsOfUseTextColor = -1,
            refButtonPrivacyPolicyBackgroundColor = -1,
            refButtonPrivacyPolicyTextColor = -1,
            refButtonCandyLinkBackgroundColor = -1,
            refButtonCandyLinkTextColor = -1,
            socialMediaButtonColor = -1
        )

    }


}