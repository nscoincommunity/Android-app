package com.stocksexchange.android.ui.about

import com.stocksexchange.android.model.AboutReference
import com.stocksexchange.android.model.SocialMediaType
import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface AboutContract {


    interface View : BaseView {

        fun shareText(text: String, chooserTitle: String)

        fun launchBrowser(url: String)

    }


    interface ActionListener {

        fun onShareButtonClicked()

        fun onReferenceButtonClicked(type: AboutReference)

        fun onSocialMediaButtonClicked(type: SocialMediaType)

    }


}