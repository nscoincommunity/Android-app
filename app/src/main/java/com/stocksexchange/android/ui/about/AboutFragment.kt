package com.stocksexchange.android.ui.about

import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.R
import com.stocksexchange.android.model.AboutReference
import com.stocksexchange.android.model.SocialMediaType
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.utils.handlers.BrowserHandler
import com.stocksexchange.android.utils.handlers.SharingHandler
import com.stocksexchange.core.formatters.TimeFormatter
import com.stocksexchange.core.utils.extensions.ctx
import kotlinx.android.synthetic.main.about_fragment_layout.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class AboutFragment : BaseFragment<AboutPresenter>(), AboutContract.View {


    override val mPresenter: AboutPresenter by inject { parametersOf(this) }




    override fun init() {
        super.init()

        initContentContainer()
        initToolbar()
        initAppLogo()
        initVersion()
        initAppMotto()
        initReferenceButtons()
        initBottomContainer()
    }


    private fun initContentContainer() = with(mRootView.contentContainerRl) {
        ThemingUtil.About.contentContainer(this, getAppTheme())
    }


    private fun initToolbar() = with(mRootView.toolbar) {
        setTitleText(getStr(R.string.about))

        setOnLeftButtonClickListener {
            mPresenter.onNavigateUpPressed()
        }
        setOnRightButtonClickListener {
            mPresenter.onShareButtonClicked()
        }

        ThemingUtil.About.toolbar(this, getAppTheme())
    }


    private fun initAppLogo() = with(mRootView.appLogoIv) {
        ThemingUtil.About.appLogo(this, getAppTheme())
    }


    private fun initAppMotto() = with(mRootView.appMottoTv) {
        text = getStr(R.string.app_motto)

        ThemingUtil.About.appMotto(this, getAppTheme())
    }


    private fun initVersion() = with(mRootView.appVersionTv) {
        text = mStringProvider.getString(R.string.app_version_template, BuildConfig.VERSION_NAME)

        ThemingUtil.About.appMotto(this, getAppTheme())
    }


    private fun initReferenceButtons() {
        initVisitWebsiteReferenceButton()
        initTermsOfUseReferenceButton()
        initPrivacyPolicyReferenceButton()
        initCandyLinkReferenceButton()
    }


    private fun initVisitWebsiteReferenceButton() = with(mRootView.visitWebsiteRbv) {
        setSubtitleText(getStr(R.string.about_fragment_reference_button_visit_website_subtitle_text))
        setTitleText(getStr(R.string.about_fragment_reference_button_visit_website_title_text))

        setOnClickListener {
            mPresenter.onReferenceButtonClicked(AboutReference.WEBSITE)
        }

        ThemingUtil.About.visitWebsiteReferenceButton(this, getAppTheme())
    }


    private fun initTermsOfUseReferenceButton() = with(mRootView.termsOfUseRbv) {
        setSubtitleText(getStr(R.string.about_fragment_reference_button_terms_of_use_subtitle_text))
        setTitleText(getStr(R.string.about_fragment_reference_button_terms_of_use_title_text))

        setOnClickListener {
            mPresenter.onReferenceButtonClicked(AboutReference.TERMS_OF_USE)
        }

        ThemingUtil.About.termsOfUseReferenceButton(this, getAppTheme())
    }


    private fun initPrivacyPolicyReferenceButton() = with(mRootView.privacyPolicyRbv) {
        setSubtitleText(getStr(R.string.about_fragment_reference_button_privacy_policy_subtitle_text))
        setTitleText(getStr(R.string.about_fragment_reference_button_privacy_policy_title_text))

        setOnClickListener {
            mPresenter.onReferenceButtonClicked(AboutReference.PRIVACY_POLICY)
        }

        ThemingUtil.About.privacyPolicyReferenceButton(this, getAppTheme())
    }


    private fun initCandyLinkReferenceButton() = with(mRootView.candyLinkRbv) {
        setSubtitleText(getStr(R.string.about_fragment_reference_button_candy_link_subtitle_text))
        setTitleText(getStr(R.string.about_fragment_reference_button_candy_link_title_text))

        setOnClickListener {
            mPresenter.onReferenceButtonClicked(AboutReference.CANDY_LINK)
        }

        ThemingUtil.About.candyLinkReferenceButton(this, getAppTheme())
    }


    private fun initBottomContainer() {
        initSocialMediaButtonsContainer()
        initCompanyName()
        initCompanyAddress()
        initCopyright()
    }


    private fun initSocialMediaButtonsContainer() {
        initFacebookButton()
        initTwitterButton()
        initTelegramButton()
        initMediumButton()
    }


    private fun initFacebookButton() = with(mRootView.facebookBtnIv) {
        setOnClickListener {
            mPresenter.onSocialMediaButtonClicked(SocialMediaType.FACEBOOK)
        }

        ThemingUtil.About.facebookButton(this, getAppTheme())
    }


    private fun initTwitterButton() = with(mRootView.twitterBtnIv) {
        setOnClickListener {
            mPresenter.onSocialMediaButtonClicked(SocialMediaType.TWITTER)
        }

        ThemingUtil.About.twitterButton(this, getAppTheme())
    }


    private fun initTelegramButton() = with(mRootView.telegramBtnIv) {
        setOnClickListener {
            mPresenter.onSocialMediaButtonClicked(SocialMediaType.TELEGRAM)
        }

        ThemingUtil.About.telegramButton(this, getAppTheme())
    }


    private fun initMediumButton() = with(mRootView.mediumBtnIv) {
        setOnClickListener {
            mPresenter.onSocialMediaButtonClicked(SocialMediaType.MEDIUM)
        }

        ThemingUtil.About.mediumButton(this, getAppTheme())
    }


    private fun initCompanyName() = with(mRootView.companyNameIv) {
        ThemingUtil.About.companyName(this, getAppTheme())
    }


    private fun initCompanyAddress() = with(mRootView.companyAddressTv) {
        text = getStr(R.string.company_address)

        ThemingUtil.About.companyAddress(this, getAppTheme())
    }


    private fun initCopyright() = with(mRootView.copyrightTv) {
        text = mStringProvider.getString(
            R.string.copyright_template,
            get<TimeFormatter>().getYear(System.currentTimeMillis())
        )

        ThemingUtil.About.copyright(this, getAppTheme())
    }


    override fun shareText(text: String, chooserTitle: String) {
        get<SharingHandler>().shareText(ctx, text, chooserTitle)
    }


    override fun launchBrowser(url: String) {
        get<BrowserHandler>().launchBrowser(ctx, url, getAppTheme())
    }


    override fun getContentLayoutResourceId() = R.layout.about_fragment_layout


}