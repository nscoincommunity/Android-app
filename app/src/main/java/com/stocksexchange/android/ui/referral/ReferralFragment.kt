package com.stocksexchange.android.ui.referral

import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.text.toSpannable
import com.stocksexchange.android.R
import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.model.ReferralMode
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.utils.handlers.SharingHandler
import com.stocksexchange.core.handlers.QrCodeHandler
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.helpers.crossFade
import kotlinx.android.synthetic.main.referral_fragment_layout.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ReferralFragment : BaseFragment<ReferralPresenter>(), ReferralContract.View {


    override val mPresenter: ReferralPresenter by inject { parametersOf(this) }




    override fun init() {
        super.init()

        initContentContainer()
        initToolbar()
        initTitle()
        initSubtitle()
        initReferralCodeImage()
        initReferralLink()
        initNotice()
        initReferralCodeInput()
        initButtons()
    }


    private fun initContentContainer() = with(mRootView.contentContainerRl) {
        ThemingUtil.Referral.contentContainer(this, getAppTheme())
    }


    private fun initToolbar() = with(mRootView.toolbar) {
        setTitleText(getStr(R.string.referral))

        setOnLeftButtonClickListener {
            mPresenter.onNavigateUpPressed()
        }
        setOnRightButtonClickListener {
            mPresenter.onShareButtonClicked()
        }

        updateToolbarRightButtonState()

        ThemingUtil.Referral.toolbar(this, getAppTheme())
    }


    private fun initTitle() = with(mRootView.titleTv) {
        text = getStr(R.string.referral_fragment_title_text)

        ThemingUtil.Referral.title(this, getAppTheme())
    }


    private fun initSubtitle() = with(mRootView.subtitleTv) {
        text = mStringProvider.getReferralSubtitle(mPresenter.profileInfo.referralProgram.memberCount)

        ThemingUtil.Referral.subtitle(this, getAppTheme())
    }


    private fun initReferralCodeImage() = with(mRootView.referralQrCodeIv) {
        setImageBitmap(get<QrCodeHandler>().generateQrCodeImage(
            text = mPresenter.profileInfo.referralProgram.invitationLink,
            size = dimenInPx(R.dimen.referral_fragment_referral_qr_code_image_size)
        ))
    }


    private fun initReferralLink() = with(mRootView.referralLinkTv) {
        val originalText = mPresenter.profileInfo.referralProgram.invitationLink

        text = originalText.toSpannable().apply {
            this[0, length] = UnderlineSpan()
        }

        setOnClickListener {
            mPresenter.onReferralLinkClicked()
        }

        ThemingUtil.Referral.referralLink(this, getAppTheme())
    }


    private fun initNotice() = with(mRootView.noticeTv) {
        text = getStr(R.string.referral_fragment_notice_text)

        if(mPresenter.referralMode != ReferralMode.WAITING_FOR_REFERRAL_CODE) {
            makeVisible()
        } else {
            makeInvisible()
        }

        ThemingUtil.Referral.notice(this, getAppTheme())
    }


    private fun initReferralCodeInput() = with(mRootView.referralCodeIv) {
        setEtInputType(ctx.getKeyboardNumericInputType())
        setEtHintText(getStr(R.string.referral_fragment_referral_code_iv_hint_text))

        if(mPresenter.referralMode == ReferralMode.WAITING_FOR_REFERRAL_CODE) {
            makeVisible()
        } else {
            makeInvisible()
        }

        ThemingUtil.Referral.referralInput(this, getAppTheme())
    }


    private fun initButtons() {
        initLeftButton()
        initRightButton()
    }


    private fun initLeftButton() = with(mRootView.leftBtn) {
        text = getStr(R.string.action_copy)

        setOnClickListener {
            mPresenter.onLeftButtonClicked()
        }

        updateLeftButtonStyle()
    }


    private fun initRightButton() = with(mRootView.rightBtn) {
        setOnClickListener {
            mPresenter.onRightButtonClicked()
        }

        updateRightButtonStyle()
        updateRightButtonText()
    }


    override fun showProgressBar() = mRootView.toolbar.showProgressBar()


    override fun hideProgressBar() = mRootView.toolbar.hideProgressBar()


    override fun showReferralCodeInput() {
        crossFadeTwoViews(
            viewToHide = mRootView.noticeTv,
            viewToShow = mRootView.referralCodeIv
        )
    }


    override fun showNotice() {
        crossFadeTwoViews(
            viewToHide = mRootView.referralCodeIv,
            viewToShow = mRootView.noticeTv
        )
    }


    private fun crossFadeTwoViews(viewToHide: View, viewToShow: View) {
        crossFade(
            viewToHide = viewToHide,
            viewToShow = viewToShow,
            onBeforeShowingView = {
                viewToHide.makeInvisible()
                viewToHide.alpha = 1f

                viewToShow.alpha = 0f
                viewToShow.makeVisible()
            }
        )
    }


    override fun shareText(text: String, chooserTitle: String) {
        get<SharingHandler>().shareText(ctx, text, chooserTitle)
    }


    private fun updateToolbarRightButtonState() = with(mRootView.toolbar) {
        if(mPresenter.referralMode == ReferralMode.REFERRAL_CODE_ALREADY_PROVIDED) {
            hideRightButton()
        } else {
            showRightButton()
        }
    }


    private fun updateLeftButtonStyle() {
        with(ThemingUtil.Referral) {
            when(mPresenter.referralMode) {
                ReferralMode.WAITING_FOR_REFERRAL_CODE -> {
                    secondaryButton(mRootView.leftBtn, getAppTheme())
                }

                else -> primaryButton(mRootView.leftBtn, getAppTheme())
            }
        }
    }


    private fun updateRightButtonStyle() {
        with(ThemingUtil.Referral) {
            when(mPresenter.referralMode) {
                ReferralMode.WAITING_FOR_REFERRAL_CODE -> {
                    primaryButton(mRootView.rightBtn, getAppTheme())
                }

                else -> secondaryButton(mRootView.rightBtn, getAppTheme())
            }
        }
    }


    private fun updateRightButtonText() = with(mRootView.rightBtn) {
        text = getRightButtonText()
    }


    override fun updateReferralMode(referralMode: ReferralMode) {
        updateToolbarRightButtonState()
        updateLeftButtonStyle()
        updateRightButtonStyle()
        updateRightButtonText()
    }


    override fun setReferralCodeInputViewState(state: InputViewState) = with(mRootView.referralCodeIv) {
        setState(state)
    }


    override fun getReferralCodeInput(): String = mRootView.referralCodeIv.getContent()


    private fun getRightButtonText(): String {
        return getStr(when(mPresenter.referralMode) {
            ReferralMode.REFERRAL_CODE_ALREADY_PROVIDED -> R.string.action_share
            ReferralMode.ABLE_TO_PROVIDE_REFERRAL_CODE -> R.string.action_start
            ReferralMode.WAITING_FOR_REFERRAL_CODE -> R.string.action_confirm
        })
    }


    override fun getContentLayoutResourceId(): Int = R.layout.referral_fragment_layout


}