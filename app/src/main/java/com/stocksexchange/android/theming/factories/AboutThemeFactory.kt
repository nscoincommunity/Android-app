package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.R
import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.AboutTheme
import com.stocksexchange.core.providers.ColorProvider

class AboutThemeFactory(
    colorProvider: ColorProvider
) : BaseThemeFactory<AboutTheme>(colorProvider) {


    override fun getDeepTealTheme(): AboutTheme {
        return AboutTheme(
            refButtonVisitWebsiteBackgroundColor = getColor(R.color.deepTealAboutRefButtonVisitWebsiteBackgroundColor),
            refButtonVisitWebsiteTextColor = getColor(R.color.deepTealAboutRefButtonVisitWebsiteTextColor),
            refButtonTermsOfUseBackgroundColor = getColor(R.color.deepTealAboutRefButtonTermsOfUseBackgroundColor),
            refButtonTermsOfUseTextColor = getColor(R.color.deepTealAboutRefButtonTermsOfUseTextColor),
            refButtonPrivacyPolicyBackgroundColor = getColor(R.color.deepTealAboutRefButtonPrivacyPolicyBackgroundColor),
            refButtonPrivacyPolicyTextColor = getColor(R.color.deepTealAboutRefButtonPrivacyPolicyTextColor),
            refButtonCandyLinkBackgroundColor = getColor(R.color.deepTealAboutRefButtonCandyLinkBackgroundColor),
            refButtonCandyLinkTextColor = getColor(R.color.deepTealAboutRefButtonCandyLinkTextColor),
            socialMediaButtonColor = getColor(R.color.deepTealAboutSocialMediaButtonColor)
        )
    }


}