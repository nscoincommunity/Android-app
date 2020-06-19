package com.stocksexchange.android.ui.referral

import com.stocksexchange.android.R
import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.model.ReferralMode
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.api.exceptions.rest.ReferralException
import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.core.handlers.ClipboardHandler

class ReferralPresenter(
    view: ReferralContract.View,
    model: ReferralModel,
    private val clipboardHandler: ClipboardHandler,
    private val sessionManager: SessionManager
) : BasePresenter<ReferralContract.View, ReferralModel>(view, model),
    ReferralContract.ActionListener, ReferralModel.ActionListener {


    var profileInfo: ProfileInfo = (sessionManager.getProfileInfo() ?: ProfileInfo.STUB_PROFILE_INFO)

    var referralMode: ReferralMode = ReferralMode.REFERRAL_CODE_ALREADY_PROVIDED




    init {
        model.setActionListener(this)

        referralMode = if(profileInfo.referralProgram.isInvited) {
            ReferralMode.REFERRAL_CODE_ALREADY_PROVIDED
        } else {
            ReferralMode.ABLE_TO_PROVIDE_REFERRAL_CODE
        }
    }


    override fun onShareButtonClicked() {
        mView.shareText(
            mStringProvider.getString(
                R.string.app_sharing_template,
                profileInfo.referralProgram.invitationLink
            ),
            mStringProvider.getString(R.string.action_share_via)
        )
    }


    override fun onReferralLinkClicked() {
        copyInvitationLinkToClipboard()
    }


    override fun onLeftButtonClicked() {
        copyInvitationLinkToClipboard()
    }


    private fun copyInvitationLinkToClipboard() {
        clipboardHandler.copyText(profileInfo.referralProgram.invitationLink)

        mView.showToast(mStringProvider.getString(R.string.link_copied_to_clipboard))
    }


    override fun onRightButtonClicked() {
        when(referralMode) {
            ReferralMode.REFERRAL_CODE_ALREADY_PROVIDED -> onShareButtonClicked()
            ReferralMode.ABLE_TO_PROVIDE_REFERRAL_CODE -> onStartButtonClicked()
            ReferralMode.WAITING_FOR_REFERRAL_CODE -> onConfirmButtonClicked()
        }
    }


    private fun onStartButtonClicked() {
        updateReferralMode(ReferralMode.WAITING_FOR_REFERRAL_CODE)

        mView.showReferralCodeInput()
    }


    private fun updateReferralMode(mode: ReferralMode) {
        referralMode = mode

        mView.updateReferralMode(mode)
    }


    private fun onConfirmButtonClicked() {
        val referralCode = mView.getReferralCodeInput()

        if(referralCode.isBlank()) {
            showErrorDialog(mStringProvider.getString(R.string.error_empty_code))

            mView.setReferralCodeInputViewState(InputViewState.ERRONEOUS)

            return
        }

        mModel.performReferralCodeProvision(referralCode)
    }


    override fun onRequestSent(requestType: Int) {
        mView.showProgressBar()
    }


    override fun onResponseReceived(requestType: Int) {
        mView.hideProgressBar()
    }


    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        val profileInfo = (sessionManager.getProfileInfo() ?: return)
        val updatedProfileInfo = profileInfo.copy(referralProgram = profileInfo.referralProgram.copy(
            isInvited = true
        ))

        mModel.saveProfileInfo(updatedProfileInfo) {
            sessionManager.setProfileInfo(updatedProfileInfo)

            this.profileInfo = updatedProfileInfo

            updateReferralMode(ReferralMode.REFERRAL_CODE_ALREADY_PROVIDED)

            mView.showNotice()

            showInfoDialog(
                title = mStringProvider.getString(R.string.referral_fragment_referral_provision_success_dialog_title),
                content = mStringProvider.getString(R.string.referral_fragment_referral_provision_success_dialog_content)
            )
        }
    }


    override fun onRequestFailed(requestType: Int, exception: Throwable, metadata: Any) {
        when(exception) {
            is ReferralException -> showErrorDialog(when(exception.error) {
                ReferralException.Error.INVALID_CODE -> mStringProvider.getString(R.string.error_invalid_code)
                ReferralException.Error.UNKNOWN -> exception.message
            })

            else -> mView.showToast(mStringProvider.getString(R.string.error_something_went_wrong))
        }
    }


    override fun onNavigateUpPressed(): Boolean {
        return (if(handleBackPress()) true else super.onNavigateUpPressed())
    }


    override fun onBackPressed(): Boolean {
        return onNavigateUpPressed()
    }


    private fun handleBackPress(): Boolean {
        if(referralMode != ReferralMode.WAITING_FOR_REFERRAL_CODE) {
            return false
        }

        updateReferralMode(ReferralMode.ABLE_TO_PROVIDE_REFERRAL_CODE)

        mView.showNotice()

        return true
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            referralMode = it.referralMode
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            referralMode = referralMode
        ))
    }


}