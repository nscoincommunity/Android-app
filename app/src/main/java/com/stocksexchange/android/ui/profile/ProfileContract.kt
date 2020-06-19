package com.stocksexchange.android.ui.profile

import com.stocksexchange.android.model.ProfileItemModel
import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface ProfileContract {


    interface View : BaseView {

        fun launchLoginActivity()

        fun launchRegistrationActivity()

        fun navigateToSettingsScreen()

        fun navigateToReferralScreen()

        fun navigateToVerificationScreen()

        fun navigateToHelpScreen()

        fun navigateToAboutScreen()

        fun setItems(items: List<ProfileItemModel>)

        fun isDataSetEmpty(): Boolean

    }


    interface ActionListener {

        fun onProfileItemClicked(itemModel: ProfileItemModel)

    }


}