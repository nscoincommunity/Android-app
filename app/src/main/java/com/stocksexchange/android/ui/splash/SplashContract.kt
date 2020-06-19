package com.stocksexchange.android.ui.splash

import com.stocksexchange.android.model.PinCodeMode
import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface SplashContract {


    interface View : BaseView {

        fun showInfoViewContainer(caption: String)

        fun hideInfoViewContainer()

        fun launchLoginActivity()

        fun launchAuthenticationActivity(pinCodeMode: PinCodeMode)

        fun launchDestinationActivity()

        fun finishActivity()

    }


    interface ActionListener


}