package com.stocksexchange.android.ui.about

import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.android.analytics.FirebaseEventLogger
import com.stocksexchange.android.model.AboutReference
import com.stocksexchange.android.model.SocialMediaType
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class AboutPresenter(
    view: AboutContract.View,
    model: StubModel,
    private val firebaseEventLogger: FirebaseEventLogger
) : BasePresenter<AboutContract.View, StubModel>(view, model), AboutContract.ActionListener {


    override fun onShareButtonClicked() {
        mView.shareText(
            mStringProvider.getString(
                R.string.app_sharing_template,
                Constants.PLAY_STORE_APP_REFERENCE
            ),
            mStringProvider.getString(R.string.action_share_via)
        )
    }


    override fun onReferenceButtonClicked(type: AboutReference) {
        when(type) {
            AboutReference.WEBSITE -> firebaseEventLogger.onAboutVisitOurWebsiteButtonClicked()
            AboutReference.TERMS_OF_USE -> firebaseEventLogger.onAboutOurTermsOfUseButtonClicked()
            AboutReference.PRIVACY_POLICY -> firebaseEventLogger.onAboutPrivacyPolicyButtonClicked()
            AboutReference.CANDY_LINK -> firebaseEventLogger.onAboutCandyLinkButtonClicked()
        }

        if(BuildConfig.IS_CHINESE_FLAVOR && (type == AboutReference.WEBSITE)) {
            return
        }

        mView.launchBrowser(type.url)
    }


    override fun onSocialMediaButtonClicked(type: SocialMediaType) {
        mView.launchBrowser(type.url)
    }


    override fun onBackPressed(): Boolean {
        return onNavigateUpPressed()
    }


}