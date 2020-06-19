package com.stocksexchange.android.ui.verification.selection

import com.arthurivanets.adapster.markers.ItemResources
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.formatters.NumberFormatter

class VerificationSelectionResources(
    val profileInfo: ProfileInfo,
    val settings: Settings,
    val stringProvider: StringProvider,
    val numberFormatter: NumberFormatter,
    val strings: List<String>
) : ItemResources {


    companion object {

        const val STRING_VERIFIED = 0


        fun newInstance(profileInfo: ProfileInfo,
                        settings: Settings,
                        stringProvider: StringProvider,
                        numberFormatter: NumberFormatter): VerificationSelectionResources {
            val strings = listOf(
                stringProvider.getString(R.string.verified)
            )

            return VerificationSelectionResources(
                profileInfo = profileInfo,
                settings = settings,
                stringProvider = stringProvider,
                numberFormatter = numberFormatter,
                strings = strings
            )
        }

    }


}